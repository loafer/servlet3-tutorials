package com.github.loafer.session.metadata;

import java.io.Serializable;

/**
 * @author zhaojh
 */
public class SessionMetaData implements Serializable{
    private String id;
    private long creationTime;
    private long lastAccessedTime;
    private int maxInactiveInterval;

    public SessionMetaData(String id, long creationTime, long lastAccessedTime, int maxInactiveInterval) {
        this.id = id;
        this.creationTime = creationTime;
        this.lastAccessedTime = lastAccessedTime;
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public String getId() {
        return id;
    }

    public long getCreationTime() {
        return creationTime;
    }


    public long getLastAccessedTime() {
        return lastAccessedTime;
    }


    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

}
