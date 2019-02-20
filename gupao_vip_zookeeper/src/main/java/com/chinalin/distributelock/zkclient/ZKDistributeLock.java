package com.chinalin.distributelock.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

/**
 * @date:2019/02/19 16:32
 * @author:艾书胜
 * 使用zkclient来操作zookeeper实现分布式共享锁
 * 当前类是使用zkclient来实现zookeeper的分布式共享锁
 */
public class ZKDistributeLock {

    private static final String ROOT_LOCK="/LOCKS";//根节点，用于在当前根节点下创建临时有序节点
    private static final byte[] data={1,2};//临时有序节点中的数据
    private ZkClient zkClient;//zkclient连接zookeeper的实例
    private String lockID;//记录当前获取锁的节点id即临时有序节点路径
    private CountDownLatch countDownLatch = new CountDownLatch(1);//计算器

    //实例化zkclient
    public ZKDistributeLock() {
        this.zkClient = ZKClient.getInstance();
    }

    //获取锁
    public synchronized boolean lock(){
        try{
            //创建临时有序节点
            this.lockID=this.zkClient.createEphemeralSequential(ROOT_LOCK+"/",data, ZooDefs.Ids.OPEN_ACL_UNSAFE);
            System.out.println(Thread.currentThread().getName()+"->成功创建了临时有序节点，节点id:["+this.lockID+"],开始竞争锁");
            List<String> childNodes=this.zkClient.getChildren(ROOT_LOCK);//获取根节点下的所有子节点
            //对获取到的子节点按照编号从小到大进行排序
            SortedSet<String> sortedSet = new TreeSet<>();
            if(null != childNodes && !childNodes.isEmpty()){
                for(String children:childNodes){
                    sortedSet.add(ROOT_LOCK+"/"+children);
                }
            }
            //获取所有子节点中编号最小的节点
            String mixNode = sortedSet.first();
            if(this.lockID.equals(mixNode)){//当前创建的临时有序节点就是最小节点，即获取锁
                System.out.println(Thread.currentThread().getName()+"->成功获取锁，lock节点:["+this.lockID+"]");
                return true;
            }else{//需要监控比当前创建的临时有序节点小的上一个节点
                SortedSet<String> less = ((TreeSet<String>) sortedSet).headSet(this.lockID);
                if(!less.isEmpty()){
                    //获取当前创建的临时有序节点的前一个节点，因为只有监听当前节点的前一个节点，前一个节点释放锁之后当前节点就是最新节点，即可来获取锁了
                    String lastNode=less.last();
                    //监控比当前创建的临时节点要小的上一个节点
                    this.zkClient.subscribeDataChanges(lastNode, new IZkDataListener() {
                        @Override
                        public void handleDataChange(String dataPath, Object data) throws Exception {

                        }

                        @Override
                        public void handleDataDeleted(String dataPath) throws Exception {
                            countDownLatch.countDown();//计数器减1
                        }
                    });
                    countDownLatch.await();//得到上一个节点删除，阻塞
                    System.out.println(Thread.currentThread().getName()+"->成功获取锁:lock节点为:["+this.lockID+"]");
                }
                return true;
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    //释放锁
    public synchronized boolean unLock(){
        try{
            System.out.println(Thread.currentThread().getName()+"->开始释放锁，lock节点为:["+this.lockID+"]");
            //通过删除节点来释放锁
            this.zkClient.delete(this.lockID,-1);
            System.out.println(Thread.currentThread().getName()+"->["+this.lockID+"]成功删除");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
