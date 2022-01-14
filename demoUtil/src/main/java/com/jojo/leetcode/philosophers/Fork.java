package com.jojo.leetcode.philosophers;

public class Fork {

    private volatile boolean isHolded = false;

    String holderName = "";

    String forkName = "";

    public Fork(String forkName) {
        this.forkName = forkName;
    }

    public synchronized boolean takeUp(String name) {
        System.out.println(name + "尝试拿起" + forkName);
        if (isHolded) {
            if (holderName.equals(name)) {
                System.out.println(name + "再次拿起" + forkName + "成功");
                return true;
            }
            System.out.println(name + "拿起" + forkName + "失败");
            return false;
        }

        isHolded = true;
        holderName = name;
        System.out.println(name + "拿起" + forkName + "成功");

        return true;
    }

    public synchronized boolean putDown() {
        System.out.println(holderName + "尝试放下" + forkName);
        holderName = "";
        isHolded = false;
        System.out.println(holderName + "放下" + forkName + "成功");
        return true;
    }
}
