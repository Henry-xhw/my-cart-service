package com.active.henry.spring.test.annotations.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class BeanTest {

    @Component
    public class test {
        @Bean(name = {"henry"})
        public   BeanTest  getBean(){
            BeanTest bean = new  BeanTest();
            System.out.println("调用方法："+bean);
            return bean;
        }
    }


    public static void main(String[] args) {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(BeanTest.class);
        context.refresh();

        Object bean1 = context.getBeansOfType(BeanTest.class);

        System.out.println(bean1);
        Object bean2 = context.getBeansOfType(BeanTest.class);
        System.out.println(bean2);
        context.close();
    }
}
