
package com.jojo.zzz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    HashMap<Integer, Integer> map = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        Work work = new Work();
        for (int i = 0; i < 250; i++) {
            work.map.put(i, i);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 250; i < 500; i++) {
                    work.map.put(i, i);
                }
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 250; i++) {
                    work.map.put(i, i);
                }
            }
        });

        executorService.shutdown();

        Thread.sleep(1000);

        for (Map.Entry<Integer, Integer> entry : work.map.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }


}