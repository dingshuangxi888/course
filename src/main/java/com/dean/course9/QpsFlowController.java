package com.dean.course9;

import java.util.concurrent.*;

/**
 * QPSÁ÷Á¿¿ØÖÆ
 * Created by dean on 15/2/8.
 */
public class QpsFlowController {

    private final int MAX_QPS = 50;

    private Semaphore semaphore = new Semaphore(MAX_QPS);

    public void releaseWork() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                semaphore.release(MAX_QPS);
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    private boolean controlFlow() {
        if (semaphore.getQueueLength() > 0) {
            System.out.println("Thead " + Thread.currentThread().getName() + " is rejected");
            return false;
        }
        try {
            semaphore.acquire();
            System.out.println("Thead " + Thread.currentThread().getName() + " is accessed");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        QpsFlowController qpsFlowController = new QpsFlowController();
        qpsFlowController.releaseWork();
        qpsFlowController.simulateAccess(1000);

    }
}
