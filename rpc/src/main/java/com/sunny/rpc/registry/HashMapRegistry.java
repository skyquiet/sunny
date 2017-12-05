package com.sunny.rpc.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * HashMap 实现服务注册表
 *
 * @author: sunlijie
 * CreateDate: 2017/12/4 19:06
 */

public class HashMapRegistry implements Registry {

    /**
     * 服务注册表
     */
    private static final Map<String, Set<String>> serviceRegistry = new HashMap<>();

    private final Lock lock = new ReentrantLock();


    @Override
    public boolean register(String serviceName, String IP, int port) {
        lock.lock();
        try {
            String address = IP + ":" + port;
            if (serviceRegistry.containsKey(serviceName)) {
                serviceRegistry.get(serviceName).add(address);
            } else {
                Set<String> addressList = new HashSet<>();
                addressList.add(address);
                serviceRegistry.put(serviceName, addressList);
            }
            return true;
        } catch (Exception e) {
            //todo log
            return false;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean unregister(String serviceName, String IP, int port) {
        lock.lock();
        try {
            if (serviceRegistry.containsKey(serviceName)) {
                String address = IP + ":" + port;
                serviceRegistry.get(serviceName).remove(address);
            }
            return true;
        } catch (Exception e) {
            // TODO: 2017/12/4 log
            return false;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Set<String> getServiceProvidersList(String serviceName) {
        return serviceRegistry.get(serviceName);
    }
}
