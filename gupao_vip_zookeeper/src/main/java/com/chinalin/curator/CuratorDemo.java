package com.chinalin.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @date:2019/02/18 15:52
 * @author:艾书胜
 * 使用curator来操作zookeeper,目前是最常用的方式
 */
public class CuratorDemo {

    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址

    private static final int SESSION_TIMEOUT=5000;//session会话过期时间

    private static final int CONNECTION_TIMEOUT=10000;//连接超时时间

    public static void main(String[] args){
        //建立连接
        CuratorFramework curatorFramework = getCuratorFramework();
        //创建节点
        //createNode(curatorFramework);
        //删除节点
        //deleteNode(curatorFramework,"/curator");
        //获取节点数据
        //getNodeData(curatorFramework,"/zkClientTest");
        //更新节点数据
        //setNodeData(curatorFramework,"/zkClientTest");
        //curator中的异步操作
        //asyOperator(curatorFramework);
        //curator中的事务操作
        //transactionOperator(curatorFramework);
        //curator中的节点监听事件
        eventOperator(curatorFramework);

    }



    //连接zookeeper
    private static CuratorFramework getCuratorFramework(){
        /*创建curaotr连接zookeeper的重试策略
        * curator提供了5种重试策略:
        * 策略1：ExponentialBackoffRetry()，衰减重试,第一个参数为重试的间隔时间，第二个参数为重试次数
        * 策略2：RetryNTimes,指定最大重试次数
        * 策略3：RetryOneTime，仅仅重试一次
        * 策略4：RetryUntilElapsed，一直重试直到规定的时间
        * 策略5：RetryForever：永远重试策略
        */
        RetryPolicy policy = new ExponentialBackoffRetry(1000,3);
        //方式1；创建curator连接zookeeper对象
        //CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTION_URL,SESSION_TIMEOUT,CONNECTION_TIMEOUT,policy);
        //方式2：使用fluent风格即链式编程风格来创建curator连接zookeeper对象
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                                                                            .connectString(CONNECTION_URL)
                                                                            .sessionTimeoutMs(SESSION_TIMEOUT)
                                                                            .connectionTimeoutMs(CONNECTION_TIMEOUT)
                                                                            .retryPolicy(policy)
                                                                            .build();
        curatorFramework.start();//启动连接
        System.out.println("使用curator连接zookeeper成功");
        return curatorFramework;
    }

    //创建节点
    private static void createNode(CuratorFramework curatorFramework){
        try {
           String path = curatorFramework.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .forPath("/curator/curator1/curator2","test".getBytes());
            System.out.println("创建的节点信息:"+path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除节点
    private static void deleteNode(CuratorFramework curatorFramework,String path){
        try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
            System.out.println("节点删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取节点数据
    private static void getNodeData(CuratorFramework curatorFramework,String path){
        try {
            Stat stat = new Stat();//保存节点的状态信息
            //.storingStatIn方法用来获取当前节点的stat状态信息,如果不想获取状态信息，这个方法可以不用写
            byte[] bytes=curatorFramework.getData().storingStatIn(stat).forPath(path);
            System.out.println("节点数据:"+new String(bytes));
            System.out.println("节点状态信息:"+stat.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //更新节点数据
    private static void setNodeData(CuratorFramework curatorFramework,String path){
        try {
            Stat stat = curatorFramework.setData().forPath(path,"123".getBytes());
            System.out.println("节点更新之后的数据信息:"+stat.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //zkclient和原生API都有异步操作特性
    //curator中的异步操作，因为操作zookeeper时，有时候操作需要的时间会比较长，处于阻塞状态，导致后面的逻辑没有执行，针对这种情况，可以使用异步操作
    private static void asyOperator(CuratorFramework curatorFramework){
        CountDownLatch countDownLatch = new CountDownLatch(1);//增加这个为了看到下面这个异步的效果
        ExecutorService service = Executors.newFixedThreadPool(1);//创建一个线程池来执行下面的异步方法
        //这里以创建节点为例来说明curator中的异步操作
        try {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback(){

                //回调方法,当前的意思是节点创建完成之后会调用这个回调方法
                @Override
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                    System.out.println("当前线程："+Thread.currentThread().getName());
                    System.out.println("响应结果码:"+curatorEvent.getResultCode());
                    System.out.println("当前操作类型:"+curatorEvent.getType());
                    countDownLatch.countDown();
                }
            },service).forPath("/test","123".getBytes());
            countDownLatch.await();
            service.shutdown();//关闭线程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //curator的事务操作，这是curator独有的
    private static void transactionOperator(CuratorFramework curatorFramework){
        try {
            //下面这行代码的作用是把创建一个节点和修改另一个节点的数据放在一个事务里面
            //同时成功时才会成功，只要有一个操作不成功整个操作结果就是不成功的
            Collection<CuratorTransactionResult> collection=curatorFramework.inTransaction().create().forPath("/trans","123".getBytes()).and().setData().forPath("/zkClientTest","111".getBytes()).and().commit();
            for(CuratorTransactionResult result:collection){//输出结果信息
                System.out.println(result.getForPath()+"->"+result.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //curator中提供了三种watcher通知来监听节点事件（注意，curator中的监听是重复监听,即监听自动重复注册）
    //第一种：PathCache：监听一个路径下子节点的创建、删除、节点数据更新，并缓存更新后的子节点数据
    //第二种：NodeCache：监听一个节点的创建、更新和删除,并缓存更新后的节点数据
    //第三种：TreeCache：监听一个路径下的创建、更新、删除事件，缓存一个路径下的所有子节点的数据，是上面两种方法的合体
    private static void eventOperator(CuratorFramework curatorFramework){

        try {
            //以下演示的是第一种PathCache监听事件
            PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,"/event1",true);//给指定节点添加子节点监听事件
            pathChildrenCache.start();//启动监听
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {

                    switch (pathChildrenCacheEvent.getType()){//判断当前子节点事件类型
                        case CHILD_ADDED://添加子节点
                            System.out.println("添加子节点");
                            break;
                        case CHILD_REMOVED://删除子节点
                            System.out.println("删除子节点");
                            break;
                        case CHILD_UPDATED://更新子节点
                            System.out.println("更新子节点");
                            break;
                        default:System.out.println("其他操作");break;
                    }
                }
            });
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/event1","event1".getBytes());//先创建父节点
            TimeUnit.SECONDS.sleep(2);
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/event1/event2","event2".getBytes());//创建子节点
            TimeUnit.SECONDS.sleep(3);
            curatorFramework.setData().forPath("/event1/event2","update".getBytes());//修改子节点数据
            TimeUnit.SECONDS.sleep(3);
            curatorFramework.delete().forPath("/event1/event2");//删除子节点
            TimeUnit.SECONDS.sleep(5);

            //以下演示的是第二种NodeCache监听事件
            //第一个参数是连接zookeeper的客户端对象，第二个参数为监听哪个节点路径，第三个参数是缓存数据是否进行压缩
           /* NodeCache nodeCache = new NodeCache(curatorFramework,"/zkClientTest",false);//给指定节点添加节点监听事件
            nodeCache.start(true);//启动监听
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                //注意：NodeCacheListener无法监听到节点删除通知
                @Override
                public void nodeChanged() throws Exception {
                    System.out.println("节点数据发生变化,变化后的结果是:"+new String(nodeCache.getCurrentData().getData()));
                }
            });//添加监听事件
            curatorFramework.setData().forPath("/zkClientTest","aaa".getBytes());//修改节点数据
            TimeUnit.SECONDS.sleep(5);*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
