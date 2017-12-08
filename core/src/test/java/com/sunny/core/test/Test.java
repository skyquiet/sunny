package com.sunny.core.test;

import com.sunny.core.proxy.ProxyFactory;
import com.sunny.core.proxy.RefererInvocationHandler;
import com.sunny.core.proxy.impl.JdkProxyFactory;
import com.sunny.core.service.HelloWorld;
import com.sunny.core.service.HelloWorldImpl;

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
        ProxyFactory proxyFactory = new JdkProxyFactory();

        HelloWorld helloWorld = proxyFactory.getProxy(HelloWorld.class, new RefererInvocationHandler(HelloWorld.class));

        helloWorld.say("sunlijie");
    }

    public static void main(String[] args) {



    }

}
