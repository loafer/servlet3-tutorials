package com.github.loafer.session.support;

import com.github.loafer.session.SessionStore;
import com.github.loafer.session.metadata.SessionMetaData;
import com.github.loafer.util.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zhaojh
 */
public class RedisSessionStore implements SessionStore {
    private static final Logger logger = LoggerFactory.getLogger(RedisSessionStore.class);
    private static final String SESSION_METADATA_KEY = "httpsession:%s:metadata";
    private static final String SESSION_ATTRIBUTE_KEY = "httpsession:%s:attribute";
    private Jedis jedis;

    @Override
    public void start() {
        jedis = new Jedis("192.168.56.110");
    }

    @Override
    public void stop() {
        jedis.quit();
    }

    @Override
    public void putSessionMetaData(HttpSession session) {
        Map<String, String> sessionAttributes = new HashMap<String, String>();
        sessionAttributes.put(AbstractDistributedHttpSession.CREATION_TIME_KEY, String.valueOf(session.getCreationTime()));
        sessionAttributes.put(AbstractDistributedHttpSession.LAST_ACCESSED_TIME_KEY, String.valueOf(session.getLastAccessedTime()));
        sessionAttributes.put(AbstractDistributedHttpSession.MAX_INACTIVE_INTERVAL_KEY, String.valueOf(session.getMaxInactiveInterval()));

        String key = String.format(SESSION_METADATA_KEY, session.getId());
        jedis.hmset(key, sessionAttributes);
    }

    @Override
    public SessionMetaData getSessionMetaData(String sessionid) {
        String key = String.format(SESSION_METADATA_KEY, sessionid);
        Map<String, String> sessionAttributes = jedis.hgetAll(key);
        if (null != sessionAttributes && !sessionAttributes.isEmpty()){
            return new SessionMetaData(
                    sessionid,
                    Long.parseLong(sessionAttributes.get("CreationTime")),
                    Long.parseLong(sessionAttributes.get("LastAccessedTime")),
                    Integer.parseInt(sessionAttributes.get("MaxInactiveInterval"))
            );
        }
        return null;
    }

    @Override
    public void removeSessionMetaData(String sessionid) {
        String metakey = String.format(SESSION_METADATA_KEY, sessionid);
        String attributekey = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        jedis.del(metakey);
        jedis.del(attributekey.getBytes());
    }

    @Override
    public void updateSessionMetaData(String sessionid, String name, Object value) {
        String metakey = String.format(SESSION_METADATA_KEY, sessionid);
        jedis.hset(metakey, name, String.valueOf(value));
    }

    @Override
    public void setAttribute(String sessionid, String name, Object value) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        jedis.hset(key.getBytes(), name.getBytes(), SerializationUtils.serialize(value));
    }

    @Override
    public void removeAttribute(String sessionid, String name) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        jedis.hdel(key.getBytes(), name.getBytes());
    }

    @Override
    public Object getAttribute(String sessionid, String name) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        return SerializationUtils.deserialize(jedis.hget(key.getBytes(), name.getBytes()));
    }

    @Override
    public List<String> getAttributeNames(String sessionid) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        Map<byte[], byte[]> attributes = jedis.hgetAll(key.getBytes());
        if(!attributes.isEmpty()){
            Iterator<byte[]> iterator = attributes.keySet().iterator();
            List<String> attributeNames = new ArrayList<String>();
            while (iterator.hasNext()){
                attributeNames.add(new String(iterator.next()));
            }

            return attributeNames;
        }

        return Collections.emptyList();
    }
}
