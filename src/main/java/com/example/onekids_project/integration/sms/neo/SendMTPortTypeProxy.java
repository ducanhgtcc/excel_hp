package com.example.onekids_project.integration.sms.neo;

public class SendMTPortTypeProxy implements SendMTPortType {
  private String _endpoint = null;
  private SendMTPortType sendMTPortType = null;
  
  public SendMTPortTypeProxy() {
    _initSendMTPortTypeProxy();
  }
  
  public SendMTPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initSendMTPortTypeProxy();
  }
  
  private void _initSendMTPortTypeProxy() {
    try {
      sendMTPortType = (new SendMTLocator()).getSendMTHttpSoap11Endpoint();
      if (sendMTPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sendMTPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sendMTPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sendMTPortType != null)
      ((javax.xml.rpc.Stub)sendMTPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public SendMTPortType getSendMTPortType() {
    if (sendMTPortType == null)
      _initSendMTPortTypeProxy();
    return sendMTPortType;
  }
  
  @Override
public String insertSMS(String username, String password, String receiver, String content, Integer loaisp, String brandname, Integer isUnicode, String target) throws java.rmi.RemoteException{
    if (sendMTPortType == null)
      _initSendMTPortTypeProxy();
    return sendMTPortType.insertSMS(username, password, receiver, content, loaisp, brandname, isUnicode, target);
  }
  
  @Override
public String sendFromServiceNumber(String username, String password, String receiver, String content, String target) throws java.rmi.RemoteException{
    if (sendMTPortType == null)
      _initSendMTPortTypeProxy();
    return sendMTPortType.sendFromServiceNumber(username, password, receiver, content, target);
  }
  
  @Override
public String sendMessage(String username, String password, String receiver, String content, Integer repeat, Integer repeatType, Integer loaisp, String brandname, String timestart, String timeend, String timesend, String target) throws java.rmi.RemoteException{
    if (sendMTPortType == null)
      _initSendMTPortTypeProxy();
    return sendMTPortType.sendMessage(username, password, receiver, content, repeat, repeatType, loaisp, brandname, timestart, timeend, timesend, target);
  }
  
  @Override
public String sendFromBrandname(String username, String password, String receiver, String brandname, String content, String target) throws java.rmi.RemoteException{
    if (sendMTPortType == null)
      _initSendMTPortTypeProxy();
    return sendMTPortType.sendFromBrandname(username, password, receiver, brandname, content, target);
  }
  
  @Override
public String useCard(String username, String password, String issure, String cardCode, String cardSerial) throws java.rmi.RemoteException{
    if (sendMTPortType == null)
      _initSendMTPortTypeProxy();
    return sendMTPortType.useCard(username, password, issure, cardCode, cardSerial);
  }
  
  @Override
public String sendSMS(String username, String password, String receiver, String content, Integer loaisp, String brandname, String target) throws java.rmi.RemoteException{
    if (sendMTPortType == null)
      _initSendMTPortTypeProxy();
    return sendMTPortType.sendSMS(username, password, receiver, content, loaisp, brandname, target);
  }
  
  
}