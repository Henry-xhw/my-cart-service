package com.active.henry.java.anonymous;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnonymousClassDemo {

    // static block   ---1
    static {
        // JDK7 匿名内部类写法
        new Thread(new Runnable(){// 接口名
            @Override
            public void run(){// 方法名
                System.out.println("Thread run()");
            }
        }).start();
    }

    // instance block ----2
    {
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
    }

    // anonymous class can be in constructor -----3
    public AnonymousClassDemo () {
        // JDK7 匿名内部类写法
        new Thread(new Runnable(){// 接口名
            @Override
            public void run(){// 方法名
                System.out.println("Thread run()");
            }
        }).start();
    }

    // anonymous class can be in static method ----4
    private static void staticMethod () {
        // JDK7 匿名内部类写法
        new Thread(new Runnable(){// 接口名
            @Override
            public void run(){// 方法名
                System.out.println("Thread run()");
            }
        }).start();
    }

    // anonymous class can be in non-static method(instance method) ----5
    public void instanceMethod () {
        // JDK7 匿名内部类写法
        new Thread(new Runnable(){// 接口名
            @Override
            public void run(){// 方法名
                System.out.println("Thread run()");
            }
        }).start();
    }
}
