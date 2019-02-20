package com.chinalin.distributelock.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @date:2019/02/19 22:19
 * @author:艾书胜
 * 使用curator来操作zookeeper实现分布式共享锁
 * 当前类是连使用curator连接zookeeper的类
 */
public class CuratorClient {

    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址

    private static final int SESSION_TIMEOUT=5000;//session会话过期时间

    private static final int CONNECTION_TIMEOUT=10000;//连接超时时间

    //使用curator连接zookeeper
    public static CuratorFramework getInstance(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTION_URL,SESSION_TIMEOUT,CONNECTION_TIMEOUT,new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();//启动连接
        System.out.println("使用curator连接zookeeper成功!");
        return curatorFramework;
    }
}
