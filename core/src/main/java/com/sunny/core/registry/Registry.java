package com.sunny.core.registry;

import java.util.Set;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/4 19:00
 */

public interface Registry {

    /**
     * 注册服务
     *
     * @param serviceName
     * @param IP
     * @param port
     * @return
     */
    public boolean register(String serviceName, String IP, int port);

    /**
     * 注销服务
     *
     * @param serviceName
     * @param IP
     * @param port
     * @return
     */
    public boolean unregister(String serviceName, String IP, int port);

    /**
     * 获取服务提供至列表
     *
     * @param serviceName
     * @return
     */
    public Set<String> getServiceProvidersList(String serviceName);

}
