package com.github.loafer.filter;

import com.github.loafer.session.SessionManager;
import com.github.loafer.session.support.SessionManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author zhaojh
 */
public class DistributableSessionFilter implements Filter{
    private static Logger logger = LoggerFactory.getLogger(DistributableSessionFilter.class);
    private SessionManager sessionManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        sessionManager = new SessionManagerImpl(filterConfig.getServletContext());
        sessionManager.start();
    }

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpReponse = (HttpServletResponse) response;

        logger.info("servlet path: {}",((HttpServletRequest) request).getServletPath());
        logger.info("cookies: {}", ((HttpServletRequest) request).getCookies());

        HttpServletRequest newRequest = new HttpServletRequestWrapper(httpRequest){
            @Override
            public HttpSession getSession(boolean create) {
                HttpSession session = null;

                //查找与当前requset关联的session
                String sessionid = sessionManager.getRequestedSessionId(httpRequest);
                logger.info("==========================================");
                logger.info("与当前请求相关联的session为[{}]。", sessionid);
                if(sessionid != null){
                    session = sessionManager.getHttpSession(sessionid);
                    if(session != null){
                        logger.info("检索到[{}]标记的session。", sessionid);
                    }else{
                        logger.info("没有检索到[{}]标记的session。", sessionid);
                    }
                }

                //如果session不存在,并且create=true，则创建新session，否则返回null
                if(session == null && create){
                    session = sessionManager.newHttpSession(httpRequest, httpReponse);
                    logger.info("创建一个新session[{}]。", session.getId());
                }

                logger.info("==========================================");
                return session;
            }

            @Override
            public HttpSession getSession() {
                return getSession(true);
            }
        };

        chain.doFilter(newRequest, response);
    }

    @Override
    public void destroy() {
        sessionManager.stop();
    }
}
