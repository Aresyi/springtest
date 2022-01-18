package com.ydj.test.aop.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author : yidejun
 * @version : 1.0
 * @createTime : 2021/12/14 9:00 下午
 * @description :
 */
@Data
@Builder
public class Param {
    private Class clazz;
    private String jsonStr;

    public Param(){}

    public Param(Class clazz, String jsonStr) {
        this.clazz = clazz;
        this.jsonStr = jsonStr;
    }
}
