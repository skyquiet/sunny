package com.sunny.core.client.impl;

import com.sunny.core.client.Client;
import com.sunny.core.utils.SocketUtil;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BIO core 客户端
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:39
 */

public class BIOClient implements Client {

    private int port;

    private Socket socket;

    private LinkedBlockingQueue<Object> msgQueue = new LinkedBlockingQueue();

    public BIOClient(int port) throws IOException {
        this.port = port;
        this.socket = new Socket(SocketUtil.getIP(), port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        BIOClient client =  new BIOClient(8989);
        client.start();

        for (int i = 0; i < 100; i++) {
            client.sendMsg("sunlijie >> " + i);
            Thread.sleep(1000);
        }
    }

    public void start() throws IOException, InterruptedException {
        read();
        writeListener();
        System.out.println(this.getClass().getName()+" started!");

    }

    private void read() throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = socket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    Object obj;
                    while ((obj = objectInputStream.readObject()) != null) {
                        String msg = (String) obj;
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendMsg(String msg){
        msgQueue.add(msg);
    }
    public void writeListener() throws IOException, InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    Object msg;
                    while ((msg = msgQueue.take()) !=null) {
                        objectOutputStream.writeObject(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}