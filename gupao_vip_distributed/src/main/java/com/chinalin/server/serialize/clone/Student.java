package com.chinalin.server.serialize.clone;

import java.io.*;

/**
 * Created by robertai on 2019/2/14.
 */
public class Student implements Serializable {

    private static final long serialVersionUID = -6915030137602006549L;
    private String name;
    private int age;

    private Teacher teacher;

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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", teacher={" + teacher.toString() +"}" +
                '}';
    }

    //通过序列化实现深度克隆
    public Object deepClone(){
        try {
            //序列化
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);//把当前对象写入到字节数组中
            //反序列化
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());//获取字节数组中的序列化信息
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
