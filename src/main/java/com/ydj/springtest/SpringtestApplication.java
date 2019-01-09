package com.ydj.springtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//这个注解  会全package的扫描
@SpringBootApplication
public class SpringtestApplication {

	public static void main(String[] args) {
		System.out.println("init");
		SpringApplication.run(SpringtestApplication.class, args);
	}
}
