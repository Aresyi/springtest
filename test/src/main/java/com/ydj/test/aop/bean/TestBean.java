package com.ydj.test.aop.bean;

import com.ydj.test.aop.Hold;
import com.ydj.test.aop.Retry;
import com.ydj.test.aop.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("myTestBean")
@Slf4j
public class TestBean {

    @Retry(retryDes = "重试模拟测试")
    public void hello(Student student, String str, int i) {
        System.out.println(String.format("hello()->student = %s, str = %s , i = %d, flag = %s", student, str,i, Hold.get()));
        throw new RetryException("PDQ重试异常");
//        throw new IllegalArgumentException("参数异常");

    }
}