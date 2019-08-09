package com.qinxy.plugins;

import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.qinxy.entity.DataSourceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by qinxy on 2019/6/26.
 */
@Component
public class DataSourceKit {

    private static Logger logger = LoggerFactory.getLogger(DataSourceEntity.class);

    @Autowired
    private DataSource dataSource;

    /**
     * 初始化加载数据库中的数据源
     */
    public DataSourceKit(){

    }

    public void removeDataSource(String dataSourceCode) {
        DynamicRoutingDataSource dynamicDatasource = (DynamicRoutingDataSource) dataSource;
        dynamicDatasource.removeDataSource(dataSourceCode);
    }

    public Map<String, DataSource> getCurrentDataSources() {
        DynamicRoutingDataSource dynamicDatasource = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> dataSourceMap = dynamicDatasource.getCurrentDataSources();
        dataSourceMap.keySet().forEach(key -> {
            logger.info("datasource :{}", key);
        });
        return dataSourceMap;
    }

    /**
     * 添加数据源
     *
     * @param dataSourceEntity 数据源
     */
    public void addDataSource(DataSourceEntity dataSourceEntity) {
        DataSourceProperty property = new DataSourceProperty();
        property.setUrl(dataSourceEntity.getUrl());
        property.setDriverClassName(dataSourceEntity.getDriverClassName());
        property.setUsername(dataSourceEntity.getUsername());
        property.setPassword(dataSourceEntity.getPassword());
        DynamicDataSourceCreator add = new DynamicDataSourceCreator();
        DataSource addBasicDataSource = add.createBasicDataSource(property);
        DynamicRoutingDataSource dynamicDatasource = (DynamicRoutingDataSource) dataSource;
        dynamicDatasource.addDataSource(dataSourceEntity.getCode(), addBasicDataSource);

        Map<String, DataSource> dataSources = dynamicDatasource.getCurrentDataSources();
        logger.info("目前共有 {} 个数据源", Integer.valueOf(dataSources.size()));
        dataSources.keySet().forEach(key -> {
            logger.info("datasource name :{}", key);
        });
    }
}
