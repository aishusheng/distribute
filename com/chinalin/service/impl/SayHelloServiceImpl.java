
package com.chinalin.service.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SayHelloServiceImpl", targetNamespace = "http://impl.service.chinalin.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SayHelloServiceImpl {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://impl.service.chinalin.com/", className = "com.chinalin.service.impl.SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://impl.service.chinalin.com/", className = "com.chinalin.service.impl.SayHelloResponse")
    @Action(input = "http://impl.service.chinalin.com/SayHelloServiceImpl/sayHelloRequest", output = "http://impl.service.chinalin.com/SayHelloServiceImpl/sayHelloResponse")
    public String sayHello(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}
