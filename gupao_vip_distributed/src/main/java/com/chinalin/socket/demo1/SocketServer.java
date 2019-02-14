package com.chinalin.socket.demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by robertai on 2019/2/14.
 * socket server端
 */
public class SocketServer {

    public static void main(String[] args){
        ServerSocket serverSocket=null;
        try{
            serverSocket = new ServerSocket(8888);//启动一个socket服务
            Socket socket = serverSocket.accept();//等待一个接收请求
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//读数据
            System.out.println(reader.readLine());
            reader.close();
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
