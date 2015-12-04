package com.github.loafer.session;

import org.slf4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zhaojh
 */
public interface SessionManager extends LifeCycle{
    String DISTRIBUTED_SESSION_ID = "JDSESSIONID";

    void add(HttpSession session);
    void remove(HttpSession session);
    void setMaxInactiveInterval(int interval);
    HttpSession newHttpSession(HttpServletRequest request, HttpServletResponse response);
    HttpSession getHttpSession(String id);
    ServletContext getServletContext();
    String getRequestedSessionId(HttpServletRequest request);
    SessionStore getSessionStore();
}
