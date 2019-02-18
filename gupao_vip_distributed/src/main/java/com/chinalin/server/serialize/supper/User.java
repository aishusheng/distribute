package com.chinalin.server.serialize.supper;

import java.io.Serializable;

/**
 * Created by robertai on 2019/2/14.
 * 创建一个子类，子类实现Serializable接口
 */
public class User extends UserSupper implements Serializable {

    @Override
    public String toString() {
        return "User{name="+getName()+",age="+getAge()+"}";
    }
}
