package com.gome.test.gtp.service.executor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringContextHelper implements ApplicationContextAware {
    private ApplicationContext context;

    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return context.getBeansOfType(type);
    }

    public <T> Object getBean(Class<T> type) {
        return context.getBean(type);
    }


    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;

    }
}
