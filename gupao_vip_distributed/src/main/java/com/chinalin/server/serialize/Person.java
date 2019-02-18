package com.chinalin.server.serialize;

import java.beans.Transient;
import java.io.Serializable;

/**
 * Created by robertai on 2019/2/14.
 * 创建一个待序列化的类
 */
public class Person implements Serializable {

    //下面这个id是序列化和反序列化需要使用的，反序列化的时候需要用到这个来进行对比,保证对象是同一个
    private static final long serialVersionUID = -8468664920927547683L;

    //序列化会忽略静态变量
    public static int height=2;

    //transient关键字修饰的变量不会参与序列化，也就是说，反序列话之后该变量为null
    private transient String address;

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
