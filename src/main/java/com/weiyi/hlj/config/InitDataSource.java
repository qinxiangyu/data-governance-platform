package com.weiyi.hlj.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiyi.hlj.entity.DataSourceEntity;
import com.weiyi.hlj.plugins.DataSourceKit;
import com.weiyi.hlj.service.DataSourceEntityService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by qinxy on 2019/6/26.
 * 项目启动后主动加载数据库中存在的数据源
 */
@Component
@Slf4j
@Order(1)
public class InitDataSource implements ApplicationRunner {

    @Autowired
    private DataSourceEntityService dataSourceEntityService;

    @Autowired
    private DataSourceKit dataSourceKit;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        queryDataSource();
    }

    private void addDataSource(IPage<DataSourceEntity> list) {
        if (list == null || list.getPages() == 0) {
            return;
        }
        list.getRecords().forEach(dataSourceEntity -> {
            dataSourceKit.addDataSource(dataSourceEntity);
        });
    }

    private void queryDataSource() {
        int page = 1;
        int pageSize = 100;
        Page<DataSourceEntity> page1 = new Page<>(page, pageSize);
        IPage<DataSourceEntity> list = dataSourceEntityService.page(page1);
        addDataSource(list);
        long totalPage = list.getPages();
        long pageNumber = list.getCurrent();
        while (pageNumber < totalPage) {
            page++;
            page1.setCurrent(page);
            list = dataSourceEntityService.page(page1);
            addDataSource(list);
            totalPage = list.getPages();
            pageNumber = list.getCurrent();
        }
    }
}
