package com.sunny.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:41
 */

public class SocketUtil {

    public static String getIP() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
}
