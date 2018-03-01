package com.sunny.core.proxy;

import com.alibaba.fastjson.JSON;
import com.sunny.core.client.Client;
import com.sunny.core.rpc.DefaultRequest;
import com.sunny.core.rpc.Response;
import com.sunny.core.utils.RequestIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 客户端proxy调用handler
 *
 * @author: sunlijie
 * CreateDate: 2017/12/7 17:38
 */

public class ConsumerInvocationHandler implements InvocationHandler {

    private Class cls;

    private Client client;

    public ConsumerInvocationHandler(Class serviceClass, Client cli) {
        this.cls = serviceClass;
        this.client = cli;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        // 1. 构建request 对象
        DefaultRequest request = new DefaultRequest();

        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setArguments(args);
        request.setMethodName(method.getName());
        request.setInterfaceName(cls.getName());

        //todo log

        /**
         * 2. 序列化
         *      1. 自定义协议头 todo
         */

        //3.发送请求

        Response response = client.send(request);
        //4.处理结果 ，返回接口对应的返回类型
        if (response.getException() == null){
            return convertObjectType(response.getValue(), method.getReturnType());
        }else {
            /**
             * 5.处理异常情况
             *      1.业务异常
             *      2.框架异常
             */
            return response.getException();
        }

    }

    private <T> T convertObjectType(Object o, Class<T> cls) {
        return (T) o;
    }
}
