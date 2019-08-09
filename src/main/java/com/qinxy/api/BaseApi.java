package com.qinxy.api;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qinxy on 2019/6/21.
 */
public class BaseApi {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

    @Autowired
    protected HttpServletRequest request;

}
