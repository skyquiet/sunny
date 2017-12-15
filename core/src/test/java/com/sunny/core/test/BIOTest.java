package com.sunny.core.test;

import com.sunny.core.client.Client;
import com.sunny.core.client.impl.BIOClient;
import com.sunny.core.server.Server;
import com.sunny.core.server.impl.BIOServer;
import com.sunny.core.service.HelloWorld;
import com.sunny.core.service.HelloWorldImpl;

import java.io.IOException;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/15 10:48
 */

public class BIOTest {

    public  void startClient() {
        for (int j = 0; j < 2; j++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Client cli = null;
                    try {
                        cli = new BIOClient(8989);
                        cli.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //2.get proxy
                    HelloWorld helloWorld = cli.getProxy(HelloWorld.class);

                    //3.invoke
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < 10000; i++) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + i);
                        String ret = helloWorld.say("sunlijie " + i);

                        System.out.println(Thread.currentThread().getName()+" ret : " + ret);
                    }

                    System.out.println(Thread.currentThread().getName()+" cost time : " + (System.currentTimeMillis() - start));
                }
            }).start();
            System.out.println(j+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new BIOServer(8989);
                server.bindServices(HelloWorld.class, HelloWorldImpl.class);
                server.start();
            }
        }).start();
    }

    public static void main(String[] args) {
        BIOTest test = new BIOTest();
        test.startServer();
        test.startClient();
    }



}
