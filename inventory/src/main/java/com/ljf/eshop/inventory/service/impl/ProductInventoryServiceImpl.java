package com.ljf.eshop.inventory.service.impl;

import com.ljf.eshop.inventory.dao.RedisDAO;
import com.ljf.eshop.inventory.mapper.ProductInventoryMapper;
import com.ljf.eshop.inventory.model.ProductInventory;
import com.ljf.eshop.inventory.service.ProductInventoryService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2019/7/26
 */
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Resource
    private ProductInventoryMapper productInventoryMapper;

    @Resource
    private RedisDAO redisDAO;

    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.del(key);
    }

    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDAO.set(key, String.valueOf(productInventory.getProductId()));
    }

    @Override
    public ProductInventory getProductInventoryCache(Integer productId) {
        String key = "product:inventory:" + productId;
        String result = redisDAO.get(key);

        Long inventory;
        if (!StringUtils.isEmpty(result)) {
            inventory = Long.parseLong(redisDAO.get(key));
            return new ProductInventory(productId, inventory);
        }

        return null;
    }
}
