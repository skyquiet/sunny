package com.sunny.core.client;

import com.sunny.core.rpc.Request;
import com.sunny.core.rpc.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:39
 */

public interface Client {

    public void start() throws IOException, InterruptedException;

    public Response send(Request msg) throws InterruptedException, ExecutionException;


    /**
     * 获取接口的代理
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clz);





}
