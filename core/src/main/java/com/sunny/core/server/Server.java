package com.sunny.core.server;

public interface Server {

    void start();

    void stop();

    void shutdown();

     void bindServices(Class interfaceClass , Class implClass);


}
