package com.jojo.leetcode.philosophers;


public class Philosopher {

    public Philosopher(String name, Fork leftFork, Fork rightFork, int target) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.target = target;
    }

    String name;

    private Fork leftFork;
    private Fork rightFork;

    private int target;
    private int eatCount;

    public void tryEat() {
        while (eatCount < target) {
            boolean leftFork = getLeftFork();
            boolean rightFork = getRightFork();

            if (leftFork && rightFork) {
                eat();
            }

            putLeftFork();

            putRightFork();
        }
        System.out.println(name + "完成任务");
    }

    private boolean getLeftFork() {
        return leftFork.takeUp(name);
    }

    private boolean getRightFork() {
        return rightFork.takeUp(name);
    }

    private void eat() {
        eatCount++;
        System.out.println(name + "吃到" + eatCount + "次面条");
    }

    private void putLeftFork() {
        leftFork.putDown();
    }

    private void putRightFork() {
        rightFork.putDown();
    }
}
