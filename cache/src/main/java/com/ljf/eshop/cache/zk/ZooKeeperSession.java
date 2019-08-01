package com.ljf.eshop.cache.zk;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Created by mr.lin on 2019/8/1
 * <p>
 * 分布式锁
 */
public class ZooKeeperSession {

    private static class Singleton {

        private static ZooKeeperSession instance;

        static {
            instance = new ZooKeeperSession();
        }

        public static ZooKeeperSession getInstance() {
            return instance;
        }

    }

    public static ZooKeeperSession getInstance() {
        return Singleton.getInstance();
    }

    public static void init() {
        getInstance();
    }

    private ZooKeeper zooKeeper;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeperSession() {

        try {
            zooKeeper = new ZooKeeper("10.2.25.209:2181,10.2.27.144:2181,10.2.26.232:2181",
                    50000,
                    new ZooKeeperWatcher());

            System.out.println("ZooKeeperSession:" + zooKeeper.getState());

            countDownLatch.await();

            System.out.println("ZooKeeperSession:" + "建立连接");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ZooKeeperWatcher implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {

            System.out.println("ZooKeeperSession:" + "收到回调" + watchedEvent.getState());

            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                countDownLatch.countDown();
            }

        }
    }

    /**
     * 阻塞式锁
     *
     * @param productId
     */
    public void acquireDistributedLock(Long productId) {

        String path = "/product-lock-" + productId;

        try {

            zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            System.out.println("ZooKeeperSession:" + "创建节点成功");

        } catch (Exception e) {
            //阻塞式
            int count = 0;

            while (true) {
                try {
                    Thread.sleep(1000);

                    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

                } catch (Exception e1) {
                    count++;
                    System.out.println("ZooKeeperSession:" + "尝试创建节点" + count);
                    continue;
                }

                System.out.println("ZooKeeperSession:" + "尝试创建节点" + count + "次后，成功");
                break;
            }


        }

    }

    /**
     * 释放锁
     *
     * @param productId
     */
    public void releaseDistributedLock(Long productId) {

        String path = "/product-lock-" + productId;
        try {
            zooKeeper.delete(path, -1);

            System.out.println("ZooKeeperSession:" + "释放节点成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
