package com.github.loafer.session.support;

import com.github.loafer.helper.CookieHelper;
import com.github.loafer.session.SessionIdManager;
import com.github.loafer.session.SessionManager;
import com.github.loafer.session.SessionStore;
import com.github.loafer.session.metadata.SessionMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zhaojh
 */
public class SessionManagerImpl implements SessionManager {
    private final Logger logger = LoggerFactory.getLogger(SessionManagerImpl.class);

    private SessionIdManager sessionIdManager;
    private SessionStore sessionStore;
    private int maxInactiveInterval = 30;
    private ServletContext servletContext;

    public SessionManagerImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void start() {
        sessionIdManager = new DefaultSessionIdManager();
        sessionStore = new RedisSessionStore();
        sessionIdManager.start();
        sessionStore.start();
    }

    @Override
    public void stop() {
        sessionIdManager.stop();
        sessionStore.stop();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void add(HttpSession session) {
        sessionStore.putSessionMetaData(session);
    }

    @Override
    public void remove(HttpSession session) {
        sessionStore.removeSessionMetaData(session.getId());
    }

    @Override
    public HttpSession newHttpSession(HttpServletRequest request, HttpServletResponse response) {
        String id = newSessionId(request);
        HttpSession httpSession = new RedisHttpSession(id, maxInactiveInterval, this);
        CookieHelper.writeSessionIdToCookie(httpSession.getId(), request, response);
        return httpSession;
    }

    @Override
    public HttpSession getHttpSession(String id) {
        SessionMetaData metaData = sessionStore.getSessionMetaData(id);
        if(null != metaData){
             return new RedisHttpSession(
                    id,
                    metaData.getCreationTime(),
                    metaData.getLastAccessedTime(),
                    metaData.getMaxInactiveInterval(),
                    this
            );
        }

        return null;
    }

    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        String sessionid = request.getParameter(DISTRIBUTED_SESSION_ID);
        if(null == sessionid){
            sessionid = CookieHelper.findSessionId(request);
        }
        return sessionid;
    }

    @Override
    public SessionStore getSessionStore() {
        return sessionStore;
    }

    private String newSessionId(HttpServletRequest request) {
        return sessionIdManager.newSessionId(request, System.currentTimeMillis());
    }
}
