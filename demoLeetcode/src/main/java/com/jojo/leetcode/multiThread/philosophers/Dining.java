package com.jojo.leetcode.multiThread.philosophers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dining {

    public static void main(String[] args) {
        Fork fork1 = new Fork("1");
        Fork fork2 = new Fork("2");
        Fork fork3 = new Fork("3");
        Fork fork4 = new Fork("4");
        Fork fork5 = new Fork("5");

        int eatTarget = 1;

        List<Philosopher> list = new ArrayList<>();
        list.add(new Philosopher("A", fork1, fork5, eatTarget));
        list.add(new Philosopher("B", fork2, fork1, eatTarget));
        list.add(new Philosopher("C", fork3, fork2, eatTarget));
        list.add(new Philosopher("D", fork4, fork3, eatTarget));
        list.add(new Philosopher("E", fork5, fork4, eatTarget));

        ExecutorService executorService = Executors.newFixedThreadPool(list.size());
        for (Philosopher philosopher : list) {
            executorService.execute(() -> philosopher.tryEat());
        }
        executorService.shutdown();

    }

}
