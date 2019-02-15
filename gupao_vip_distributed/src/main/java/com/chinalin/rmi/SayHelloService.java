package com.chinalin.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by robertai on 2019/2/15.
 * 创建一个接口，用于RMI测试
 */
public interface SayHelloService extends Remote {

    public abstract String sayHello(String name) throws RemoteException;
}
