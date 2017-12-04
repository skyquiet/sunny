package com.sunny.rpc.registry;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 实现服务注册表容器
 *
 * @author: sunlijie
 * CreateDate: 2017/12/4 19:06
 */

public class HashMapRegistry implements Registry {

    /**
     * 服务注册表
     */
    private static final ConcurrentHashMap<String, Set<String>> serviceRegistry = new ConcurrentHashMap<>();

    @Override
    public boolean register(String serviceName, String IP, int port) {
        try {
            String address = IP + ":" + port;
            //ConcurrentHashMap线程安全？ todo
            if (serviceRegistry.contains(serviceName)) {
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
        }
    }

    @Override
    public boolean unregister(String serviceName, String IP, int port) {
        try {
            if (serviceRegistry.contains(serviceName)) {
                String address = IP + ":" + port;
                serviceRegistry.get(serviceName).remove(address);
            }
            return true;
        } catch (Exception e) {
            // TODO: 2017/12/4 log
            return false;
        }
    }

    @Override
    public Set<String> getServiceProvidersList(String serviceName) {
        return serviceRegistry.get(serviceName);
    }
}
