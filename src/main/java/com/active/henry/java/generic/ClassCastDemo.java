package com.active.henry.java.generic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ClassCastDemo {
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add(1);
        list.add("henry");

        List<Object> list2 = list;

        Type type = String.class;
        Class type2 = Collection.class;
        System.out.println(type);
        System.out.println(type2);

        List<String> list3 = Arrays.asList("a", "b", "c");
//        list3.add("d");
        list3.forEach(System.out::println);

        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);

        Container<String> container = new Container<>("henry");
        Container<String> container1 = new Container("henry");

    }


    private static void exchange(List a, List b) {
        a.addAll(b);
        Integer integer = (Integer) a.get(0);
    }

    public static class Container <E extends CharSequence> {
        private E e;
        public Container (E e) {
            this.e = e;
        }
    }
}

class A {

}

class B {


}
class C {

}