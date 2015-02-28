package com.dean.course9;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 并发流量控制
 * Created by dean on 15/2/8.
 */
public class ConcurrentFlowController {

    private Semaphore semaphore = new Semaphore(20);

    private boolean controlFlow() {
        if (semaphore.getQueueLength() > 10) {
            System.out.println("Thead " + Thread.currentThread().getName() + " is rejected");
            return false;
        }
        try {
            semaphore.acquire();
            System.out.println("Thead " + Thread.currentThread().getName() + " is accessed");
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        return true;
    }

    public void simulateAccess(int threadSize) {

        CountDownLatch countDownLatch = new CountDownLatch(threadSize);

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        final int[] accessNum = {0};
        final int[] rejectNum = {0};

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadSize; i++) {
            final String threadName = i + "";
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName(threadName);
                    boolean isAccess = controlFlow();
                    if (isAccess) {
                        accessNum[0] ++;
                    } else {
                        rejectNum[0] ++;
                    }
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Access Num: " + accessNum[0]);
        System.out.println("Reject Num: " + rejectNum[0]);
        System.out.println("Using time: " + (endTime - startTime) + " ms");

        executorService.shutdown();
    }

    public static void main(String[] args) {

        ConcurrentFlowController concurrentFlowController = new ConcurrentFlowController();
        concurrentFlowController.simulateAccess(1000);
    }
}
