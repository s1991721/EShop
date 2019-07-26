package com.ljf.eshop.inventory.service;

import com.ljf.eshop.inventory.model.User;

/**
 * Created by mr.lin on 2019/7/25
 */
public interface UserService {

    User findUserInfo();

    User getCacheUserInfo();
}
