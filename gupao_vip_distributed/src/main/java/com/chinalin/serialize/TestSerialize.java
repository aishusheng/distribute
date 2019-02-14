package com.chinalin.serialize;

import java.io.*;

/**
 * Created by robertai on 2019/2/14.
 * 序列化和反序列化操作类
 */
public class TestSerialize {

    public static void main(String[] args){
        SerializePerson();
        Person.height=5;//修改静态变量的值，然后再反序列化
        DeSerialziePerson();
    }

    /**
     * 序列化一个对象
     */
    public static void SerializePerson(){
        try {
            //当前序列化的数据会保存到当前项目的根目录下的person文件中
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("person")));
            Person person = new Person();
            person.setName("aishusheng");
            person.setAge(18);
            person.setAddress("广东省");
            oos.writeObject(person);
            System.out.println("序列化完成");
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化操作
     */
    public static void DeSerialziePerson(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("person")));
            Person person =(Person) ois.readObject();
            System.out.println("反序列化的对象信息:"+person.toString());
            //这里静态变量的值是修改之后的值，序列化并不保证静态变量的状态
            System.out.println("序列化之后的静态变量的值:"+person.height);
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
