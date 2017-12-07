package com.sunny.core.rpc;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/7 17:59
 */

public interface Request {

    /**
     *
     * service interface
     *
     * @return
     */
    String getInterfaceName();

    /**
     * service method name
     *
     * @return
     */
    String getMethodName();

    /**
     * service method param
     *
     * @return
     */
    Object[] getArguments();

    /**
     * request id
     *
     * @return
     */
    long getRequestId();


}
