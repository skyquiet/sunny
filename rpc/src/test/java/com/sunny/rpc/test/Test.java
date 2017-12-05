package com.sunny.rpc.test;

/**
 * todo Description
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:31
 */

public class Test {

    private int port = 112;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Test(int port) {
        this.port = port;
    }

    public void tt() {
        Test test = new Test(6666);

        System.out.println(test.getPort());

    }

    public static void main(String[] args) {
        Test test = new Test(6666);

        System.out.println(test.getPort());
    }

}
