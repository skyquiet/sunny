package com.sunny.core.client.impl;

import com.sunny.core.client.Client;
import com.sunny.core.client.TransactionContainer;
import com.sunny.core.proxy.ProxyFactory;
import com.sunny.core.proxy.RefererInvocationHandler;
import com.sunny.core.proxy.impl.JdkProxyFactory;
import com.sunny.core.rpc.Request;
import com.sunny.core.rpc.Response;
import com.sunny.core.rpc.RpcContext;
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
                    // TODO: 2017/12/15 思考
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();
    }

    /**
     * 同步rpc请求
     *
     * @param msg
     * @return
     */
    public Response send(Request msg) {

        long requestId = msg.getRequestId();
        RpcContext rpcContext = new RpcContext();

        rpcContext.setRequest(msg);
        transactionContainer.put(requestId, rpcContext);
        sendMsgQueue.add(msg);


        //if method async ，需要从配置里面解析，这个留到做spring支持的时候做

        boolean isAsync = true;

        if (isAsync) {

            //返回一个包装类，然后在包装类上面设置callback

            //添加@Asyncable注解

            //解析注解，生成异步接口方法，异步调用的时候支持添加callback
        } else {
            //同步调用

        }


        //future
        Callable<Response> callable = new Callable<Response>() {
            @Override
            public Response call() {
                synchronized (lock) {
                    while (true) {
                        if (transactionContainer.get(requestId).getResponse() != null) {
                            Response response = (Response) transactionContainer.get(requestId).getResponse();
                            //从容器中删除
                            transactionContainer.remove(requestId);
                            //返回客户端
                            return response;
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

        /**
         * 如果是异步调用的话，这里把future返回回去就可以，让客户端也用FutureTask<Response> 来做returnType
         * todo 可以使用 ListenableFuture  这个东西应该是google 的
         * dubbo 的异步请求确实好繁琐
         * 参考 ：http://www.10tiao.com/html/240/201604/2649254387/1.html
         */
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
