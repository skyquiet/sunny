package com.sunny.core.client;

import com.sunny.core.rpc.RpcContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理RPC请求的容器
 *
 * 实现事件驱动
 *
 * @author: sunlijie
 * CreateDate: 2017/12/12 15:28
 */

public class TransactionContainer {

    private Map<Long, RpcContext> transactionContainer = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Long, RpcContext> transactionContainer = new ConcurrentHashMap<>();


    public void put(Long requestId, RpcContext rpcContext) {
        transactionContainer.put(requestId, rpcContext);
    }

    public RpcContext get(Long requestId) {
        return transactionContainer.get(requestId);
    }

    public void remove(Long requestId) {
        transactionContainer.remove(requestId);
    }

    public boolean containKey(Long requestId) {
        return transactionContainer.containsKey(requestId);
    }

}
