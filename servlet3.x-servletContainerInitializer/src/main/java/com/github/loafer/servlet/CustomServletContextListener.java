package com.github.loafer.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

/**
 * 动态注册servlet
 * 注意：
 *  1、只会在容器启动时加载一次
 *
 * @author zhaojh
 */
public class CustomServletContextListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        ServletRegistration.Dynamic dynamic = context.addServlet("greeting", GreetingServlet.class);
        dynamic.addMapping("/greeting");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
