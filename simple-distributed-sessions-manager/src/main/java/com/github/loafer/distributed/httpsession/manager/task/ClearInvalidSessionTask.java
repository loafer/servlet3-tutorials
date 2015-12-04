package com.github.loafer.distributed.httpsession.manager.task;

import com.github.loafer.distributed.httpsession.session.DistributedHttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaojh
 */
public class ClearInvalidSessionTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ClearInvalidSessionTask.class);
    private static final int SLEEP_TIME = 10;
    private HttpSession session;

    public ClearInvalidSessionTask(HttpSession session) {
        this.session = session;
    }

    @Override
    public void run() {
        while (true){
            try {
                if(((DistributedHttpSession)session).isValid()){
                    session.invalidate();
                    break;
                }
                TimeUnit.SECONDS.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
