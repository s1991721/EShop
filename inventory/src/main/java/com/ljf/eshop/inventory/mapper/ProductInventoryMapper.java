package com.ljf.eshop.inventory.mapper;

import com.ljf.eshop.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Param;

/**
 * Created by mr.lin on 2019/7/26
 */
public interface ProductInventoryMapper {

    void updateProductInventory(ProductInventory productInventory);

    ProductInventory findProductInventory(@Param("productId") Integer productId);

}
