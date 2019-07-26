package com.ljf.eshop.inventory.request;

import com.ljf.eshop.inventory.model.ProductInventory;
import com.ljf.eshop.inventory.service.ProductInventoryService;

/**
 * 查询商品库存
 * Created by mr.lin on 2019/7/26
 */
public class ProductInventoryCacheRefreshRequest implements Request {

    private Integer productId;

    private ProductInventoryService productInventoryService;

    private boolean forceRefresh;

    public ProductInventoryCacheRefreshRequest(Integer productId, ProductInventoryService productInventoryService, boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {

        //数据库查询数量
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);

        //将商品数量缓存
        productInventoryService.setProductInventoryCache(productInventory);

    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return forceRefresh;
    }

}
