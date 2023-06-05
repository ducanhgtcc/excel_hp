/**
 * SendMTLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.example.onekids_project.integration.sms.neo;

public class SendMTLocator extends org.apache.axis.client.Service implements SendMT {

    public SendMTLocator() {
    }


    public SendMTLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SendMTLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SendMTHttpSoap11Endpoint
    private String SendMTHttpSoap11Endpoint_address = "http://g3g4.vn:8008/smsws/services/SendMT.SendMTHttpSoap11Endpoint/";

    @Override
	public String getSendMTHttpSoap11EndpointAddress() {
        return SendMTHttpSoap11Endpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private String SendMTHttpSoap11EndpointWSDDServiceName = "SendMTHttpSoap11Endpoint";

    public String getSendMTHttpSoap11EndpointWSDDServiceName() {
        return SendMTHttpSoap11EndpointWSDDServiceName;
    }

    public void setSendMTHttpSoap11EndpointWSDDServiceName(String name) {
        SendMTHttpSoap11EndpointWSDDServiceName = name;
    }

    @Override
	public SendMTPortType getSendMTHttpSoap11Endpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SendMTHttpSoap11Endpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSendMTHttpSoap11Endpoint(endpoint);
    }

    @Override
	public SendMTPortType getSendMTHttpSoap11Endpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SendMTSoap11BindingStub _stub = new SendMTSoap11BindingStub(portAddress, this);
            _stub.setPortName(getSendMTHttpSoap11EndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSendMTHttpSoap11EndpointEndpointAddress(String address) {
        SendMTHttpSoap11Endpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @Override
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SendMTPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                SendMTSoap11BindingStub _stub = new SendMTSoap11BindingStub(new java.net.URL(SendMTHttpSoap11Endpoint_address), this);
                _stub.setPortName(getSendMTHttpSoap11EndpointWSDDServiceName());
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
    @Override
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("SendMTHttpSoap11Endpoint".equals(inputPortName)) {
            return getSendMTHttpSoap11Endpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    @Override
	public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://sms.neo", "SendMT");
    }

    private java.util.HashSet ports = null;

    @Override
	public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://sms.neo", "SendMTHttpSoap11Endpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("SendMTHttpSoap11Endpoint".equals(portName)) {
            setSendMTHttpSoap11EndpointEndpointAddress(address);
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
