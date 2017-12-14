package com.sunny.core.client.impl;

import com.alibaba.fastjson.JSON;
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
//        BIOClient client =  new BIOClient(8989);
//        client.start();

//        for (int i = 0; i < 100; i++) {
////            client.sendMsg("sunlijie >> " + i);
//            Thread.sleep(1000);
//        }

        Client cli = new BIOClient(8989);

        cli.start();

        //2.get proxy
        HelloWorld helloWorld = cli.getProxy(HelloWorld.class);

        //3.invoke
        for (int i = 0; i < 100; i++) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+i);
            String ret = helloWorld.say("sunlijie");

            System.out.println("ret : "+ret);
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
                        System.out.println(JSON.toJSONString("response : " + response.getValue()));
                        RpcContext rpcContext = transactionContainer.get(response.getRequestId());
                        rpcContext.setResponse(response);
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        System.out.println("cli receive end");
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
                    System.out.println("notify ");
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

        System.out.println("future start.");
        try {
            System.out.println("futureTask.get() : "+JSON.toJSONString(futureTask.get()));
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
                        System.out.println("send str:" + JSON.toJSONString(msg));
                        objectOutputStream.writeObject(msg);
                    }
                    System.out.println("msg = null");
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
