package com.chinalin.socket.demo3;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by robertai on 2019/2/14.
 * 组播客户端
 */
public class MulticastClient {

    public static void main(String[] args){
        try {
            InetAddress group = InetAddress.getByName("224.5.6.7");//定义一个组，这个组必须与服务器端一致
            MulticastSocket socket = new MulticastSocket(8888);
            socket.joinGroup(group);//加入指定的组
            byte[] buff = new byte[256];
            while(true){
                DatagramPacket msgPackage=new DatagramPacket(buff,buff.length);
                socket.receive(msgPackage);//接收数据
                String msg = new String(msgPackage.getData());
                System.out.println("接收服务器端传递的数据:"+msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
