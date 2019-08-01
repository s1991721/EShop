package com.ljf.eshop.cache.listener;

import com.ljf.eshop.cache.kafka.KafkaConsumer;
import com.ljf.eshop.cache.rebuild.RebuildCacheThread;
import com.ljf.eshop.cache.spring.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by mr.lin on 2019/7/29
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SpringContext.setApplicationContext(applicationContext);

        new Thread(new KafkaConsumer("cache-message")).start();
        new Thread(new RebuildCacheThread()).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}