package com.weiyi.hlj.config;

import com.alibaba.fastjson.JSONObject;
import com.weiyi.hlj.common.Constants;
import com.weiyi.hlj.dto.AccountCredentials;
import com.weiyi.hlj.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by qinxy on 2019/7/2.
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //验证
        AccountCredentials user = jwtUtil.getUser(request);
        if(user != null && StringUtils.isNotBlank(user.getUsername())){
            request.setAttribute(Constants.USER_INFO,user);
            return true;
        }
        reject(response);
        return false;
    }

    /**
     * 拒绝未登录的请求
     *
     * @param response
     */
    private void reject(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            JSONObject resp = new JSONObject();
            resp.put("success", false);
            resp.put("code", HttpStatus.UNAUTHORIZED.value());
            resp.put("message", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            response.setCharacterEncoding(Constants.CHARSET_UTF_8);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(resp.toJSONString());
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
