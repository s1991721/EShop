package com.ljf.eshop.cache.service;

import com.ljf.eshop.cache.model.ProductInfo;
import com.ljf.eshop.cache.model.ShopInfo;

/**
 * Created by mr.lin on 2019/7/28
 */
public interface CacheService {

    ProductInfo saveLocalCache(ProductInfo productInfo);

    ProductInfo getLocalCache(Long id);

    ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo);

    ProductInfo getProductInfoFromLocalCache(Long productId);

    ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo);

    ShopInfo getShopInfoFromLocalCache(Long shopId);

    void saveProductInfo2RedisCache(ProductInfo productInfo);

    ProductInfo getProductInfoFromRedisCache(Long productId);

    void saveShopInfo2RedisCache(ShopInfo shopInfo);

    ShopInfo getShopInfoFromRedisCache(Long shopId);

}
