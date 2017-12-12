package com.sunny.core.rpc;

/**
 * 包含request、response
 *
 * @author: sunlijie
 * CreateDate: 2017/12/12 15:25
 */

public class RpcContext {

    private Request request;

    private Response response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
