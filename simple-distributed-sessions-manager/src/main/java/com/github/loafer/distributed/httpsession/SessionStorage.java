package com.github.loafer.distributed.httpsession;

import com.github.loafer.distributed.httpsession.metadata.SessionMetaData;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author zhaojh
 */
public interface SessionStorage extends LifeCycle{
    boolean isStored(HttpSession session);
    void storeSessionMetaData(HttpSession session);
    SessionMetaData getSessionMetaData(String sessionid);
    void updateSessionMetaDataField(String sessionid, String name, Object value);
    Object getSessionMetaDataField(String sessionid, String name);
    void removeSession(String sessionid);

    void storeSessionAttribute(String sessionid, String name, Object value);
    void removeSessionAttribute(String sessionid, String name);
    Object getSessionAttribute(String sessionid, String name);
    List<String> getSessionAttributeNames(String sessionid);
    Map<String, Object> getSessionAttributes(String sessionid);
}
