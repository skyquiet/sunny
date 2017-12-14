package com.sunny.core.rpc;

import java.io.Serializable;
import java.util.Map;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/7 18:03
 */

public class DefaultRequest implements Serializable, Request {

    private static final long serialVersionUID = 1164620391610215L;
    private String interfaceName;
    private String methodName;
    private Object[] arguments;

    private long requestId;

    @Override
    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return interfaceName + "." + methodName +" requestId=" + requestId;
    }

}
