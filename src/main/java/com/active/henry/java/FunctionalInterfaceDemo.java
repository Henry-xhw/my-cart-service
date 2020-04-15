package com.active.henry.java;

public class FunctionalInterfaceDemo {

    public static void main(String[] arg) {


        Function1 function1 = str -> {

        };

        FunctionalInterfaceWithoutAnnotation f2 = () -> "hello";

        FunctionalInterface3 functionalInterface3 = integer -> String.valueOf(integer);
    }

    @FunctionalInterface
    public interface Function1 {

        public abstract void execute(String str);

        // 不能出现两次抽象方法定义
        //        void execute2();

        default String getDescription() {
            return String.valueOf(this);
        }
    }

    //    @FunctionalInterface // @FunctionalInterface 并非必选的
    public interface FunctionalInterfaceWithoutAnnotation {

        String execute();
    }

    //    @FunctionalInterface // @FunctionalInterface 只能描述接口
    //    public class FunctionClass {
    //
    //    }

    public interface FunctionalInterface3 {
        String integerToString(Integer integer);
    }
}
