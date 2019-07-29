package com.ljf.eshop.cache.listener;

import com.ljf.eshop.cache.kafka.KafkaConsumer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by mr.lin on 2019/7/29
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Thread(new KafkaConsumer("cache-message")).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}