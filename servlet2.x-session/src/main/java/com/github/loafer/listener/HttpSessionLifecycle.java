package com.github.loafer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author zhaojh
 */
public class HttpSessionLifecycle implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionLifecycle.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("HttpSession [{}] 被创建。", session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("HttpSession [{}] 已销毁。", session.getId());
    }
}
