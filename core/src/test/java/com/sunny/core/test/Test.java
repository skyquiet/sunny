package com.sunny.core.test;

import com.sunny.core.client.Client;
import com.sunny.core.client.impl.BIOClient;
import com.sunny.core.proxy.ProxyFactory;
import com.sunny.core.proxy.RefererInvocationHandler;
import com.sunny.core.proxy.impl.JdkProxyFactory;
import com.sunny.core.registry.HashMapRegistry;
import com.sunny.core.registry.Registry;
import com.sunny.core.server.Server;
import com.sunny.core.server.impl.BIOServer;
import com.sunny.core.service.HelloWorld;
import com.sunny.core.service.HelloWorldImpl;

import java.io.IOException;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:31
 */

public class Test {

    private int port = 112;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

//    public Test(int port) {
//        this.port = port;
//    }

//    public void tt() {
//        Test test = new Test(6666);
//
//        System.out.println(test.getPort());
//
//    }


    @org.junit.Test
    public void testProxy() {

        System.out.println(222);
        ProxyFactory proxyFactory = new JdkProxyFactory();

//        HelloWorld helloWorld = proxyFactory.getProxy(HelloWorld.class, new RefererInvocationHandler(HelloWorld.class));
//
//        helloWorld.say("sunlijie");

        //1. new server

        //2.bind service

        //3.start

    }

    public void testClient() throws IOException, InterruptedException {

        //1. new client

        Client cli = new BIOClient(8989);

        cli.start();

        //2.get proxy
        HelloWorld helloWorld =  cli.getProxy(HelloWorld.class);

        //3.invoke

        helloWorld.say("sunlijie");

    }

    public void testServer()  {

//        1. new server
        new BIOServer(8989).start();

        System.out.println(1111111);

    }


    public static void main(String[] args) {

        Test test = new Test();
        test.testServer();

    }

}
