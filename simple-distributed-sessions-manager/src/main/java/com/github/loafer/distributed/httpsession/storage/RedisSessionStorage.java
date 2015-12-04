package com.github.loafer.distributed.httpsession.storage;

import com.github.loafer.distributed.httpsession.SessionStorage;
import com.github.loafer.distributed.httpsession.metadata.SessionMetaData;
import com.github.loafer.distributed.httpsession.util.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zhaojh
 */
public class RedisSessionStorage implements SessionStorage{
    private static final Logger logger = LoggerFactory.getLogger(RedisSessionStorage.class);
    private static final String SESSION_METADATA_KEY = "httpsession:%s:metadata";
    private static final String SESSION_ATTRIBUTE_KEY = "httpsession:%s:attribute";
    private Jedis jedis;

    @Override
    public void start() {
        logger.info("启动Session Storage for redis.");
        jedis = new Jedis("192.168.56.110");
    }

    @Override
    public void stop() {
        jedis.quit();
    }

    @Override
    public boolean isStored(HttpSession session) {
        String key = String.format(SESSION_METADATA_KEY, session.getId());
        return jedis.exists(key);
    }

    @Override
    public void storeSessionMetaData(HttpSession session) {
        Map<String, String> sessionAttributes = new HashMap<String, String>();
        sessionAttributes.put(SessionMetaData.CREATION_TIME_KEY, String.valueOf(session.getCreationTime()));
        sessionAttributes.put(SessionMetaData.LAST_ACCESSED_TIME_KEY, String.valueOf(session.getLastAccessedTime()));
        sessionAttributes.put(SessionMetaData.MAX_INACTIVE_INTERVAL_KEY, String.valueOf(session.getMaxInactiveInterval()));

        String key = String.format(SESSION_METADATA_KEY, session.getId());
        jedis.hmset(key, sessionAttributes);
    }

    @Override
    public void removeSession(String sessionid) {
        String metakey = String.format(SESSION_METADATA_KEY, sessionid);
        String attributekey = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        jedis.del(metakey);
        jedis.del(attributekey.getBytes());
    }

    @Override
    public SessionMetaData getSessionMetaData(String sessionid) {
        String key = String.format(SESSION_METADATA_KEY, sessionid);
        Map<String, String> sessionAttributes = jedis.hgetAll(key);
        if (null != sessionAttributes && !sessionAttributes.isEmpty()){
            return new SessionMetaData(
                    sessionid,
                    Long.parseLong(sessionAttributes.get(SessionMetaData.CREATION_TIME_KEY)),
                    Long.parseLong(sessionAttributes.get(SessionMetaData.LAST_ACCESSED_TIME_KEY)),
                    Integer.parseInt(sessionAttributes.get(SessionMetaData.MAX_INACTIVE_INTERVAL_KEY))
            );
        }
        return null;
    }

    @Override
    public void updateSessionMetaDataField(String sessionid, String name, Object value) {
        String key = String.format(SESSION_METADATA_KEY, sessionid);
        jedis.hset(key, name, String.valueOf(value));
    }

    @Override
    public Object getSessionMetaDataField(String sessionid, String name) {
        String key = String.format(SESSION_METADATA_KEY, sessionid);
        return jedis.hget(key, name);
    }

    @Override
    public void storeSessionAttribute(String sessionid, String name, Object value) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        jedis.hset(key.getBytes(), name.getBytes(), SerializationUtils.serialize(value));
    }

    @Override
    public void removeSessionAttribute(String sessionid, String name) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        jedis.hdel(key.getBytes(), name.getBytes());
    }

    @Override
    public Object getSessionAttribute(String sessionid, String name) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        return SerializationUtils.deserialize(jedis.hget(key.getBytes(), name.getBytes()));
    }

    @Override
    public List<String> getSessionAttributeNames(String sessionid) {
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

    @Override
    public Map<String, Object> getSessionAttributes(String sessionid) {
        String key = String.format(SESSION_ATTRIBUTE_KEY, sessionid);
        Map<byte[], byte[]> attributes = jedis.hgetAll(key.getBytes());
        if(!attributes.isEmpty()){
            Map<String, Object> result = new HashMap<String, Object>();
            for (Iterator<Map.Entry<byte[], byte[]>> iterator = attributes.entrySet().iterator(); iterator.hasNext();){
                Map.Entry<byte[], byte[]> entry = iterator.next();
                result.put(new String(entry.getKey()), SerializationUtils.deserialize(entry.getValue()));
            }
            return result;
        }
        return Collections.EMPTY_MAP;
    }
}
