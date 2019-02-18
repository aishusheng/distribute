package com.chinalin.server.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by robertai on 2019/2/15.
 * RMI发布服务
 */
public class TestServer {

    public static void main(String[] args){
        try {
            SayHelloService service = new SayHelloServiceImpl();
            LocateRegistry.createRegistry(8888);//给指定端口注册远程服务
            Naming.bind("rmi://localhost:8888/sayHello",service);//把服务绑定到指定网址
            System.out.println("server start success");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
