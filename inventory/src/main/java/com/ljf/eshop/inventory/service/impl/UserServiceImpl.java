package com.ljf.eshop.inventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ljf.eshop.inventory.dao.RedisDAO;
import com.ljf.eshop.inventory.mapper.UserMapper;
import com.ljf.eshop.inventory.model.User;
import com.ljf.eshop.inventory.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by mr.lin on 2019/7/25
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisDAO redisDAO;

    @Override
    public User findUserInfo() {
        return userMapper.findUserInfo();
    }

    @Override
    public User getCacheUserInfo() {
        redisDAO.set("cached_user_lisi", "{\"name\":\"lisi\",\"age\":28}");

        String userJson = redisDAO.get("cached_user_lisi");
        JSONObject jsonObject = JSONObject.parseObject(userJson);

        User user = new User();
        user.setName(jsonObject.getString("name"));
        user.setAge(jsonObject.getInteger("age"));

        return user;
    }

}
