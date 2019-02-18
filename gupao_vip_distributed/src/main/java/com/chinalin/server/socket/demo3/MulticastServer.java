package com.chinalin.server.socket.demo3;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.TimeUnit;

/**
 * Created by robertai on 2019/2/14.
 * 组播服务端
 */
public class MulticastServer {

    public static void main(String[] args){
        try {
            //组播的IP地址段范围:224.0.0.0-239.255.255.255
            InetAddress group = InetAddress.getByName("224.5.6.7");//定义一个组
            MulticastSocket socket = new MulticastSocket();
            for(int i=0;i<10;i++){
                String data ="hello multicast "+i;
                byte[] bytes = data.getBytes();
                socket.send(new DatagramPacket(bytes,bytes.length,group,8888));
                TimeUnit.SECONDS.sleep(2);//等待2秒钟
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
