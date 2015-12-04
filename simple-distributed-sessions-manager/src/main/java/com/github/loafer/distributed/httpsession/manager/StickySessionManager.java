package com.github.loafer.distributed.httpsession.manager;

import com.github.loafer.distributed.httpsession.metadata.SessionMetaData;
import com.github.loafer.distributed.httpsession.session.DistributedHttpSession;
import com.github.loafer.distributed.httpsession.session.StickyHttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * @author zhaojh
 */
public class StickySessionManager extends AbstractSessionManager{
    private static final Logger logger = LoggerFactory.getLogger(StickySessionManager.class);

    public StickySessionManager(ServletContext servletContext, int maxInactiveInterval) {
        super(servletContext, maxInactiveInterval);
    }

    @Override
    protected HttpSession newHttpSession(String id, int maxInactiveInterval, ServletContext servletContext) {
        return new StickyHttpSession(id, maxInactiveInterval, this, servletContext);
    }

    @Override
    public HttpSession findHttpSession(String id) {
        //local
        HttpSession session = findSessionFromLocal(id);
        if(null != session){
            return session;
        }else{
            logger.info("本地没有session[{}]的信息。", id);
        }

        //storage
        SessionMetaData metaData = getSessionMetaDataFromStorage(id);
        if(null != metaData){
            logger.debug("Session Storage中持有session[{}]的信息.", id);
            session = new StickyHttpSession(
                    id,
                    metaData.getCreationTime(),
                    metaData.getLastAccessedTime(),
                    metaData.getMaxInactiveInterval(),
                    this,
                    getServletContext()
            );

            logger.info("根据Session Storage中的信息构建session[{}].", id);
            ((DistributedHttpSession)session).refresh();
        }

        logger.debug("未找到Session[{}]", id);
        return session;
    }
}
