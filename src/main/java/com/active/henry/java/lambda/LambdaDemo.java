package com.active.henry.java.lambda;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

public class LambdaDemo {
    public static void main(String[] args) {
        // 匿名内部类写法 无参函数
        new Thread(new Runnable() {
            @Override public void run() {
                System.out.println("Hello World!");
            }
        }).run();

        // lambda写法1 (单行)
        new Thread(
            () -> System.out.println("Hello World2!")

        ).run();

        // lambda写法2 (多行)
        new Thread(
            () -> {
                System.out.println("Hello World3!");
            }

        ).run();

        // lambda写法3 (方法引用) call println()
        new Thread(
            LambdaDemo::println
        ).run();

        // lambda写法4 (方法引用) call System.out.println()
        new Thread(
            System.out::println
        ).run();


        // JDK7 匿名内部类写法 有参函数
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

        // lambda写法
        List<String> list2 = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list2, (String s1, String s2) -> {
            if (s1 == null)
                return -1;
            if (s2 == null)
                return 1;
            return s1.length()-s2.length();
        });

        // lambda写法
        List<String> list3 = Arrays.asList("I", "love", "you", "too");
        Collections.sort(list3, (s1, s2) -> {// 省略参数表的类型
            if (s1 == null)
                return -1;
            if (s2 == null)
                return 1;
            return s1.length()-s2.length();
        });

        showMyStream();

    }

    private static void showLambda() {
        // Lambda表达式的书写形式
        Runnable run = () -> System.out.println("Hello World");// 1

        // ActionListener虽然没有被注解@FunctionalInterface, 仍然会被编译器当成函数式接口对待
        ActionListener listener = (ActionEvent event) -> System.out.println("button clicked");// 2
        // java编译器的类型推断机制 可以省略lambda参数类型
        ActionListener listener2 = event -> System.out.println("button clicked");// 3

        ActionListener listener3 = e -> println(e); //4
        ActionListener listener4 = e -> println(); //5
        ActionListener listener5 = LambdaDemo::println; // 6 (方法引用) call println(ActionEvent e)
        ActionListener listener6 = System.out::println; // 7 (方法引用) call System.out.println(Object x)
        Runnable multiLine = () -> {// 8 代码块
            System.out.print("Hello");
            System.out.println(" Hoolee");
        };
        // lambda多参数
        BinaryOperator<Long> add = (Long x, Long y) -> x + y;// 9
        BinaryOperator<Long> add2 = LambdaDemo::add;// 10 (方法引用)
        BinaryOperator<Long> addImplicit = (x, y) -> x + y;// 11 类型推断
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
    public interface ConsumerInterface<T> {
        void accept(T t);
//        void test(String str);
    }
    private static void testConsumerInterface() {
        ConsumerInterface<String> consumer = str -> System.out.println(str);
        // 方法引用是lambda的一种写法，不能和"() ->" 混用在一起！
        ConsumerInterface<Integer> consumer2 = System.out::println;
        Consumer<String> consumer3 = System.out::println;

    }


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
