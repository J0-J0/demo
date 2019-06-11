package com.jojo.zzz;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Work {

    public static void main(String[] args) {
        int count = 10000000;
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (int i = 0; i < count; i++) {
                queue.add(1);
            }
        });
        //
        for (int i = 0; i < count; i++) {
            System.out.println("第" + i + "次" + queue.poll());
        }
        executorService.shutdown();
    }

}