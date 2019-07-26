package com.ljf.eshop.inventory.service;

import com.ljf.eshop.inventory.model.ProductInventory;

/**
 * Created by mr.lin on 2019/7/26
 */
public interface ProductInventoryService {

    /**
     * 更新商品库存
     *
     * @param productInventory
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除redis中的缓存
     *
     * @param productInventory
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商品ID查询库存
     *
     * @param productId
     * @return
     */
    ProductInventory findProductInventory(Integer productId);

    /**
     * 设置商品库存缓存
     *
     * @param productInventory
     */
    void setProductInventoryCache(ProductInventory productInventory);

    /**
     * 获取商品库存缓存
     *
     * @return
     */
    ProductInventory getProductInventoryCache(Integer productId);

}
