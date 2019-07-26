package com.ljf.eshop.inventory.service.impl;

import com.ljf.eshop.inventory.request.Request;
import com.ljf.eshop.inventory.request.RequestQueue;
import com.ljf.eshop.inventory.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by mr.lin on 2019/7/26
 */
@Service
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {


    @Override
    public void process(Request request) {
        try {
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //根据key计算要进入的队列
    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
        RequestQueue requestQueue = RequestQueue.getInstance();

        String key = String.valueOf(productId);
        int h;
        int hash = (key == null) ? 0 : ((h = key.hashCode()) ^ h >>> 16);

        int index = hash % requestQueue.queueSize();

        System.out.println("===============================" + "进入队列" + index + "商品ID" + productId);

        return requestQueue.getQueue(index);
    }

}
