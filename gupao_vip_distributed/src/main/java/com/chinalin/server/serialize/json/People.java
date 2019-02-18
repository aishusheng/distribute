package com.chinalin.server.serialize.json;

import java.io.Serializable;

/**
 * @description:创建一个需要序列号的类
 * @date:2019/02/14 21:40
 * @author:艾书胜
 */
public class People implements Serializable {

    private static final long serialVersionUID = -1152830315281959158L;
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

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
