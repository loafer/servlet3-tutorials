package com.github.loafer.session.support;

import com.github.loafer.session.SessionManager;
import com.github.loafer.session.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Collections;

/**
 * @author zhaojh
 */
public abstract class AbstractDistributedHttpSession implements HttpSession{
    private static final Logger logger = LoggerFactory.getLogger(AbstractDistributedHttpSession.class);
    public static final String LAST_ACCESSED_TIME_KEY = "lastAccessedTime";
    public static final String CREATION_TIME_KEY = "creationTime";
    public static final String MAX_INACTIVE_INTERVAL_KEY = "maxInactiveInterval";
    private String id;
    private final long creationTm;
    private long lastAccessedTm;
    private int maxInactiveInterval;
    private boolean newSession;
    private boolean persistent;
    protected SessionManager sessionManager;
    protected SessionStore sessionStore;

    /**
     * 创建一个新session
     * @param id
     * @param maxInactiveInterval
     * @param sessionManager
     */
    public AbstractDistributedHttpSession(String id,
                                          int maxInactiveInterval,
                                          SessionManager sessionManager) {
        this.id = id;
        this.creationTm = System.currentTimeMillis();
        this.lastAccessedTm = this.creationTm;
        this.maxInactiveInterval = maxInactiveInterval;
        this.newSession = true;
        this.persistent = false;
        this.sessionManager = sessionManager;
        this.sessionStore = this.sessionManager.getSessionStore();
    }

    /**
     * 根据sessionMetaData还原一个已存在的session
     * @param id
     * @param creationTm
     * @param lastAccessedTm
     * @param maxInactiveInterval
     * @param sessionManager
     */
    public AbstractDistributedHttpSession(String id,
                                          long creationTm,
                                          long lastAccessedTm,
                                          int maxInactiveInterval,
                                          SessionManager sessionManager) {
        this.id = id;
        this.creationTm = creationTm;
        this.lastAccessedTm = lastAccessedTm;
        this.maxInactiveInterval = maxInactiveInterval;
        this.newSession = false;
        this.persistent = true;
        this.sessionManager = sessionManager;
        this.sessionStore = this.sessionManager.getSessionStore();
    }

//    @Override
//    public Object getAttribute(String name) {
//        return null;
//    }

//    @Override
//    public Enumeration<String> getAttributeNames() {
//        return null;
//    }

    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTm;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public String[] getValueNames() {
        return Collections.list(getAttributeNames()).toArray(new String[]{});
    }

    @Override
    public void invalidate() {
        sessionManager.remove(this);
    }

    @Override
    public boolean isNew() {
        return newSession;
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

//    @Override
//    public void removeAttribute(String name) {
//
//    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

//    @Override
//    public void setAttribute(String name, Object value) {
//
//    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        maxInactiveInterval = interval;
    }

    public void access(boolean joined){
        if(newSession && joined){
            logger.info("保存[{}]的元数据。", getId());
            sessionManager.add(this);
            newSession = false;
            persistent = true;
        }
        updateLastAccessedTime();
    }

    private void updateLastAccessedTime(){
        lastAccessedTm = System.currentTimeMillis();
        if(persistent){
            sessionStore.updateSessionMetaData(getId(), LAST_ACCESSED_TIME_KEY, lastAccessedTm);
        }
    }
}
