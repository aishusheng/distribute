package com.chinalin.distributelock.java_api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @date:2019/02/19 10:22
 * @author:艾书胜
 * 使用Java原生API来实现zookeeper的分布式共享锁
 * 当前类是创建客户端连接
 */
public class Client {

    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址
    private static final int SESSION_TIMEOUT=5000;//session的过期时间

    //连接zookeeper
    public static ZooKeeper getInstance(){
        final CountDownLatch conectStatus = new CountDownLatch(1);//等待连接的计数器
        try {
            ZooKeeper zooKeeper = new ZooKeeper(CONNECTION_URL, SESSION_TIMEOUT, new Watcher() {
               //使用watcher机制来监控连接状态
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState()==Event.KeeperState.SyncConnected){//已建立连接
                        conectStatus.countDown();//计数器减1
                    }
                }
            });
            conectStatus.await();//等待连接，阻塞
            return zooKeeper;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //把当前定义的session会话过期时间供其他类使用
    public static int getSessionTimeout() {
        return SESSION_TIMEOUT;
    }
}
