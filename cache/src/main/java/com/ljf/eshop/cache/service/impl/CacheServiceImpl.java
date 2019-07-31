package com.ljf.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ljf.eshop.cache.model.ProductInfo;
import com.ljf.eshop.cache.model.ShopInfo;
import com.ljf.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2019/7/28
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    @Resource
    private JedisCluster jedisCluster;

    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    @Override
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    @Override
    public ProductInfo getLocalCache(Long id) {
        return null;
    }

    @CachePut(value = CACHE_NAME, key = "'product_info_' + #productInfo.getId()")
    @Override
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Cacheable(value = CACHE_NAME, key = "'product_info_' + #productId")
    @Override
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    @Override
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return null;
    }

    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    @Override
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    @Override
    public void saveProductInfo2RedisCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(productInfo));
    }

    @Override
    public ProductInfo getProductInfoFromRedisCache(Long productId) {
        String key = "product_info_" + productId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ProductInfo.class);
    }

    @Override
    public void saveShopInfo2RedisCache(ShopInfo shopInfo) {
        String key = "product_info_" + shopInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));
    }

    @Override
    public ShopInfo getShopInfoFromRedisCache(Long shopId) {
        String key = "product_info_" + shopId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ShopInfo.class);
    }

}
