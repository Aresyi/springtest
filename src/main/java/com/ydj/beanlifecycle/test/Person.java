package com.ydj.beanlifecycle.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Person implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean{

    private String name;
    
    public Person() {
        System.out.println("Person constructor");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("setName() invoked");
    }
    
    public void myInit() {
        System.out.println("myInit() invoked");
    }
    
    public void myDestroy() {
        System.out.println("myDestroy() invoked");
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("setBeanName() invoked, beanName : " + beanName);
        
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        // TODO Auto-generated method stub
        System.out.println("setBeanFactory() invoked, beanFactory : " + beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        System.out.println("setApplicationContext() invoked");
        
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet() invoked");
        
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy() invoked");
        
    }
    
    public String toString() {
        return "Person[name=" + name +"]";
    }
}