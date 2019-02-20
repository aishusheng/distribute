package com.chinalin.master.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @date:2019/02/19 15:22
 * @author:艾书胜
 * 使用zkclient来操作zookeeper实现master选举
 * 当前类是一个测试类
 */
public class Test {

    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址

    private static final int SESSION_TIMEOUT=5000;//session会话过期时间

    private static final int CONNECTION_TIMEOUT=10000;//连接超时时间

    public static void main(String[] args){

        List<MasterSelector> list = new ArrayList<>();
        try {
            for(int i=0;i<10;i++){
                ZkClient zkClient = new ZkClient(CONNECTION_URL,SESSION_TIMEOUT,CONNECTION_TIMEOUT,new SerializableSerializer());


                //模拟多进程
                UserCenter userCenter = new UserCenter();
                userCenter.setPc_id(i);
                userCenter.setPc_name("客户端->"+i);
                MasterSelector masterSelector = new MasterSelector(userCenter,zkClient);
                list.add(masterSelector);
                masterSelector.start();//开启选举

                TimeUnit.SECONDS.sleep(5);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            for(MasterSelector masterSelector:list){
                masterSelector.stop();//停止选举
            }
        }

    }
}
