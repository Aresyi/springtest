package com.ydj.test.springbeanlifecycle;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * https://www.cnblogs.com/IvySue/p/6484599.html
 */
public class PersonServiceApplicationContextTest{

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        System.out.println("start init ioc container");
//        ApplicationContext aContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext aContext = new FileSystemXmlApplicationContext("D:\\project\\springtest\\qd-springtest-service\\src\\main\\java\\com\\ydj\\test\\springbeanlifecycle\\applicationContext.xml");
        System.out.println("end loading xml");
        Person person = (Person)aContext.getBean("person1");
        System.out.println(person);
        System.out.println("close container");
        ((FileSystemXmlApplicationContext)aContext).close();
    }

}