package com.gome.test.gtp;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class Application extends SpringBootServletInitializer {
    final static Logger logger = org.slf4j.LoggerFactory.getLogger(Application.class);
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }


    public static void main(String[] args) throws Exception {
        logger.info("=======GTP server launching .==========");
        final ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        System.setProperty("log4j.configuration", "log4j.properties");
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getResourceAsStream("/quartz.properties");
        properties.load(inputStream);
        schedulerFactoryBean.setQuartzProperties(properties);

        return schedulerFactoryBean;
    }
}
