package com.sunny.core.service;

public class HelloWorldImpl  implements HelloWorld{
    @Override
    public String say(String name) {
//        System.out.println("hello " + name);
        return "hello " + add(name);
    }

    private String add(String str){
        return str + " !";
    }
}
