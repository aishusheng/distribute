package com.chinalin.serialize.json;

import com.alibaba.fastjson.JSON;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description:使用json来实现序列号
 * @date:2019/02/14 21:38
 * @author:艾书胜
 */
public class TestJson {

    //初始化要序列化的对象
    private static People init(){
        People people = new People();
        people.setName("aishusheng");
        people.setAge(18);
        return people;
    }

    private static People2 init2(){
        People2 people2 = new People2();
        people2.setName("aishusheng");
        people2.setAge(18);
        return people2;
    }

    public static void main(String[] args){
        executeWithjackson();
        executeWithFastjson();
        executeWithProtobuf();
        executeWithHessian();
    }

    //使用jackson包中的json来序列号对象
    private static void executeWithjackson(){
        System.out.println("==========使用jackson包中的json实现序列化");
        People people = init();//初始化对象
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = null;
        long start = System.currentTimeMillis();
        try {
            //序列化
            bytes = mapper.writeValueAsBytes(people);//把待序列化的对象写入到字节数组中
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("jack序列化:"+(end-start)+"ms:"+",序列号之后总大小:"+bytes.length);

        //反序列化
        try {
            People p=mapper.readValue(bytes,People.class);
            System.out.println("反序列化对象信息:"+p.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //使用alibaba的fastjson中的json来实现序列化
    private static void executeWithFastjson(){
        System.out.println("==========使用alibaba的fastjson包中的json实现序列化");
        People p = init();
        String text = null;
        long start = System.currentTimeMillis();
        text=JSON.toJSONString(p);//序列化
        long end = System.currentTimeMillis();
        System.out.println("fastjson序列化："+(end-start)+"ms,序列化大小:"+text.getBytes().length);

        //反序列化
        People people=JSON.parseObject(text,People.class);
        System.out.println("反序列化信息:"+people.toString());
    }

    //使用protobuf来实现序列化，这个序列化的性能比上面两个使用json序列化的性能要高
    //性能高的原因是：1、对字节进行了压缩，在网络上传输速度快，2、做了缓存处理，
    //但是protobuf序列化所花的时间比较长
    private static void executeWithProtobuf(){
        System.out.println("==========使用protobuf实现序列化");
        People2 p = init2();
        Codec<People2> codec = ProtobufProxy.create(People2.class,false);
        byte[] bytes=null;
        long start = System.currentTimeMillis();
        try {
            bytes=codec.encode(p);//序列化
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("protobuf序列化:"+(end-start)+"ms,大小:"+bytes.length);

        //反序列化
        try {
            People2 p2=codec.decode(bytes);
            System.out.println("反序列化信息:"+p2.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //使用hessian来实现序列化
    private static void executeWithHessian(){
        System.out.println("==========使用hessian实现序列化");
        People p = init();
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        HessianOutput hop = new HessianOutput(bos);
        long start=System.currentTimeMillis();
        try {
            hop.writeObject(p);//序列化
            long end = System.currentTimeMillis();
            System.out.println("hessian序列化:"+(end-start)+"ms,大小:"+bos.toByteArray().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //反序列化
        HessianInput hip = new HessianInput(new ByteArrayInputStream(bos.toByteArray()));
        try {
            People people=(People) hip.readObject();
            System.out.println("反序列化信息:"+people.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
