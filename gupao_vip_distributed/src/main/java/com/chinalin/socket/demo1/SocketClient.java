package com.chinalin.socket.demo1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by robertai on 2019/2/14.
 * socket client客户端
 */
public class SocketClient {

    public static void main(String[] args){
        try {
            Socket socket = new Socket("127.0.0.1",8888);
            PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);//写数据并自动清空缓冲区
            writer.println("hello socket test!");
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
