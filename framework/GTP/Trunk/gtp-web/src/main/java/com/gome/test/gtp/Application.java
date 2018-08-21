package com.gome.test.gtp;


import com.gome.test.gtp.filter.AdminFilter;
import com.gome.test.gtp.filter.ReLoginFilter;
import com.gome.test.gtp.filter.SimpleFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class Application extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public FilterRegistrationBean regReLoginFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setName("relogin");
        filterRegBean.setFilter(new ReLoginFilter());
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/login");
        filterRegBean.setUrlPatterns(urlPatterns);
        filterRegBean.setOrder(1);
        return filterRegBean;
    }

    @Bean
    public FilterRegistrationBean regSimpleFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setName("simple");
        filterRegBean.setFilter(new SimpleFilter());
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/task/*");
        urlPatterns.add("/branch/*");
        urlPatterns.add("/hosts/*");
        urlPatterns.add("/machine/*");
        urlPatterns.add("/regularRule/*");
        urlPatterns.add("/gui-pageTest/*");
        urlPatterns.add("/suggestion/*");
        urlPatterns.add("/load/*");
        urlPatterns.add("/gtp-report/*");
        filterRegBean.setUrlPatterns(urlPatterns);
        filterRegBean.setOrder(2);
        return filterRegBean;
    }

    @Bean
    public FilterRegistrationBean regAdminFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setName("admin");
        filterRegBean.setFilter(new AdminFilter());
        List<String> urlPatterns = new ArrayList<String>();
        //只有管理员可访问的路径
        urlPatterns.add("/account/*");
        urlPatterns.add("/machine/edit/*");
        urlPatterns.add("/machine/delete/*");
        urlPatterns.add("/machine/new/*");
        urlPatterns.add("/signup");
        urlPatterns.add("/signup/submit");
        filterRegBean.setUrlPatterns(urlPatterns);
        filterRegBean.setOrder(3);
        return filterRegBean;
    }
}
