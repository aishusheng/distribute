
package com.chinalin.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SayHelloServiceImplService", targetNamespace = "http://impl.service.chinalin.com/", wsdlLocation = "http://localhost:8888/test/sayHello?wsdl")
public class SayHelloServiceImplService
    extends Service
{

    private final static URL SAYHELLOSERVICEIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException SAYHELLOSERVICEIMPLSERVICE_EXCEPTION;
    private final static QName SAYHELLOSERVICEIMPLSERVICE_QNAME = new QName("http://impl.service.chinalin.com/", "SayHelloServiceImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8888/test/sayHello?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SAYHELLOSERVICEIMPLSERVICE_WSDL_LOCATION = url;
        SAYHELLOSERVICEIMPLSERVICE_EXCEPTION = e;
    }

    public SayHelloServiceImplService() {
        super(__getWsdlLocation(), SAYHELLOSERVICEIMPLSERVICE_QNAME);
    }

    public SayHelloServiceImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SAYHELLOSERVICEIMPLSERVICE_QNAME, features);
    }

    public SayHelloServiceImplService(URL wsdlLocation) {
        super(wsdlLocation, SAYHELLOSERVICEIMPLSERVICE_QNAME);
    }

    public SayHelloServiceImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SAYHELLOSERVICEIMPLSERVICE_QNAME, features);
    }

    public SayHelloServiceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SayHelloServiceImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SayHelloServiceImpl
     */
    @WebEndpoint(name = "SayHelloServiceImplPort")
    public SayHelloServiceImpl getSayHelloServiceImplPort() {
        return super.getPort(new QName("http://impl.service.chinalin.com/", "SayHelloServiceImplPort"), SayHelloServiceImpl.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SayHelloServiceImpl
     */
    @WebEndpoint(name = "SayHelloServiceImplPort")
    public SayHelloServiceImpl getSayHelloServiceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl.service.chinalin.com/", "SayHelloServiceImplPort"), SayHelloServiceImpl.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SAYHELLOSERVICEIMPLSERVICE_EXCEPTION!= null) {
            throw SAYHELLOSERVICEIMPLSERVICE_EXCEPTION;
        }
        return SAYHELLOSERVICEIMPLSERVICE_WSDL_LOCATION;
    }

}
