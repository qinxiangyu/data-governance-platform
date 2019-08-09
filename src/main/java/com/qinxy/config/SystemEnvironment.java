package com.qinxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by qinxy on 2019/6/27.
 */
@Configuration
@PropertySource(value = "file:${server.config.home}", ignoreResourceNotFound = true)
public class SystemEnvironment {

    @Autowired
    private Environment environment;

    public String getProperty(String key){
        return environment.getProperty(key);
    }
}
