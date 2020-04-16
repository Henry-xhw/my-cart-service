package com.active.henry.java.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JCFAndLambda {
    public static void main(String[] args) {
        // 使用增强for循环迭代
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        for(String str : list){
            if(str.length()>3)
                System.out.println(str);
        }

        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list2.forEach(t -> {
            if(t.length()>3)
                System.out.println(t);
        });


        ArrayList<String> list3 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list3.forEach(JCFAndLambda::println);

        ArrayList<String> list4 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list4.forEach(new Consumer<String>() {
            @Override public void accept(String s) {
                if(s.length()>3)
                    System.out.println(s);
            }
        });

        removeIf();
        replaceAll();
        showStream();
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

        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list2.removeIf(str -> str.length() > 3);
        list2.forEach(System.out::println);

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
}
