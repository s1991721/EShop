package com.ljf.eshop.cache.spring;

import org.springframework.context.ApplicationContext;

/**
 * Created by mr.lin on 2019/7/29
 */
public class SpringContext {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }
}
