package com.jojo.leetcode.philosophers;

public class Fork {

    private volatile boolean isHolded = false;

    String holderName = "";

    String forkName = "";

    public synchronized boolean takeUp(String name) {
        System.out.println(name + "尝试拿起" + forkName);
        if (isHolded) {
            if (holderName.equals(name)) {
                return true;
            }
            return false;
        }

        return false;
    }

    public boolean putDown() {
        return false;
    }
}
