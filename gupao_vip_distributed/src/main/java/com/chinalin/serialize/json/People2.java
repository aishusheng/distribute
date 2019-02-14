package com.chinalin.serialize.json;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.Serializable;

/**
 * @description:创建一个需要序列号的类
 * @date:2019/02/14 21:40
 * @author:艾书胜
 */
public class People2 implements Serializable {
    private static final long serialVersionUID = -2737328247533896202L;

    //注意，使用百度封装Google的protobuf来实现序列化，需要在成员变量上添加@Protobuf注解

    @Protobuf(fieldType= FieldType.STRING)
    private String name;

    @Protobuf(fieldType = FieldType.INT32)
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
        return "People2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
