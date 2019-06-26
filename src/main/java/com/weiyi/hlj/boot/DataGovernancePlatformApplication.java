package com.weiyi.hlj.boot;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan(basePackages = {"com.weiyi.hlj.mapper"})
@SpringBootApplication(scanBasePackages = {"com.weiyi.hlj"} ,exclude = {DruidDataSourceAutoConfigure.class})
@Configurable
@ServletComponentScan
public class DataGovernancePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataGovernancePlatformApplication.class, args);

	}





}
