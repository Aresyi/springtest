package com.ydj.test.aop;

/**
 * @author : yidejun
 * @version : 1.0
 * @createTime : 2021/12/14 2:47 下午
 * @description :
 */
public class RetryException extends RuntimeException{

    private int exceptionId;

    public RetryException(String message) {
        super(message);
    }
}
