package com.sunny.core.server.impl;

import com.sunny.core.registry.HashMapRegistry;
import com.sunny.core.registry.Registry;
import com.sunny.core.server.Server;
import com.sunny.core.utils.SocketUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO 服务端
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 15:59
 */

public class BIOServer implements Server {

    private ServerSocket serverSocket;

    private Registry registry = new HashMapRegistry();

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

    public static void main(String[] args) {
        new BIOServer(8989).start();
    }

    @Override
    public void start() {
        try {
            System.out.println(this.getClass().getName()+" started!");
            Socket socketClient = serverSocket.accept();
            receive(socketClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive(Socket socketClient) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectInputStream objectInputStream =  new ObjectInputStream(socketClient.getInputStream());
                    ObjectOutputStream objectOutputStream =  new ObjectOutputStream(socketClient.getOutputStream());
                    Object object;
                    while ((object = objectInputStream.readObject()) != null) {
                        System.out.println("receive :" + object);
                        objectOutputStream.writeObject("receive :" +object);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        socketClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Override
    public void stop() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void bindServices(Class[] cls) {
        for (Class cl : cls) {
            registry.register(cl.getName(), SocketUtil.getIP(), port);
        }
    }

}
