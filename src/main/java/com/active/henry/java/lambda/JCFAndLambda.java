package com.active.henry.java.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JCFAndLambda {
    public static void main(String[] args) {


        removeIf();
        replaceAll();
        showStream();
    }

    private static void showForeach() {

        // 原始的for循环迭代
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        for(int i = 0; i < list.size(); i++){
            String str = list.get(i);
            if(str.length()>3)
                System.out.println(str);
        }


        // 使用增强for循环迭代
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        for(String str : list){
            if(str.length()>3)
                System.out.println(str);
        }

        // 使用forEach()结合匿名内部类迭代
        ArrayList<String> list5 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list5.forEach(new Consumer<String>() {
            @Override public void accept(String s) {
                if(s.length()>3)
                    System.out.println(s);
            }
        });

        // 使用forEach()结合Lambda表达式迭代
        ArrayList<String> list3 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list2.forEach(t -> {
            if(t.length()>3)
                System.out.println(t);
        });


        // 使用forEach()结合Lambda表达式迭代(方法引用)
        ArrayList<String> list4 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list3.forEach(JCFAndLambda::println);


    }
    private static void println(String str) {
        if(str.length()>3)
            System.out.println("foreach: " + str);
    }

    private static void removeIf() {
        // 使用迭代器删除列表元素
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
            if(it.next().length()>3) // 删除长度大于3的元素
                it.remove();
        }

        //Exception in thread "main" java.util.ConcurrentModificationException
//        list.forEach(str -> {
//            if (str.length() > 3)
//                list.remove(str);
//        });
//        list.forEach(System.out::println);

        // 使用removeIf()结合匿名名内部类实现
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list2.removeIf(new Predicate<String>(){ // 删除长度大于3的元素
            @Override
            public boolean test(String str){
                return str.length()>3;
            }
        });

        // 使用removeIf()结合Lambda表达式实现
        ArrayList<String> list3 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list3.removeIf(str -> str.length() > 3); // 删除长度大于3的元素
        list3.forEach(System.out::println);

    }

    private static void replaceAll() {
        // 使用下标实现元素替换
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        for(int i=0; i<list.size(); i++){
            String str = list.get(i);
            if(str.length()>3)
                list.set(i, str.toUpperCase());
        }

        // 使用匿名内部类实现
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list2.replaceAll(new UnaryOperator<String>(){
            @Override
            public String apply(String str){
                if(str.length()>3)
                    return str.toUpperCase();
                return str;
            }
        });

        // 使用Lambda表达式实现
        ArrayList<String> list3 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list3.replaceAll(str -> {
            if(str.length()>3)
                return str.toUpperCase();
            return str;
        });


    }

    private static void showStream() {
        // 保留长度等于3的字符串
        Stream<String> stream= Stream.of("I", "love", "you", "too");
        stream.filter(str -> str.length()==3)
            .forEach(str -> System.out.println(str));

        Stream<String> stream2= Stream.of("I", "love", "you", "too");
        stream2.sorted((str1, str2) -> str1.length()-str2.length())
            .forEach(str -> System.out.println(str));

        Stream<String> stream3 = Stream.of("I", "love", "you", "too");
        stream3.map(str -> 5)
            .forEach(str -> System.out.println(str));


        Stream<List<Integer>> stream4 = Stream.of(Arrays.asList(1,2), Arrays.asList(3, 4, 5));
        stream4.flatMap(list -> list.stream())
            .forEach(i -> System.out.println(i));

        // 找出最长的单词
        Stream<String> stream5 = Stream.of("I", "love", "you", "too");
        Optional<String> longest = stream5.reduce((s1, s2) -> s1.length()>=s2.length() ? s1 : s2);
        //Optional<String> longest = stream.max((s1, s2) -> s1.length()-s2.length());
        System.out.println(longest.get());


        // 求单词长度之和
        Stream<String> stream6 = Stream.of("I", "love", "you", "too");
        Integer lengthSum = stream6.reduce(0,// 初始值　// (1)
        (sum, str) -> sum+str.length(), // 累加器 // (2)
            (a, b) -> a+b);// 部分和拼接器，并行执行时才会用到 // (3)
        // int lengthSum = stream.mapToInt(str -> str.length()).sum();
        System.out.println(lengthSum);
    }

    private static void showCollect() {
        // 将Stream转换成容器或Map
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        List<String> list = stream.collect(Collectors.toList()); // (1)
        // Set<String> set = stream.collect(Collectors.toSet()); // (2)
        // Map<String, Integer> map = stream.collect(Collectors.toMap(Function.identity(), String::length)); // (3)

        //　将Stream规约成List
        Stream<String> stream2 = Stream.of("I", "love", "you", "too");
        List<String> list2 = stream2.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);// 方式１
        //List<String> list = stream.collect(Collectors.toList());// 方式2
        System.out.println(list2);



        // 将Stream转换成List或Set
        Stream<String> stream3 = Stream.of("I", "love", "you", "too");
        List<String> list3 = stream3.collect(Collectors.toList()); // (1)
        Set<String> set = stream3.collect(Collectors.toSet()); // (2)



        // 使用toCollection()指定规约容器的类型
        Stream<String> stream4 = Stream.of("I", "love", "you", "too");
        ArrayList<String> arrayList = stream4.collect(Collectors.toCollection(ArrayList::new));// (3)
        HashSet<String> hashSet = stream4.collect(Collectors.toCollection(HashSet::new));// (4)


        // 使用toMap()统计学生GPA
