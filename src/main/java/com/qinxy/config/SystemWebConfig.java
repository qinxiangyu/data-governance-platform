package com.qinxy.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.qinxy.common.Constants;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by qinxy on 2019/3/25.
 * 配置
 */
@Configurable
@Component
public class SystemWebConfig extends WebMvcConfigurationSupport {

    @Autowired
    private LoggerInterceptor loggerInterceptor;

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor);
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/*")
                .excludePathPatterns("/login",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/swagger-resources/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "OPTIONS").allowCredentials(true)
                .maxAge(3600);
        super.addCorsMappings(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/resources/static/", "classpath:/static/");
        super.addResourceHandlers(registry);
    }

    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

    /*
    解决中文乱码问题
     */
    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory
                simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(30000);
        simpleClientHttpRequestFactory.setReadTimeout(30000);
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName(Constants.CHARSET_UTF_8)));
        return restTemplate;
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 创建定时任务工厂实例
     */

    @Bean
    public SchedulerFactory schedulerFactory() {
        return new StdSchedulerFactory();
    }

    /*
     * 创建调度器
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler() throws SchedulerException {
        return schedulerFactory().getScheduler();
    }

}
