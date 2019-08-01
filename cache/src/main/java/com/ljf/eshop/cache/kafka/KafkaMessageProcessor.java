package com.ljf.eshop.cache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.ljf.eshop.cache.model.ProductInfo;
import com.ljf.eshop.cache.model.ShopInfo;
import com.ljf.eshop.cache.service.CacheService;
import com.ljf.eshop.cache.spring.SpringContext;
import com.ljf.eshop.cache.zk.ZooKeeperSession;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mr.lin on 2019/7/29
 */
public class KafkaMessageProcessor implements Runnable {

    private KafkaStream kafkaStream;
    private CacheService cacheService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public KafkaMessageProcessor(KafkaStream kafkaStream) {
        this.kafkaStream = kafkaStream;
        this.cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();

        while (iterator.hasNext()) {
            String message = new String(iterator.next().message());

            System.out.println(message);
            JSONObject messageJSONObject = JSONObject.parseObject(message);

            String serviceId = messageJSONObject.getString("serviceId");

            if ("productInfoService".equals(serviceId)) {
                processProductInfoChangeMessage(messageJSONObject);
            } else if ("shopInfoService".equals(serviceId)) {
                processShopInfoChangeMessage(messageJSONObject);
            }

        }
    }

    private void processProductInfoChangeMessage(JSONObject messageJSONObject) {
        Long productId = messageJSONObject.getLong("productId");

        //模拟从服务获取到数据
        String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1,\"modifiedTime\":\"2017-01-01 12:00:00\"}";

        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);

        //上锁
        ZooKeeperSession.getInstance().acquireDistributedLock(productId);

        ProductInfo existProductInfo = cacheService.getProductInfoFromRedisCache(productId);//已有数据
        if (existProductInfo != null) {
            try {
                Date existDate = sdf.parse(existProductInfo.getModifiedTime());
                Date date = sdf.parse(productInfo.getModifiedTime());

                if (date.before(existDate)) {
                    System.out.println("存在数据的时间before要更新数据的时间");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println("存在数据的时间after要更新数据的时间");
        } else {
            System.out.println("缓存未存储此条数据");
        }

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cacheService.saveProductInfo2LocalCache(productInfo);
        cacheService.saveProductInfo2RedisCache(productInfo);

        //释放锁
        ZooKeeperSession.getInstance().releaseDistributedLock(productId);

    }

    private void processShopInfoChangeMessage(JSONObject messageJSONObject) {
        Long shopId = messageJSONObject.getLong("shopId");

        //模拟从服务获取到数据
        String shopInfoJSON = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";

        ShopInfo shopinfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);
        cacheService.saveShopInfo2LocalCache(shopinfo);
        System.out.println(shopinfo.toString());
        cacheService.saveShopInfo2RedisCache(shopinfo);
    }

}
