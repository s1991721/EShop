package com.ljf.eshop.cache.controller;

import com.ljf.eshop.cache.model.ProductInfo;
import com.ljf.eshop.cache.model.ShopInfo;
import com.ljf.eshop.cache.service.CacheService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2019/7/28
 */
@RestController
public class CacheController {

    @Resource
    private CacheService cacheService;

    @RequestMapping("/testPutCache")
    public String testPutCache(ProductInfo productInfo) {
        cacheService.saveLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("/testGetCache")
    public ProductInfo testGetCache(Long id) {
        return cacheService.getLocalCache(id);
    }

    @RequestMapping("/getProductInfo")
    public ProductInfo getProductInfo(Long productId) {

        ProductInfo productInfo;

        productInfo = cacheService.getProductInfoFromRedisCache(productId);
        System.out.println("from redis " + productInfo);

        if (productInfo == null) {
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            System.out.println("from cache " + productInfo);
        }

        if (productInfo == null) {
            // TODO: 2019/7/31
        }

        return productInfo;
    }

    @RequestMapping("/getShopInfo")
    public ShopInfo getShopInfo(Long shopId) {

        ShopInfo shopInfo;

        shopInfo = cacheService.getShopInfoFromRedisCache(shopId);

        if (shopInfo == null) {
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
        }

        if (shopInfo == null) {
            // TODO: 2019/7/31
        }

        return shopInfo;

    }

}