//        Map<Student, Double> studentToGPA =
//            students.stream().collect(Collectors.toMap(Function.identity(),// 如何生成key
//                student -> computeGPA(student)));// 如何生成value


        // Partition students into passing and failing
//        Map<Boolean, List<Student>> passingFailing = students.stream()
//            .collect(Collectors.partitioningBy(s -> s.getGrade() >= PASS_THRESHOLD));

        // Group employees by department
//        Map<Department, List<Employee>> byDept = employees.stream()
//            .collect(Collectors.groupingBy(Employee::getDepartment));


        // 使用下游收集器统计每个部门的人数
//        Map<Department, Integer> totalByDept = employees.stream()
//            .collect(Collectors.groupingBy(Employee::getDepartment,
//                Collectors.counting()));// 下游收集器

        // 按照部门对员工分布组，并只保留员工的名字
//        Map<Department, List<String>> byDept = employees.stream()
//            .collect(Collectors.groupingBy(Employee::getDepartment,
//                Collectors.mapping(Employee::getName,// 下游收集器
//                    Collectors.toList())));// 更下游的收集器


        // 使用Collectors.joining()拼接字符串
        Stream<String> stream5 = Stream.of("I", "love", "you");
        //String joined = stream.collect(Collectors.joining());// "Iloveyou"
        //String joined = stream.collect(Collectors.joining(","));// "I,love,you"
        String joined = stream5.collect(Collectors.joining(",", "{", "}"));// "{I,love,you}"

    }

    private static void showSort() {
        // Collections.sort()方法
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        Collections.sort(list, new Comparator<String>(){
            @Override
            public int compare(String str1, String str2){
                return str1.length()-str2.length();
            }
        });

        // List.sort()方法结合Lambda表达式
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list2.sort((str1, str2) -> str1.length()-str2.length());
    }

    private static void showMapForeach() {
        // Java7以及之前迭代Map
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        for(Map.Entry<Integer, String> entry : map.entrySet()){
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        // 使用forEach()结合匿名内部类迭代Map
        HashMap<Integer, String> map2 = new HashMap<>();
        map2.put(1, "one");
        map2.put(2, "two");
        map2.put(3, "three");
        map2.forEach(new BiConsumer<Integer, String>(){
            @Override
            public void accept(Integer k, String v){
                System.out.println(k + "=" + v);
            }
        });

        // 使用forEach()结合Lambda表达式迭代Map
        HashMap<Integer, String> map3 = new HashMap<>();
        map3.put(1, "one");
        map3.put(2, "two");
        map3.put(3, "three");
        map3.forEach((k, v) -> System.out.println(k + "=" + v));
    }

    private static void showMapGetOrDefault() {
        // 查询Map中指定的值，不存在时使用默认值
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        // Java7以及之前做法
        if(map.containsKey(4)){ // 1
            System.out.println(map.get(4));
        }else{
            System.out.println("NoValue");
        }
        // Java8使用Map.getOrDefault()
        System.out.println(map.getOrDefault(4, "NoValue")); // 2
    }

    private static void showMapRemoveAll() {
        // Java7以及之前替换所有Map中所有映射关系
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        for(Map.Entry<Integer, String> entry : map.entrySet()){
            entry.setValue(entry.getValue().toUpperCase());
        }

        // 使用replaceAll()结合匿名内部类实现
        HashMap<Integer, String> map2 = new HashMap<>();
        map2.put(1, "one");
        map2.put(2, "two");
        map2.put(3, "three");
        map2.replaceAll(new BiFunction<Integer, String, String>(){
            @Override
            public String apply(Integer k, String v){
                return v.toUpperCase();
            }
        });

        // 使用replaceAll()结合Lambda表达式实现
        HashMap<Integer, String> map3 = new HashMap<>();
        map3.put(1, "one");
        map3.put(2, "two");
        map3.put(3, "three");
        map3.replaceAll((k, v) -> v.toUpperCase());
    }

}
