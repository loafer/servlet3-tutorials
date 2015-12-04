package com.github.loafer.session;

import com.github.loafer.session.metadata.SessionMetaData;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zhaojh
 */
public interface SessionStore extends LifeCycle{
    void putSessionMetaData(HttpSession session);
    SessionMetaData getSessionMetaData(String sessionid);
    void removeSessionMetaData(String sessionid);
    void updateSessionMetaData(String sessionid, String name, Object value);

    void setAttribute(String sessionid, String name, Object value);
    void removeAttribute(String sessionid, String name);
    Object getAttribute(String sessionid, String name);
    List<String> getAttributeNames(String sessionid);
}
