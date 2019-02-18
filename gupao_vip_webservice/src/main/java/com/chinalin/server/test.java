package com.chinalin.server;

import com.chinalin.service.impl.SayHelloServiceImpl;

import javax.xml.ws.Endpoint;

/**
 * @description:
 * @date:2019/02/15 22:43
 * @author:艾书胜
 * 启动类去发布webservice服务
 */
public class test {

    public static void main(String[] args){
        //然后在浏览器中输入http://localhost:8888/test/sayHello?wsdl回车即可看到一个wsdl文档
        Endpoint.publish("http://localhost:8888/test/sayHello",new SayHelloServiceImpl());//发布，

        System.out.println("webservice publish success");
    }
}
