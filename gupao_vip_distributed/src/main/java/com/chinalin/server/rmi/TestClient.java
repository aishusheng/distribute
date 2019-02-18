package com.chinalin.server.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by robertai on 2019/2/15.
 * RMI客户端测试
 */
public class TestClient {

    public static void main(String[] args){
        try {
            SayHelloService service = (SayHelloService) Naming.lookup("rmi://localhost:8888/sayHello");
            System.out.println(service.sayHello("aishusheng"));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
