package com.sunny.core.proxy.impl;

import com.sunny.core.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkProxyFactory implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {clz}, invocationHandler);
    }

}
