package com.qinxy.entity;

import lombok.Data;

/**
 * Created by qinxy on 2019/6/25.
 */
@Data
public class DataSourceEntity extends BaseEntity {
    /**
     * 数据库地址
     */
    private String url;
    /**
     * 用户名
     */
      private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据源编码，保证唯一
     */
    private String code;

    /**
     * 数据库类型，支持oracle、mysql、sqlserver2000、sqlserver
     */
    private String driverClassName;

}
