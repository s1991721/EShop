package com.ljf.eshop.cache.service.impl;

import com.ljf.eshop.cache.model.ProductInfo;
import com.ljf.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by mr.lin on 2019/7/28
 */
@Service
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

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

}
