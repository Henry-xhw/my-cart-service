package com.active.henry.java.lambda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

public class LambdaDemo {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override public void run() {
                System.out.println("Hello World!");
            }
        }).run();


        new Thread(
            () -> System.out.println("Hello World2!")

        ).run();


        new Thread(
            () -> {
                System.out.println("Hello World3!");
            }

        ).run();

        // JDK7 匿名内部类写法
        List<String> list = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list, new Comparator<String>(){// 接口名
            @Override
            public int compare(String s1, String s2){// 方法名
                if(s1 == null)
                    return -1;
                if(s2 == null)
                    return 1;
                return s1.length()-s2.length();
            }
        });

        List<String> list2 = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list2, new Comparator<String>() {
            @Override public int compare(String s1, String s2) {
                if (s1 == null)
                    return -1;
                if (s2 == null)
                    return 1;
                return s1.length()-s2.length();
            }
        });

        List<String> list3 = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list3, (s1, s2) -> {
            if (s1 == null)
                return -1;
            if (s2 == null)
                return 1;
            return s1.length()-s2.length();
        });


        // Lambda表达式的书写形式
        Runnable run = () -> System.out.println("Hello World");// 1
        ActionListener listener = event -> System.out.println("button clicked");// 2
        ActionListener listener2 = e -> println(e);
        ActionListener listener3 = e -> println();
        ActionListener listener4 = LambdaDemo::println;
        Runnable multiLine = () -> {// 3 代码块
            System.out.print("Hello");
            System.out.println(" Hoolee");
        };
        BinaryOperator<Long> add = (Long x, Long y) -> x + y;// 4
        BinaryOperator<Long> add2 = LambdaDemo::add;// 4
        BinaryOperator<Long> addImplicit = (x, y) -> x + y;// 5 类型推断

        showMyStream();

    }

    private static Long add(Long x, Long y) {
        return x+y;
    }

    private static void println(ActionEvent e) {
        System.out.println(e.toString());
    }

    private static void println() {
        System.out.println("e.toString()");
    }

    // 自定义函数接口
    @FunctionalInterface
    public interface ConsumerInterface<T>{
        void accept(T t);
    }

    public interface ConsumerInterface2<T> {
        void accept(T t);
    }
    ConsumerInterface<String> str = t -> System.out.println(t);




    static class MyStream<T>{
        private List<T> list;
        public MyStream (List<T> list) {
            this.list = list;
        }
        public void myForEach(ConsumerInterface<T> consumer){// 1
            for(T t : list){
                consumer.accept(t);
            }
        }
    }

    private static void showMyStream() {
        MyStream<String> stream2 = new MyStream<String>(Arrays.asList("henry", "yuzi"));
        stream2.myForEach(LambdaDemo::println2);
        stream2.myForEach(t -> System.out.println("xxxyyyzzz: " + t));
        ConsumerInterface<Integer> inte = System.out::println;
        inte.accept(23);
    }

    private static <T> void println2(T t) {
        System.out.println("prefix: " + t.toString());
    }

//    stream.myForEach(str -> System.out.println(str));// 使用自定义函数接口书写Lambda表达式



}
