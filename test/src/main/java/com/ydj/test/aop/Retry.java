package com.ydj.test.aop;

import java.lang.annotation.*;

/**
 * @author : yidejun
 * @version : 1.0
 * @createTime : 2021/12/14 2:38 下午
 * @description :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Retry {

    /**
     * 重试任务说明描述
     * @return
     */
    String retryDes() default "PDQ重试";

    /**
     * 重试次数设置
     * @return
     */
    int retryNum() default 3;

    /**
     * 所支持的重试异常类型
     * @return
     */
    Class<? extends Throwable>[] supportTypes() default {RetryException.class};
}
