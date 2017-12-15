package com.sunny.core.server.impl;

import com.sunny.core.registry.HashMapRegistry;
import com.sunny.core.registry.Registry;
import com.sunny.core.rpc.DefaultResponse;
import com.sunny.core.rpc.Request;
import com.sunny.core.rpc.Response;
import com.sunny.core.server.Server;
import com.sunny.core.service.HelloWorld;
import com.sunny.core.service.HelloWorldImpl;
import com.sunny.core.utils.SocketUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * BIO 服务端
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 15:59
 */

public class BIOServer implements Server {

    private ServerSocket serverSocket;

    private Registry registry = new HashMapRegistry();

    /**
     * 服务端提供的API 容器
     * <interfaceName,interfaceInstance>
     */
    private Map<String, Object> interfaceContainer = new HashMap<>();

    private int port;

    public BIOServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            //todo registry 要不要搞个单例，另外这个registry应该是单独一个应用啊
//            this.registry = registry.newInstance();
            this.port = port;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start() {
        try {
            System.out.println(this.getClass().getName() + " started!");
            //每一个tcp连接创建一个线程
            while (true) {
                Socket socketClient = serverSocket.accept();
                receive(socketClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive(Socket socketClient) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socketClient.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
                    Request request;
                    while ((request = (Request) objectInputStream.readObject()) != null) {

                        //反射调用接口方法
                        Object value = invokeMethod(request);

                        Response response = new DefaultResponse();
                        response.setRequestId(request.getRequestId());
                        response.setValue(value);
                        objectOutputStream.writeObject(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
//                    try {
//                        socketClient.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();
    }

    private Object invokeMethod(Request request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //  获取接口实例
        Object interfaceInstance = interfaceContainer.get(request.getInterfaceName());

        Object[] args = request.getArguments();
        //获得参数类型数组
        Class[] argsTypes = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            argsTypes[i] = args[i].getClass();
        }

        //反射调用接口指定方法
        Method method = interfaceInstance
                .getClass()
                .getMethod(request.getMethodName(), argsTypes);

        return method.invoke(interfaceInstance, args);
    }

    @Override
    public void stop() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void bindServices(Class interfaceClass, Class implClass) {
        registry.register(interfaceClass.getName(), SocketUtil.getIP(), port);
        try {
            interfaceContainer.put(interfaceClass.getName(), implClass.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}

