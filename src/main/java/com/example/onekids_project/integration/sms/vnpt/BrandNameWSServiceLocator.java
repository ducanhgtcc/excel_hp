/**
 * BrandNameWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.example.onekids_project.integration.sms.vnpt;

public class BrandNameWSServiceLocator extends org.apache.axis.client.Service implements BrandNameWSService {

    public BrandNameWSServiceLocator() {
    }


    public BrandNameWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BrandNameWSServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BrandNameWSPort
    private String BrandNameWSPort_address = "http://123.29.69.74:8086/BrandNameWS/BrandNameWS";

    public String getBrandNameWSPortAddress() {
        return BrandNameWSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String BrandNameWSPortWSDDServiceName = "BrandNameWSPort";

    public String getBrandNameWSPortWSDDServiceName() {
        return BrandNameWSPortWSDDServiceName;
    }

    public void setBrandNameWSPortWSDDServiceName(String name) {
        BrandNameWSPortWSDDServiceName = name;
    }

    public BrandNameWS getBrandNameWSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BrandNameWSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBrandNameWSPort(endpoint);
    }

    public BrandNameWS getBrandNameWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            BrandNameWSPortBindingStub _stub = new BrandNameWSPortBindingStub(portAddress, this);
            _stub.setPortName(getBrandNameWSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBrandNameWSPortEndpointAddress(String address) {
        BrandNameWSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (BrandNameWS.class.isAssignableFrom(serviceEndpointInterface)) {
                BrandNameWSPortBindingStub _stub = new BrandNameWSPortBindingStub(new java.net.URL(BrandNameWSPort_address), this);
                _stub.setPortName(getBrandNameWSPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("BrandNameWSPort".equals(inputPortName)) {
            return getBrandNameWSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://sms.mc.vasc.com/", "BrandNameWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://sms.mc.vasc.com/", "BrandNameWSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("BrandNameWSPort".equals(portName)) {
            setBrandNameWSPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
