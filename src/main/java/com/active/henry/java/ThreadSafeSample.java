package com.active.henry.java;


public class ThreadSafeSample {
    public int sharedState;
    public void nonSafeAction() {
        while (sharedState < 100000) {
            synchronized (this) {
                int former = sharedState++;
                int latter = sharedState;
                System.out.println("current thread: " + Thread.currentThread() + "former: " + former + " ; latter: " + latter);
                if (former != latter - 1) {
                    System.out.printf("Observed data race, former is " +
                            former + ", " + "latter is " + latter);
                }
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeSample sample = new ThreadSafeSample();
        Thread threadA = new Thread("ThreadA"){
            public void run(){
                sample.nonSafeAction();
            }
        };
        Thread threadB = new Thread("ThreadB"){
            public void run(){
                sample.nonSafeAction();
            }
        };
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
    }
}
