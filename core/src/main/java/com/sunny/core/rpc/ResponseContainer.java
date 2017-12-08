package com.sunny.core.rpc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/8 17:25
 */

public class ResponseContainer {


    private ConcurrentHashMap<Long, Response> responseContainer = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Response> getResponseContainer() {
        return responseContainer;
    }

    public void setResponseContainer(ConcurrentHashMap<Long, Response> responseContainer) {
        this.responseContainer = responseContainer;
        //同时触发callback
    }
}
