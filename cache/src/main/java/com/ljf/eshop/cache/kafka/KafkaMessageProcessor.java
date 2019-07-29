package com.ljf.eshop.cache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.ljf.eshop.cache.model.ProductInfo;
import com.ljf.eshop.cache.model.ShopInfo;
import com.ljf.eshop.cache.service.CacheService;
import com.ljf.eshop.cache.spring.SpringContext;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * Created by mr.lin on 2019/7/29
 */
public class KafkaMessageProcessor implements Runnable {

    private KafkaStream kafkaStream;
    private CacheService cacheService;

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
            }else if ("shopInfoService".equals(serviceId)){
                processShopInfoChangeMessage(messageJSONObject);
            }

        }
    }

    public void processProductInfoChangeMessage(JSONObject messageJSONObject) {
        Long productId = messageJSONObject.getLong("productId");

        String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
        cacheService.saveProductInfo2LocalCache(productInfo);
        System.out.println(productInfo.toString());
        cacheService.saveProductInfo2RedisCache(productInfo);

    }

    private void processShopInfoChangeMessage(JSONObject messageJSONObject) {
        Long shopId = messageJSONObject.getLong("shopId");

        String shopInfoJSON = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";
        ShopInfo shopinfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);
        cacheService.saveShopInfo2LocalCache(shopinfo);
        System.out.println(shopinfo.toString());
        cacheService.saveShopInfo2RedisCache(shopinfo);
    }

}
