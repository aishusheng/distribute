package com.chinalin.java_api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @date:2019/02/18 14:12
 * @author:艾书胜
 * 使用原生API来操作zookeeper权限
 */
public class ApiControllDemo implements Watcher {

    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址
    private static final int SESSION_TIMEOUT=5000;//session的过期时间

    private static CountDownLatch countDownLatch=new CountDownLatch(1);//计数器用于等待zookeeper的连接是否完成

    private static ZooKeeper zooKeeper;

    private static Stat stat = new Stat();//节点数据状态

    public static void main(String[] args){
        try {
            zooKeeper = new ZooKeeper(CONNECTION_URL, SESSION_TIMEOUT,new ApiControllDemo());
            countDownLatch.await();//等待zookeeper的连接完成，阻塞下面的代码执行，直到zookeeper客户端连接完成
            System.out.println("zookeeper已连接");

            //zookeeper的权限acl包括(create/delete/admin/read/write)
            //权限控制模式:1、ip,控制哪些ip可以访问;2、digest,通过用户名、密码的方式访问;3、world,开放式的权限控制模式，即访问权限对所有用户开放；4、super,超级用户，可以对zookeeper上的数据进行操作
            zooKeeper.addAuthInfo("digest","root:root".getBytes());//设置权限访问方式
            //创建一个节点
            zooKeeper.create("/auth1","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            //通过另外一个客户端去删除上面创建的节点
            ZooKeeper zk=new ZooKeeper(CONNECTION_URL, SESSION_TIMEOUT,new ApiControllDemo());
            zk.addAuthInfo("digest","root:root".getBytes());
            zk.delete("/auth1",-1);

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
        if(event.getState()==Event.KeeperState.SyncConnected) {//当前客户端已经连接

            if (Event.EventType.None == event.getType() && null == event.getPath()) {//第一次建立连接
                countDownLatch.countDown();//通过计算器去控制zookeeper连接之后的后续操作
            }
        }
    }
}
