package com.github.loafer.session.support;

import com.github.loafer.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaojh
 */
public class RedisHttpSession extends AbstractDistributedHttpSession {
    private static final Logger logger = LoggerFactory.getLogger(RedisHttpSession.class);
    public RedisHttpSession(String id,
                            long creationTm,
                            long lastAccessedTm,
                            int maxInactiveInterval,
                            SessionManager sessionManager) {
        super(id, creationTm, lastAccessedTm, maxInactiveInterval, sessionManager);
    }

    public RedisHttpSession(String id, int maxInactiveInterval, SessionManager sessionManager) {
        super(id, maxInactiveInterval, sessionManager);
    }

    @Override
    public Object getAttribute(String name) {
        access(false);
        return sessionStore.getAttribute(getId(), name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        access(false);
        return Collections.enumeration(sessionStore.getAttributeNames(getId()));
    }

    @Override
    public void setAttribute(String name, Object value) {
        access(true);

        Object oldValue = sessionStore.getAttribute(getId(), name);
        if(value instanceof HttpSessionBindingListener){
            if(value != oldValue){
                try{
                    ((HttpSessionBindingListener)value).valueBound(new HttpSessionBindingEvent(this, name, value));
                }catch (Exception e){
                    logger.error("bingEvent error:", e);
                }
            }
        }

        sessionStore.setAttribute(getId(), name, value);

        if(oldValue != null && oldValue != value &&
                oldValue instanceof HttpSessionBindingListener){
            try{
                ((HttpSessionBindingListener)oldValue).valueUnbound(new HttpSessionBindingEvent(this, name));
            }catch (Exception e){
                logger.error("bingEvent error:", e);
            }
        }
    }

    @Override
    public void removeAttribute(String name) {
        access(false);
        sessionStore.removeAttribute(getId(), name);
    }
}
