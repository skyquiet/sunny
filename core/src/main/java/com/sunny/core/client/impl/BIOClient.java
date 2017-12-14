package com.sunny.core.client.impl;

import com.sunny.core.client.Client;
import com.sunny.core.client.TransactionContainer;
import com.sunny.core.proxy.ProxyFactory;
import com.sunny.core.proxy.RefererInvocationHandler;
import com.sunny.core.proxy.impl.JdkProxyFactory;
import com.sunny.core.rpc.Request;
import com.sunny.core.rpc.Response;
import com.sunny.core.rpc.RpcContext;
import com.sunny.core.service.HelloWorld;
import com.sunny.core.utils.SocketUtil;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BIO core 客户端
 *
 * @author: sunlijie
 * CreateDate: 2017/12/5 16:39
 */

public class BIOClient implements Client {

    private final TransactionContainer transactionContainer = new TransactionContainer();

    private Object lock = new Object();
    private int port;
    private Socket socket;
    private LinkedBlockingQueue<Request> sendMsgQueue = new LinkedBlockingQueue();

    //todo 可以用spring 的注入
    private ProxyFactory proxyFactory = new JdkProxyFactory();

    public BIOClient(int port) throws IOException {
        this.port = port;
        this.socket = new Socket(SocketUtil.getIP(), port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        for (int j = 0; j < 2; j++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Client cli = null;
                    try {
                        cli = new BIOClient(8989);
                        cli.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //2.get proxy
                    HelloWorld helloWorld = cli.getProxy(HelloWorld.class);

                    //3.invoke
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < 10000; i++) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + i);
                        String ret = helloWorld.say("sunlijie " + i);

                        System.out.println(Thread.currentThread().getName()+" ret : " + ret);
                    }

                    System.out.println(Thread.currentThread().getName()+" cost time : " + (System.currentTimeMillis() - start));
                }
            }).start();
            System.out.println(j+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

    }

    public void start() throws IOException, InterruptedException {
        read();
        writeListener();
        System.out.println(this.getClass().getName() + " started!");
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
                        Response response = (Response) obj;
                        RpcContext rpcContext = transactionContainer.get(response.getRequestId());
                        rpcContext.setResponse(response);
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();
    }

//    public void sendMsg(String msg){
//        sendMsgQueue.add(msg);
//    }

    public Response send(Request msg) {

        long requestId = msg.getRequestId();
        RpcContext rpcContext = new RpcContext();

        rpcContext.setRequest(msg);
        transactionContainer.put(requestId, rpcContext);
        sendMsgQueue.add(msg);

        //future
        Callable<Response> callable = new Callable<Response>() {
            @Override
            public Response call() {
                synchronized (lock) {
                    while (true) {
                        if (transactionContainer.get(requestId).getResponse() != null) {
                            return transactionContainer.get(requestId).getResponse();
                        } else {
                            try {
                                synchronized (lock) {
                                    lock.wait();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        };

        FutureTask<Response> futureTask = new FutureTask<Response>(callable);

        Thread t = new Thread(futureTask);
        t.start();

        try {
            return futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T getProxy(Class<T> clz) {
        InvocationHandler invocationHandler = new RefererInvocationHandler(clz, this);
        return proxyFactory.getProxy(clz, invocationHandler);
    }

    public void writeListener() throws IOException, InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    Object msg;
                    while ((msg = sendMsgQueue.take()) != null) {
                        objectOutputStream.writeObject(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();


    }

}
