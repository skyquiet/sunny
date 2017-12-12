package com.sunny.core.rpc;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/7 17:59
 */

public interface Response {

    /**
     * <pre>
     * 		如果 request 正常处理，那么会返回 Object value，而如果 request 处理有异常，那么 getValue 会抛出异常
     * </pre>
     *
     * @throws RuntimeException
     * @return
     */
    Object getValue();

    void setValue(Object o);

    /**
     * 如果request处理有异常，那么调用该方法return exception 如果request还没处理完或者request处理正常，那么return null
     *
     * <pre>
     * 		该方法不会阻塞，无论该request是处理中还是处理完成
     * </pre>
     *
     * @return
     */
    Exception getException();

    void setException(Exception e);

    /**
     * 与 Request 的 requestId 相对应
     *
     * @return
     */
    long getRequestId();

    void setRequestId(long requestId);


    // TODO: 2017/12/7  timeout
    int getTimeout();

}
