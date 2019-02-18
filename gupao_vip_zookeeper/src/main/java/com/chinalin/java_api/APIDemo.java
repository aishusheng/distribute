package com.chinalin.java_api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @date:2019/02/17 23:34
 * @author:艾书胜
 * 使用Java原生API来操作zookeeper
 */
public class APIDemo implements Watcher {

    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址
    private static final int SESSION_TIMEOUT=5000;//session的过期时间

    private static CountDownLatch countDownLatch=new CountDownLatch(1);//计数器用于等待zookeeper的连接是否完成

    private static ZooKeeper zooKeeper;

    private static Stat stat = new Stat();//节点数据状态

    public static void main(String[] args){

        try {
            //创建连接
            zooKeeper = new ZooKeeper(CONNECTION_URL, SESSION_TIMEOUT,new APIDemo());
            countDownLatch.await();//等待zookeeper的连接完成，阻塞下面的代码执行，直到zookeeper客户端连接完成
            System.out.println("zookeeper已连接");
            //创建节点
            //参数1：要创建节点的路径；参数2：当前节点的数据，参数3：当前节点的权限，参数4：当前节点的类型（持久节点，持久有序节点，临时节点，临时有序节点）
            String result = zooKeeper.create("/testZookeeper1","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("节点创建成功:"+result);
            TimeUnit.SECONDS.sleep(5);//等待5秒
            //删除节点
            zooKeeper.delete("/aishusheng",-1);
            TimeUnit.SECONDS.sleep(2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getState()==Event.KeeperState.SyncConnected){//当前客户端已经连接

            if(Event.EventType.None==event.getType() && null==event.getPath()){//第一次建立连接
                countDownLatch.countDown();//通过计算器去控制zookeeper连接之后的后续操作
            }else if(event.getType()==Event.EventType.NodeDataChanged){//当前节点数据发生变化时触发
                try {
                    byte[] bytes=zooKeeper.getData(event.getPath(),true,stat);//节点数据
                    System.out.println("路径："+event.getPath()+"->改变后的值："+bytes);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }else if(event.getType()==Event.EventType.NodeChildrenChanged){//当前节点的子节点发生变化时触发

            }else if(event.getType()==Event.EventType.NodeCreated){//当前节点被创建时触发
                try {
                    byte[] bytes = zooKeeper.getData(event.getPath(),true,stat);//创建的节点信息
                    System.out.println("路径:"+event.getPath()+"->节点数据:"+bytes);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(event.getType()==Event.EventType.NodeDeleted){//当前节点被删除时触发
                System.out.println("触发了删除节点");
            }
        }
    }
}
