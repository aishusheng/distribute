package com.chinalin.master.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @date:2019/02/19 14:18
 * @author:艾书胜
 * 使用zkclient来操作zookeeper实现master选举
 * 当前类用来模拟一个master选举过程
 */
public class MasterSelector {

    private ZkClient zkClient;//使用zkclient来连接zookeeper

    private final static String MASTER_PATH="/master";//创建一个临时节点，用来保存需要争抢的信息，比如说服务器信息

    private IZkDataListener dataListener;//注册节点内容变化

    private UserCenter server;//其他服务器信息

    private UserCenter master;//master节点（当前争抢到资源的服务器信息）

    private static boolean isRunning = false;//控制选举的开关

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);//创建一个定时调度任务


    public MasterSelector(UserCenter server,ZkClient zkClient) {
        this.server = server;
        this.zkClient=zkClient;
        //注册监听
        this.dataListener=new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            //节点如果被删除，发起选举操作
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("发生故障，触发节点删除事件->"+dataPath);
                chooseMaster();
            }
        };
    }

    //开始选举方法，对外提供
    public void start(){
        if(!isRunning){
            isRunning=true;
            this.zkClient.subscribeDataChanges(MASTER_PATH,dataListener);//订阅一个节点数据改变的watcher通知
            //开始选举
            chooseMaster();
        }
    }

    //停止选举方法
    public void stop(){
        if(isRunning){
            isRunning=false;
            this.scheduledExecutorService.shutdown();//关闭定时调度任务
            this.zkClient.unsubscribeDataChanges(MASTER_PATH,dataListener);//取消订阅的watcher注册通知
            releaseMaster();//释放master
        }
    }

    //具体选举master方法
    private void chooseMaster(){
        if(!isRunning){
            System.out.println("当前服务没有启动");
            return;
        }
        try{
            this.zkClient.createEphemeral(MASTER_PATH,this.server);//创建一个临时节点，临时节点的数据为服务器信息
            //如果临时节点创建成功，则当前临时节点就是master节点
            //把server节点赋值给master节点
            this.master=this.server;
            System.out.println(this.master.getPc_name()+"->我现在已经是master了，你们都要听我的");

            //定时器来模拟master出现故障，master释放
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    releaseMaster();//master释放，出现故障
                }
            },5, TimeUnit.SECONDS);//每5秒钟出现一次故障
        }catch (ZkNodeExistsException e){//节点存在即已经选中了master了
            System.out.println("master节点已经存在");
            UserCenter userCenter=this.zkClient.readData(MASTER_PATH,true);//获取master信息，如果不存在则重新选举master
            if(null==userCenter){
                chooseMaster(); //再次获取master,递归轮训选举master
            }else{
                this.master=userCenter;
            }

        }


    }

    //释放锁(释放master节点),用来模拟故障发生
    private void releaseMaster(){
        //判断当前是否是master，只有master才释放
        if(checkIsMaster()){
            this.zkClient.deleteRecursive(MASTER_PATH);//删除master节点
        }
    }

    //判断当前的server是否是master
    private boolean checkIsMaster(){
        UserCenter userCenter=this.zkClient.readData(MASTER_PATH);//获取master节点数据信息
        if(userCenter.getPc_name().equals(this.server.getPc_name())){
            this.master=userCenter;
            return true;
        }
        return false;
    }
}
