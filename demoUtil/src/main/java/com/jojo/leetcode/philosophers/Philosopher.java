package com.jojo.leetcode.philosophers;


public class Philosopher {

    String name;

    private Fork leftFork;
    private Fork rightFork;

    private int target;
    private int eatCount;

    public void tryEat(){
        while (eatCount < target) {
            if (!getLeftFork()) {
                continue;
            }

            if (!getRightFork()) {
                continue;
            }

            eat();

            putLeftFork();

            putRightFork();
        }
    }

    private boolean getLeftFork(){
        return leftFork.takeUp(name);
    }
    private boolean getRightFork(){
        return rightFork.takeUp(name);
    }

    private void eat(){
        eatCount++;
    }

    private void putLeftFork(){}
    private void putRightFork(){}
}
