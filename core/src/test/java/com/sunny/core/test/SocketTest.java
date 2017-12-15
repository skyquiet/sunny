package com.sunny.core.test;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 这个测试类，测试出来两个结果：
 * 1.只要关闭一个输入或者输出流，这个tcp连接就关闭了
 * 2.tcp其实就不是http那种请求应答模型，而是全双工的，要理解体会这种网络模型，感觉天然适合异步呢
 *
 * @author: sunlijie
 * CreateDate: 2017/12/15 9:39
 */

public class SocketTest {

    private Socket socket;

    public static void main(String[] args) {
        SocketTest test = new SocketTest();
        test.startServer();
        String ret =   test.say("sunlijie");
        System.out.println(ret);
        String ret2=   test.say("sunlijie");
        System.out.println(ret2);
    }

    public void startClient() {
        Socket socket = new Socket();

        InetSocketAddress address = new InetSocketAddress(9999);
        this.socket = socket;
        try {
            socket.connect(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String say(String name) {
        startClient();
        try {
            //写
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            outputStream.writeObject(name);
//            outputStream.close();

            //读
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object obj = inputStream.readObject();

            inputStream.close();
            return (String) obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(9999);
                    while (true){
                        Socket socketCli = serverSocket.accept();
                        reply(socketCli);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void reply(Socket socket) {
        ObjectInputStream inputStream = null;
        try {
            //读
            inputStream = new ObjectInputStream(socket.getInputStream());
            Object obj = inputStream.readObject();
            System.out.println((String) obj);
            //写
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            String msg = "get " + obj;
            outputStream.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
