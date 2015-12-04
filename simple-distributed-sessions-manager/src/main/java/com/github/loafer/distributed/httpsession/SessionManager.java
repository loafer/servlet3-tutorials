package com.github.loafer.distributed.httpsession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zhaojh
 */
public interface SessionManager extends LifeCycle{
    String DISTRIBUTED_SESSION_ID = "JSESSIONID";

    HttpSession newHttpSession(HttpServletRequest request);
    HttpSession getHttpSession(String id);
    boolean isValid(HttpSession session);
    void removeHttpSession(HttpSession session);
    SessionStorage getSessionStorage();
}
