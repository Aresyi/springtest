package com.ydj.test.aop.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student{
    private String name;
    private int age;

    public Student(){}

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}