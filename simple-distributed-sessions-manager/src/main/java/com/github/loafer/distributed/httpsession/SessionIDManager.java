package com.github.loafer.distributed.httpsession;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaojh
 */
public interface SessionIDManager extends LifeCycle{
    String newSessionId(HttpServletRequest request,long created);
}
