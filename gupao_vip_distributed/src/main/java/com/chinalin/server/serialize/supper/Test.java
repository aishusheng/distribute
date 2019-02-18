package com.chinalin.server.serialize.supper;

import java.io.*;

/**
 * Created by robertai on 2019/2/14.
 */
public class Test {

    public static void main(String[] args){
        userSerialize();
        deUserSerialize();
    }

    //序列化
    public static void userSerialize(){

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("user")));
            User user = new User();
            user.setName("aishusheng");
            user.setAge(18);
            oos.writeObject(user);
            System.out.println("序列化完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //反序列化
    public static void deUserSerialize(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("user")));
            User user = (User)ois.readObject();
            System.out.println(user.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
