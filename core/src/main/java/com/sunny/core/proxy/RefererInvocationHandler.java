package com.sunny.core.proxy;

import com.alibaba.fastjson.JSON;
import com.sunny.core.rpc.DefaultRequest;
import com.sunny.core.utils.RequestIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/7 17:38
 */

public class RefererInvocationHandler implements InvocationHandler {

    private Class cls;

    public RefererInvocationHandler(Class serviceClass) {
        this.cls = serviceClass;
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
        System.out.println(JSON.toJSONString(request));

        /**
         * 2. 序列化
         *      1. 自定义协议头 todo
         */

        //3.发送请求




        //4.处理结果 ，返回接口对应的返回类型

        /**
         * 5.处理异常情况
         *      1.业务异常
         *      2.框架异常
         */







        return null;
    }
}
