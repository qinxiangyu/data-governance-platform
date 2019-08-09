package com.qinxy.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Created by qinxy on 2019/4/1.
 * 统一的打印请求信息
 */
@Component
@WebFilter(urlPatterns = "/*",filterName = "commonFilter")
public class CommonFilter implements Filter {

    public static String REQ_KEY = "REQ_KEY";

    public static String RESP_KEY = "X-REQUEST-ID";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommonFilter(){
        logger.info("init-filter:{}", this.getClass().getName());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("FilterConfig:{}",filterConfig == null?"null":filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uuid = UUID.randomUUID().toString();
        MDC.put(REQ_KEY, uuid);
        logger.info("create new request:" + uuid);
        printHeaders((HttpServletRequest)request);
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        httpServletResponse.addHeader(RESP_KEY, uuid);
        chain.doFilter(request, httpServletResponse);
        MDC.remove(REQ_KEY);
    }

    private void printHeaders(HttpServletRequest request){
        logger.info("request-url:" + request.getRequestURI());
        logger.info("request-method:" + request.getMethod());
        JSONObject headers = new JSONObject();
        Enumeration<String> moreHeaders = request.getHeaderNames();
        while (moreHeaders.hasMoreElements()){
            String name = moreHeaders.nextElement();
            headers.put(name,request.getHeader(name));
        }
        logger.info("request-header:{}", headers.toString());
    }

    @Override
    public void destroy() {
        logger.info("request destroy for request-id:{}", this.getClass().getName());
    }
}
