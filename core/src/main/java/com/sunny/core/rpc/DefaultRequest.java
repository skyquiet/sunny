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
        return null;
    }

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public long getRequestId() {
        return 0;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }
    @Override
    public String toString() {
        return interfaceName + "." + methodName +" requestId=" + requestId;
    }

}
