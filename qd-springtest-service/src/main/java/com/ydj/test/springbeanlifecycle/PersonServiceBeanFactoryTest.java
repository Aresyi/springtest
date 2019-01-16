package com.ydj.test.springbeanlifecycle;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class PersonServiceBeanFactoryTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        System.out.println("start init ioc container");
        ConfigurableListableBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
        System.out.println("end loading xml");
        beanFactory.addBeanPostProcessor(new MyBeanPostProcessor());
        Person person = (Person)beanFactory.getBean("person1");
        System.out.println(person);
        System.out.println("close container");
        beanFactory.destroySingletons();
    }
}