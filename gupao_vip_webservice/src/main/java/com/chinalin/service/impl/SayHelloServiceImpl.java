package com.chinalin.service.impl;

import com.chinalin.service.SayHelloService;

import javax.jws.WebService;

/**
 * @description:
 * @date:2019/02/15 23:02
 * @author:艾书胜
 */
@WebService
public class SayHelloServiceImpl implements SayHelloService {
    @Override
    public String sayHello(String name) {
        System.out.println("say hello webservice method");
        return "hello world "+name;
    }
}
