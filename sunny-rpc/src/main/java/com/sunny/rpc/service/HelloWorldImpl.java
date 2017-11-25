package com.sunny.rpc.service;

public class HelloWorldImpl  implements HelloWorld{
    @Override
    public void say(String name) {
        System.out.println("hello " + name);
    }
}
