package com.chinalin.distributelock.curator;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @date:2019/02/19 23:14
 * @author:艾书胜
 * 使用Curator来操作zookeeper实现分布式共享锁
 * 当前类是测试类
 */
public class Test {

    public static void main(String[] args){
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CuratorDistributeLock lock = new CuratorDistributeLock();
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
