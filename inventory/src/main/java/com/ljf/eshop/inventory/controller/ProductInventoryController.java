package com.ljf.eshop.inventory.controller;

import com.ljf.eshop.inventory.model.ProductInventory;
import com.ljf.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.ljf.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.ljf.eshop.inventory.service.ProductInventoryService;
import com.ljf.eshop.inventory.service.RequestAsyncProcessService;
import com.ljf.eshop.inventory.vo.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2019/7/26
 */
@RestController
public class ProductInventoryController {

    @Resource
    private RequestAsyncProcessService requestAsyncProcessService;

    @Resource
    private ProductInventoryService productInventoryService;

    @RequestMapping("/updateProductInventory")
    public Response updateProductInventory(ProductInventory productInventory) {
        Response response;

        try {
            System.out.println("===============================" + "更新商品库存" + productInventory);
            requestAsyncProcessService.process(new ProductInventoryDBUpdateRequest(productInventory, productInventoryService));
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(Response.FAILURE);
        }
        return response;
    }

    @RequestMapping("/getProductInventory")
    public ProductInventory getProductInventory(Integer productId) {

        ProductInventory productInventory;

        try {

            requestAsyncProcessService.process(new ProductInventoryCacheRefreshRequest(productId, productInventoryService, false));

            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;

            while (true) {

                if (waitTime > 200) {
                    break;
                }

                productInventory = productInventoryService.getProductInventoryCache(productId);

                if (productInventory != null) {
                    return productInventory;
                } else {
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }

            }

            productInventory = productInventoryService.findProductInventory(productId);

            if (productInventory != null) {
                requestAsyncProcessService.process(new ProductInventoryCacheRefreshRequest(productId, productInventoryService, true));
                return productInventory;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ProductInventory(productId, -1L);
    }

}
