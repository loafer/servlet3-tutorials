package com.github.loafer.session;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaojh
 */
public interface SessionIdManager extends LifeCycle{
    String newSessionId(HttpServletRequest request,long created);
}
