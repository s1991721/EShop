package com.ljf.eshop.inventory.request;

/**
 * Created by mr.lin on 2019/7/25
 */
public interface Request{

    void process();

    Integer getProductId();

    boolean isForceRefresh();

}
