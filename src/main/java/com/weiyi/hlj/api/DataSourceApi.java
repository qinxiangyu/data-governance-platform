package com.weiyi.hlj.api;

import com.weiyi.hlj.entity.DataSourceEntity;
import com.weiyi.hlj.plugins.DataSourceKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qinxy on 2019/6/25.
 * 动态增减数据源
 */
@RestController
@RequestMapping(value = "/dataSource")
public class DataSourceApi extends BaseApi{

    @Autowired
    private DataSourceKit dataSourceKit;

    @PostMapping("/addDs")
    public String add(){
        DataSourceEntity property = new DataSourceEntity();
        property.setUrl("jdbc:mysql://localhost:3306/user_center_store");
        property.setDriverClassName("com.mysql.jdbc.Driver");
        property.setUsername("root");
        property.setPassword("root");
        property.setCode("slave");
        dataSourceKit.addDataSource(property);
        return "success";

    }

    @PostMapping("/removeDs")
    public String remove(String ds){
        dataSourceKit.removeDataSource(ds);
        return "success";
    }
}
