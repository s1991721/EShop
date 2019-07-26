package com.ljf.eshop.inventory.request;

import com.ljf.eshop.inventory.model.ProductInventory;
import com.ljf.eshop.inventory.service.ProductInventoryService;

/**
 * 更新库存
 * Created by mr.lin on 2019/7/26
 */
public class ProductInventoryDBUpdateRequest implements Request {

    private ProductInventory productInventory;

    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        System.out.println("===============================" + "删除缓存" + productInventory);
        //删除缓存
        productInventoryService.removeProductInventoryCache(productInventory);

        System.out.println("===============================" + "数据库更新请求" + productInventory);
        //更新数据库
        productInventoryService.updateProductInventory(productInventory);

    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }

}
