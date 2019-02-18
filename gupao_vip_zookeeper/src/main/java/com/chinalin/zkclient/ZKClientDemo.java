package com.chinalin.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @date:2019/02/18 14:53
 * @author:艾书胜
 * 通过zkclient来操作zookeeper
 */
public class ZKClientDemo {
    private static final String CONNECTION_URL="192.168.8.129:2181,192.168.8.130:2181,192.168.8.131:2181";//zookeeper集群地址

    private static final int CONNECTION_TIMEOUT=10000;//连接超时时间

    public static void main(String[] args){

        ZkClient zk = getInstance();//连接zookeeper
        //createNode(zk);//创建一个节点
        //createNode2(zk);//递归创建持久化节点
        //deleteNode(zk,"/test");
        //deleteNode2(zk,"/test1");
        //getNode(zk,"/zookeeper");
        zkClientWatcher(zk,"/zkClientTest");
    }

    //连接zookeeper
    private static ZkClient getInstance(){
        ZkClient zk = new ZkClient(CONNECTION_URL,CONNECTION_TIMEOUT);//连接zookeeper
        System.out.println(zk+"->success");
        return zk;
    }

    //创建节点
    private static void createNode(ZkClient zk){
        zk.createPersistent("/zkClientTest","test");//创建一个\节点
        System.out.println("节点创建成功");
    }

    //递归创建节点
    private static void createNode2(ZkClient zk){
        zk.createPersistent("/test1/t1/t2/t3",true);
    }

    //删除节点
    private static void deleteNode(ZkClient zk,String path){
        zk.delete(path);//删除节点
    }

    //递归删除节点,传递父节点路径
    private static void deleteNode2(ZkClient zk,String path){
        zk.deleteRecursive(path);
        System.out.println("递归删除节点成功");
    }

    //获取子节点信息
    private static void getNode(ZkClient zk,String path){
        List<String> list = zk.getChildren(path);
        System.out.println("子节点信息:"+list);

    }

    //Zkclient中的watcher通知
    private static void zkClientWatcher(ZkClient zk,String path){
        //此处订阅一个节点数据改变的watcher通知,异步通知
        zk.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("节点数据发生改变");
                System.out.println("节点路径:"+dataPath+",节点数据:"+data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("节点数据删除");
            }
        });

        //修改当前节点的数据
        zk.writeData(path,"update data");
        try {
            TimeUnit.SECONDS.sleep(2);//由于watcher通知是异步通知，所以这里必须等待几秒中，否则上面那个subscribeDataChanges方法还没执行，当前主方法就结束了
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
