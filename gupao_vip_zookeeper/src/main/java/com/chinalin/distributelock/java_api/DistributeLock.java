package com.chinalin.distributelock.java_api;

import org.apache.curator.utils.ZookeeperFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Id;

import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @date:2019/02/19 10:29
 * @author:艾书胜
 * 使用Java原生API来实现zookeeper的分布式共享锁
 * 这个类是zookeeper分布式共享锁的实现类
 */
public class DistributeLock {

    private static final String ROOT_LOCK="/LOCKS";//根节点,要先创建根节点

    private static final byte[] data={1,2};//节点数据

    private ZooKeeper zookeeper;//zookeeper实例

    private int sessionTimeout;//会话超时时间

    private String lockID;//记录当前获取锁的节点id(即节点路径)

    private CountDownLatch countDownLatch = new CountDownLatch(1);//计数器

    public DistributeLock() {
        this.zookeeper=Client.getInstance();//连接zookeeper
        this.sessionTimeout=Client.getSessionTimeout();//session会话过期时间
    }

    //获取锁
    public synchronized boolean lock(){


        try {
            //创建临时有序节点,这里的临时有序节点没有给名称，只有zookeeper自带的编号，类似于/LOCKS/00000001
            this.lockID=this.zookeeper.create(ROOT_LOCK+"/",data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName()+"->成功创建了临时有序节点,节点id:"+this.lockID+",开始去竞争锁");
            List<String> childNodes=this.zookeeper.getChildren(ROOT_LOCK,true);//获取根节点下的所有子节点,并使用watcher进行监听
            //对获取到的子节点按照编号从小到大进行排序
            SortedSet<String> sortedSet = new TreeSet<String>();
            for(String children:childNodes){
                sortedSet.add(ROOT_LOCK+"/"+children);
            }
            //获取所有子节点中最小的节点信息
            String mixNode=sortedSet.first();
            if(this.lockID.equals(mixNode)){//当前创建的临时有序节点是最小节点，即成功获取到锁
                System.out.println(Thread.currentThread().getName()+"->成功获取锁，lock节点为:["+this.lockID+"]");
                return true;
            }
            //否则要对当前节点的前面节点进行监控
            //获取子节点中比当前创建的临时有序节点要小的节点信息
            SortedSet<String> less=((TreeSet<String>) sortedSet).headSet(this.lockID);
            if(!less.isEmpty()){
                //获取当前创建的临时有序节点的前一个节点，因为只有监听当前节点的前一个节点，前一个节点释放锁之后当前节点就是最新节点，即可来获取锁了
                String lastNode=less.last();
                //监控比当前创建的临时节点要小的上一个节点
                this.zookeeper.exists(lastNode,new LockWatcher(countDownLatch));
                countDownLatch.await();//阻塞等待,等待时间为session会话时间
                //上面两行代码的意思是：如果连接zookeeper的session会话超时或者比当前创建的临时有序节点要小的上一个节点节点被删除了（释放锁）
                //当前创建的临时有序节点即可获取锁
                System.out.println(Thread.currentThread().getName()+"->成功获取锁:lock节点为:["+this.lockID+"]");
            }
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    //释放锁
    public synchronized boolean unLock(){
        System.out.println(Thread.currentThread().getName()+"->开始释放锁:["+this.lockID+"]");
        try {
            //通过删除创建的临时节点来释放锁
            this.zookeeper.delete(this.lockID,-1);
            System.out.println("节点["+this.lockID+"]成功删除");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    //使用多线程来模拟多进程竞争共享锁
    public static void main(String[] args){
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DistributeLock lock = new DistributeLock();
                    countDownLatch.countDown();
                    try {
                        countDownLatch.await();
                        lock.lock();//获取锁
                        Thread.sleep(random.nextInt(5000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        if(null != lock){
                            lock.unLock();//释放锁
                        }
                    }
                }
            }).start();
        }
    }
}
