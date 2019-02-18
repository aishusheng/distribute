package com.chinalin.server.socket.demo2;

import java.io.*;
import java.net.Socket;

/**
 * Created by robertai on 2019/2/14.
 * socket client客户端发送数据并接收服务器端数据
 * 单播
 */
public class SocketClient {

    public static void main(String[] args){
        Socket socket = null;
        BufferedReader reader=null;
        PrintWriter writer=null;
        try{
            socket = new Socket("127.0.0.1",8888);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//接收服务器端发送的数据
            writer = new PrintWriter(socket.getOutputStream(),true);//发送数据到服务器端
            writer.println("hello server test !");
            while(true){//接收服务器端发送的数据
                String serverData = reader.readLine();
                if(null==serverData){
                    break;
                }
                System.out.println("客户端接收到服务器端的数据:"+serverData);
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
            if(null != socket){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
