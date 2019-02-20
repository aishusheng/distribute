package com.chinalin.distributelock.java_api;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @date:2019/02/19 11:13
 * @author:艾书胜
 * 使用Java原生API来实现zookeeper的分布式共享锁
 * 当前类是使用watcher机制进行监控的类
 */
public class LockWatcher implements Watcher {

    private CountDownLatch countDownLatch;//计数器

    public LockWatcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType()== Event.EventType.NodeDeleted){//监控是否是节点删除，临时节点端口连接之后会自动删除
            countDownLatch.countDown();//计数器减1
        }
    }
}
