package com.jojo.leetcode;

public class L1114Foo {

    private volatile boolean first = true;
    private volatile boolean second = false;
    private volatile boolean third = false;

    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();
        second = true;
    }

    public void second(Runnable printSecond) throws InterruptedException {
        while (!second) {
            Thread.sleep(20);
            System.out.println(Thread.currentThread().getId() + "尝试获取second锁");
        }
        printSecond.run();
        third = true;
    }

    public void third(Runnable printThird) throws InterruptedException {
        while (!third) {
            Thread.sleep(20);
            System.out.println(Thread.currentThread().getId() + "尝试获取third锁");
        }
        printThird.run();
    }


    public static void main(String[] args) {
        L1114Foo foo = new L1114Foo();

        Thread thread1 = new Thread(() -> {
            try {
                foo.first(() -> System.out.println("first"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                foo.second(() -> System.out.println("second"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread thread3 = new Thread(() -> {
            try {
                foo.third(() -> System.out.println("third"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("===启动3条线程");
        thread3.start();
        thread2.start();
        thread1.start();

    }
}
