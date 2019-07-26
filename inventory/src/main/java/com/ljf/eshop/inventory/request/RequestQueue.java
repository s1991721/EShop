package com.ljf.eshop.inventory.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求队列列表
 * Created by mr.lin on 2019/7/25
 */
public class RequestQueue {

    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    private Map<Integer,Boolean> flagMap=new ConcurrentHashMap<>();

    public static class Singleton {

        private static RequestQueue instance;

        static {
            instance = getInstance();
        }

        public static RequestQueue getInstance() {
            return instance;
        }

    }

    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }

    //队列加入列表
    public void addQueue(ArrayBlockingQueue<Request> queue) {
        queues.add(queue);
    }

    //获取相应的队列
    public ArrayBlockingQueue<Request> getQueue(int index) {
        return queues.get(index);
    }

    //当前队列的数量
    public int queueSize() {
        return queues.size();
    }

    public Map<Integer, Boolean> getFlagMap() {
        return flagMap;
    }
}
