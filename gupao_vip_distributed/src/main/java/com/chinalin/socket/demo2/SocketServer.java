package com.chinalin.socket.demo2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by robertai on 2019/2/14.
 * socket server接收并发送客户端数据
 * 单播
 */
public class SocketServer {

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(8888);//启动一个服务
            while (true){
                Socket socket = serverSocket.accept();//等待接收请求
                new Thread(()-> {
                    BufferedReader reader=null;
                    PrintWriter writer=null;
                    try{
                        //读取客户端数据
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //给客户端发送数据
                        writer = new PrintWriter(socket.getOutputStream(),true);
                        while(true){
                            String clientData = reader.readLine();
                            if(null==clientData){
                                break;
                            }
                            System.out.println("服务器端接收到的数据:"+clientData);
                            writer.println("服务器端已经接收到客户端发送的数据");//返回数据给客户端
                            writer.flush();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if(null != reader){
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(null != writer){
                            writer.close();
                        }
                    }

                }).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != serverSocket){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
