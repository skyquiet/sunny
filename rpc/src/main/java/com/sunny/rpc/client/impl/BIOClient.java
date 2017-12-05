package com.sunny.rpc.client.impl;

import com.sunny.rpc.client.Client;
import com.sunny.rpc.utils.SocketUtil;

import java.io.*;
import java.net.Socket;

/**
 * BIO rpc 客户端
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:39
 */

public class BIOClient implements Client {

    private int port;

    private Socket socket;

    public BIOClient(int port) throws IOException {
        this.port = port;
        this.socket = new Socket(SocketUtil.getIP(), port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        BIOClient client =  new BIOClient(8989);
        client.start();

        for (int i = 0; i < 100; i++) {
            client.write("sunlijie >> " + i);
            Thread.sleep(1000);
        }
    }

    public void start() throws IOException {
//        read();
        System.out.println(this.getClass().getName()+" started!");
    }

    private void read() throws IOException {
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Object obj;
                    while ((obj = objectInputStream.readObject()) != null) {
                        String msg = (String) obj;
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void write(String msg) throws IOException, InterruptedException {
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(msg);
//        objectOutputStream.close();
//        outputStream.close();
    }

}
