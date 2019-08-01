package com.ljf.eshop.cache.rebuild;

import com.ljf.eshop.cache.model.ProductInfo;
import com.ljf.eshop.cache.service.CacheService;
import com.ljf.eshop.cache.spring.SpringContext;
import com.ljf.eshop.cache.zk.ZooKeeperSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mr.lin on 2019/8/1
 */
public class RebuildCacheThread implements Runnable {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
        ZooKeeperSession zooKeeperSession = ZooKeeperSession.getInstance();
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");

        while (true) {

            ProductInfo productInfo = rebuildCacheQueue.takeProductInfo();

            zooKeeperSession.acquireDistributedLock(productInfo.getId());

            ProductInfo existProductInfo = cacheService.getProductInfoFromRedisCache(productInfo.getId());//已有数据
            if (existProductInfo != null) {
                try {
                    Date existDate = sdf.parse(existProductInfo.getModifiedTime());
                    Date date = sdf.parse(productInfo.getModifiedTime());

                    if (date.before(existDate)) {
                        System.out.println("重建缓存：存在数据的时间before要更新数据的时间");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("重建缓存：存在数据的时间after要更新数据的时间");
            } else {
                System.out.println("重建缓存：缓存未存储此条数据");
            }

            cacheService.saveProductInfo2LocalCache(productInfo);
            cacheService.saveProductInfo2RedisCache(productInfo);

            zooKeeperSession.releaseDistributedLock(productInfo.getId());

        }

    }

}
