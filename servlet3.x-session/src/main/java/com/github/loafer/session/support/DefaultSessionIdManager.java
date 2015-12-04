package com.github.loafer.session.support;

import com.github.loafer.session.SessionIdManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author zhaojh
 */
public class DefaultSessionIdManager implements SessionIdManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultSessionIdManager.class);

//    private final static String   __NEW_SESSION_ID  = "com.github.loafer.newSessionId";
    private Random random;
    private boolean weakRandom;
    private long reseed=100000L;

    @Override
    public void start() {
        try{
            random = new SecureRandom();
        }catch (Exception e){
            logger.warn("Could not generate SecureRandom for httpsession-id randomness", e);
            random = new Random();
            weakRandom = true;
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public String newSessionId(HttpServletRequest request, long created) {
        if(request == null){
            return newSessionId(created);
        }

        return newSessionId(request.hashCode());
    }

    private String newSessionId(long seedTerm){
        String id = null;
        while (id == null  || id.length() == 0){
            long r0 = weakRandom ? (hashCode() ^ Runtime.getRuntime().freeMemory() ^ random.nextInt() ^ (seedTerm<<32))
                    : random.nextLong();

            if(r0 < 0) r0 = -r0;

            if (reseed>0 && (r0 % reseed)== 1L){
                logger.info("Reseeding {}", this);

                if(random instanceof SecureRandom){
                    SecureRandom secure = (SecureRandom) random;
                    secure.setSeed(secure.generateSeed(8));
                }else{
                    random.setSeed(random.nextLong() ^ System.currentTimeMillis() ^ seedTerm ^ Runtime.getRuntime().freeMemory());
                }
            }

            long r1 = weakRandom ?
                    (hashCode() ^ Runtime.getRuntime().freeMemory() ^ random.nextInt() ^ (seedTerm<<32))
                    : random.nextLong();

            if(r1 < 0) r1 = -r1;

            id = Long.toString(r0, 36) + Long.toString(r1, 36);
        }

        logger.info("new httpsession id: {}", id);
        return id + ".quick4j";
    }

    public static void main(String[] args){
        final DefaultSessionIdManager manager = new DefaultSessionIdManager();
        manager.start();

        for(int i=0; i<10; i++){
            Runnable t = new Runnable() {
                @Override
                public void run() {
                    String id = manager.newSessionId(this.hashCode());
                    logger.info("id: {},  length: {}", id, id.length());
                }
            };

            t.run();
        }

        manager.stop();
    }
}
