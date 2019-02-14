package com.chinalin.serialize.clone;

/**
 * Created by robertai on 2019/2/14.
 * 使用序列化来实现深度克隆的测试类
 */
public class Test {

    public static void main(String[] args){

        Teacher t = new Teacher();
        t.setName("zhangsan");
        t.setAge(30);

        Student s = new Student();
        s.setTeacher(t);
        s.setName("lisi");
        s.setAge(18);
        System.out.println("原对象:"+s.toString());
        Student student=(Student)s.deepClone();//使用序列化深度克隆一个对象
        student.getTeacher().setName("wangwu");
        System.out.println("克隆对象:"+student);
    }
}
