package com.ydj.test.springbeanlifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor{

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        // TODO Auto-generated method stub
        System.out.println("postProcessAfterInitialization() invoked, beanName : " + beanName);
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        System.out.println("postProcessBeforeInitialization() invoked, beanName : " + beanName);
        return bean;
    }

}