package com.ljf.eshop.inventory.thread;

import com.ljf.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.ljf.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.ljf.eshop.inventory.request.Request;
import com.ljf.eshop.inventory.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * Created by mr.lin on 2019/7/25
 */
public class RequestProcessorThread implements Callable<Boolean> {

    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {

        try {

            while (true) {
                Request request = queue.take();
                boolean forceRefresh = request.isForceRefresh();

                if (!forceRefresh) {//非强制执行
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

                    if (request instanceof ProductInventoryDBUpdateRequest) {
                        flagMap.put(request.getProductId(), true);//刷新数据库，必须执行
                    } else if (request instanceof ProductInventoryCacheRefreshRequest) {
                        Boolean flag = flagMap.get(request.getProductId());

                        if (flag == null) {//只是查询操作
                            flagMap.put(request.getProductId(), false);
                        }

                        if (flag != null && flag) {//之前存在刷新数据库操作
                            flagMap.put(request.getProductId(), false);
                        }

                        if (flag != null && !flag) {//查询操作之前已经有了
                            return true;
                        }

                    }
                }
                System.out.println("==============================="+"请求处理");
                request.process();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
