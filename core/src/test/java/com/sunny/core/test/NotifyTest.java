package com.sunny.core.test;


/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:31
 */

public class NotifyTest {

    private Object lock = new Object();

    private volatile String flag[] = { "true" };

    public static void main(String[] args) {

        NotifyTest test = new NotifyTest();



        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (test.flag) {
                    try {
                        System.out.println("listen thread");
                        while (test.flag[0]!="false") {
                            System.out.println("11111");
                            test.flag.wait();
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
                synchronized (test.flag) {
                    try {
                        System.out.println(66666666);
                        Thread.sleep(1000);
                        test.flag[0] = "false";
                        test.flag.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}
