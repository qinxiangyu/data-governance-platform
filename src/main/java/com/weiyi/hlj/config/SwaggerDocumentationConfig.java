package com.weiyi.hlj.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Created by qinxy on 2019/3/25.
 */
@EnableSwagger2
@Configuration
public class SwaggerDocumentationConfig {

    @Value("${swagger.enable:false}")
    private Boolean swaggerEnable;

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("数据治理平台API")
                .description("数据治理平台接口文档")
                .license("数据治理平台接口说明文档")
                .licenseUrl("")
                .termsOfServiceUrl("")
                .contact(new Contact("data-governance-platform", null, null))
                .version("1.0.0")
                .build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .enable(swaggerEnable)
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .paths(paths())
                .build()
                .directModelSubstitute(Date.class, LocalDate.class)
                .directModelSubstitute(OffsetDateTime.class, Date.class)
                .apiInfo(apiInfo());
    }

    private Predicate<String> paths() {
        return Predicates.or(
                PathSelectors.regex("/.*")
        );
    }

}