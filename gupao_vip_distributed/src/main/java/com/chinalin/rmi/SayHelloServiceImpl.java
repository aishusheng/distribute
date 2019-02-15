package com.chinalin.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by robertai on 2019/2/15.
 */
public class SayHelloServiceImpl extends UnicastRemoteObject implements SayHelloService {

    public SayHelloServiceImpl() throws RemoteException {

    }

    public String sayHello(String name) throws RemoteException {

        return "hello world "+name;
    }
}
