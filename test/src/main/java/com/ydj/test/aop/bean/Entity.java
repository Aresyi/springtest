package com.ydj.test.aop.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Entity implements Serializable {
    private Class clazz;
    private String methodName;
    private List<Param> paramList;

    public Entity(){}

    public Entity(Class clazz, String methodName, List<Param> paramList) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.paramList = paramList;
    }
}
