package com.ljf.eshop.cache.service;

import com.ljf.eshop.cache.model.ProductInfo;

/**
 * Created by mr.lin on 2019/7/28
 */
public interface CacheService {

    ProductInfo saveLocalCache(ProductInfo productInfo);

    ProductInfo getLocalCache(Long id);

}
