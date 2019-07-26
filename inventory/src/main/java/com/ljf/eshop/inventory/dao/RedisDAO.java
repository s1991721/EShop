package com.ljf.eshop.inventory.dao;

/**
 * Created by mr.lin on 2019/7/25
 */
public interface RedisDAO {

    void set(String key, String value);

    String get(String key);

    void del(String key);

}
