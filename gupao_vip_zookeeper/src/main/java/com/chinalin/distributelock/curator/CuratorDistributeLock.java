package com.chinalin.distributelock.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

/**
 * @date:2019/02/19 22:25
 * @author:艾书胜
 * 使用curator来操作zookeeper实现分布式共享锁
 * 当前类是zookeeper实现分布式共享锁的实现类
 */
public class CuratorDistributeLock {

    private static final String ROOT_LOCKS="/LOCKS";//根节点

    private static final byte[] data={1,2};//临时有序节点数据

    private CuratorFramework curatorFramework;//curator连接zookeeper的实例

    private String lockId;//记录当前获取锁的节点路径

    private CountDownLatch countDownLatch = new CountDownLatch(1);//计数器

    public CuratorDistributeLock() {
        this.curatorFramework = CuratorClient.getInstance();
    }

    //获取锁
    public synchronized boolean lock(){

        try {
            //创建临时有序节点
           this.lockId = this.curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(ROOT_LOCKS+"/",data);

            System.out.println(Thread.currentThread().getName()+"->成功创建了临时有序节点,节点id:["+this.lockId+"],开始去竞争锁");
            List<String> childNodes=this.curatorFramework.getChildren().forPath(ROOT_LOCKS);//获取子节点
            //对获取到的子节点按照编号从小到大进行排序
            SortedSet<String> sortedSet = new TreeSet<String>();
            if(null != childNodes && !childNodes.isEmpty()){
                for(String children:childNodes){
                    sortedSet.add(ROOT_LOCKS+"/"+children);
                }
            }
            //获取所有子节点中最小的节点信息
            String mixNode = null;
            if (null != sortedSet && !sortedSet.isEmpty()){
                mixNode=sortedSet.first();
            }else{
                mixNode=this.lockId;
            }
            if(this.lockId.equals(mixNode)){//当前创建的临时有序节点是最小节点，即成功获取到锁
                System.out.println(Thread.currentThread().getName()+"->成功获取锁，lock节点为:["+this.lockId+"]");
                return true;
            }else{
                //否则要对当前节点的前面节点进行监控
                //获取子节点中比当前创建的临时有序节点要小的节点信息
                SortedSet<String> less = ((TreeSet<String>) sortedSet).headSet(this.lockId);
                if(null != less && !less.isEmpty()){
                    //获取当前创建的临时有序节点的前一个节点，因为只有监听当前节点的前一个节点，前一个节点释放锁之后当前节点就是最新节点，即可来获取锁了
                    String lastNode=less.last();
                    //监控比当前创建的临时节点要小的上一个节点
                    PathChildrenCache pathChildrenCache = new PathChildrenCache(this.curatorFramework,ROOT_LOCKS,true);
                    pathChildrenCache.start();
                    pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                        @Override
                        public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                            if(pathChildrenCacheEvent.getType()==PathChildrenCacheEvent.Type.CHILD_REMOVED){//监听的是子节点删除事件通知
                                String childPath=pathChildrenCacheEvent.getData().getPath();
                                if(lastNode.equals(childPath)){//且删除的子节点就是比当前创建的临时有序节点小的上一个节点
                                    countDownLatch.countDown();
                                }
                            }
                        }
                    });
                    countDownLatch.await();
                    //nodeCache.close();
                    //当前创建的临时有序节点即可获取锁
                    System.out.println(Thread.currentThread().getName()+"->成功获取锁:lock节点为:["+this.lockId+"]");
                }
                return true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //释放锁
    public synchronized boolean unLock(){
        try {
            System.out.println(Thread.currentThread().getName()+"->开始释放锁:["+this.lockId+"]");
            //通过删除创建的临时有序节点来释放锁
            this.curatorFramework.delete().forPath(this.lockId);
            System.out.println("节点["+this.lockId+"]成功删除");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
