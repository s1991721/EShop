package com.ljf.eshop.inventory.thread;

import com.ljf.eshop.inventory.request.Request;
import com.ljf.eshop.inventory.request.RequestQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mr.lin on 2019/7/25
 */
public class RequestProcessorThreadPool {

    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public RequestProcessorThreadPool() {
        RequestQueue requestQueue = RequestQueue.getInstance();

        for (int i = 0; i < 10; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(100);
            requestQueue.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }

    }

    public static class Singleton {

        private static RequestProcessorThreadPool instance;

        static {
            instance = Singleton.getInstance();
        }

        public static RequestProcessorThreadPool getInstance() {
            return new RequestProcessorThreadPool();
        }

    }

    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    public static void init() {
        Singleton.getInstance();
    }

}
