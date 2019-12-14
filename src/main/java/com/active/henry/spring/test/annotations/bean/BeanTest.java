package com.active.henry.spring.test.annotations.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class BeanTest {
    @Bean
    public   BeanTest  getBean(){
        BeanTest bean = new  BeanTest();
        System.out.println("调用方法："+bean);
        return bean;
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();

        Object bean1 = context.getBean("getBean");

        System.out.println(bean1);
        Object bean2 = context.getBean("getBean");
        System.out.println(bean2);
    }
}
