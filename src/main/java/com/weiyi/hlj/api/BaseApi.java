package com.weiyi.hlj.api;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qinxy on 2019/6/21.
 */
public class BaseApi {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

}
