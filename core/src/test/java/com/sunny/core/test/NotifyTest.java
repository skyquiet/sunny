package com.sunny.core.test;


/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:31
 */

public class NotifyTest {

    private Object lock = new Object();

    private volatile boolean open = false;

    public static void main(String[] args) {

        NotifyTest test = new NotifyTest();



        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (test.lock) {
                    try {
                        while (test.open == false) {
                            System.out.println("11111");
                            test.lock.wait();
                        }
                        System.out.println(2222);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (test.lock) {
                    try {
                        Thread.sleep(1000);
                        test.open = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}
