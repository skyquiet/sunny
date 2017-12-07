package com.sunny.core.rpc;

import java.io.Serializable;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/7 18:21
 */

public class DefaultResponse implements Response,Serializable{

    private static final long serialVersionUID = 4281186647291615871L;

    private Object value;
    private Exception exception;
    private long requestId;
    private int timeout;


    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
}
