package com.chinalin.distributelock.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @date:2019/02/19 16:26
 * @author:艾书胜
 * 使用zkclient来操作zookeeper实现分布式共享锁
 * 当前类是连使用zkclient连接zookeeper的类
 */
public class ZKClient {

    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址
    private static final int SESSION_TIMEOUT=5000;//session的过期时间
    private static final int CONNECION_TIMEOUT=10000;//连接超时时间

    public static ZkClient getInstance(){
        ZkClient zkClient = new ZkClient(CONNECTION_URL,SESSION_TIMEOUT,CONNECION_TIMEOUT);//连接zookeeper
        System.out.println("zkclien->成功连接上zookeeper");
        return zkClient;
    }


}
