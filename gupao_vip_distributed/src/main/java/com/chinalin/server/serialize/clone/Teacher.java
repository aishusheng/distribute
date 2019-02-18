package com.chinalin.server.serialize.clone;

import java.io.Serializable;

/**
 * Created by robertai on 2019/2/14.
 */
public class Teacher implements Serializable {

    private static final long serialVersionUID = -7366654897948694182L;
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
        return "Teacher{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
