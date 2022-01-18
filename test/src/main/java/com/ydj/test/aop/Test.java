package com.ydj.test.aop;

import com.ydj.test.aop.bean.Student;
import com.ydj.test.aop.bean.TestBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author : yidejun
 * @version : 1.0
 * @createTime : 2021/12/14 2:12 下午
 * @description :
 */
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class Test {

//    @Bean
//    public MyAspect myAspect() {
//        return new MyAspect();
//    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Test.class);
        TestBean testBean = ctx.getBean(TestBean.class);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Student ydj = Student.builder().name("ydj").age(28).build();
                        testBean.hello(ydj,"YDJ",1);
                    }
                }
        ).start();


//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        Student ydj = Student.builder().name("zs").age(200).build();
//                        testBean.hello(ydj,"ZS",2);
//                    }
//                }
//        ).start();
    }
}
