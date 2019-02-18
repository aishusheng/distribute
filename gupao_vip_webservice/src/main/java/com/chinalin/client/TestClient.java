package com.chinalin.client;

/**
 * @description:
 * @date:2019/02/15 23:10
 * @author:艾书胜
 */
public class TestClient {

    public static void main(String[] args){
        SayHelloServiceImplService service = new SayHelloServiceImplService();
        SayHelloServiceImpl impl=service.getSayHelloServiceImplPort();
        String result = impl.sayHello("aishusheng");
        System.out.println("webService client result:"+result);
    }
}
