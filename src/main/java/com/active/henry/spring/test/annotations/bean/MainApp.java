package com.active.henry.spring.test.annotations.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainApp {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(HelloWorldConfig.class);
//        HelloWorld helloWorld = (HelloWorld) ac.getBean("helloWorld");
        HelloWorld helloWorld = ac.getBean(HelloWorld.class);
        helloWorld.setMessage("henry test");
        LOG.info(helloWorld.getMessage());
//        System.out.println(helloWorld.getMessage());
    }

}
