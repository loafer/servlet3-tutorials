package com.github.loafer.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * 动态注册servlet
 * 注意：
 *  1、在META-INF/services/中放入javax.servlet.ServletContainerInitializer文件
 *  2、只会在容器启动时加载一次
 * @author zhaojh
 */
public class WebApplicationInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("greeting", GreetingServlet.class);
        dynamic.addMapping("/greeting");
    }
}
