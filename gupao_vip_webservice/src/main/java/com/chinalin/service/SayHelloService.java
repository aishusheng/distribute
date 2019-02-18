package com.chinalin.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @description:
 * @date:2019/02/15 22:39
 * @author:艾书胜
 * 创建一个webservice接口
 */
@WebService
public interface SayHelloService {

    @WebMethod
    String sayHello(String name);
}
