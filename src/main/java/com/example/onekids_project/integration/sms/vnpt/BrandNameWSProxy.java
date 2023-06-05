package com.example.onekids_project.integration.sms.vnpt;

public class BrandNameWSProxy implements BrandNameWS {
  private String _endpoint = null;
  private BrandNameWS brandNameWS = null;

  public BrandNameWSProxy() {
    _initBrandNameWSProxy();
  }

  public BrandNameWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initBrandNameWSProxy();
  }

  private void _initBrandNameWSProxy() {
    try {
      brandNameWS = (new BrandNameWSServiceLocator()).getBrandNameWSPort();
      if (brandNameWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)brandNameWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)brandNameWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }

    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }

  public String getEndpoint() {
    return _endpoint;
  }

  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (brandNameWS != null)
      ((javax.xml.rpc.Stub)brandNameWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

  }

  public BrandNameWS getBrandNameWS() {
    if (brandNameWS == null)
      _initBrandNameWSProxy();
    return brandNameWS;
  }

  public long uploadSMS(String username, String password, String serviceId, String userId, String contentType, String serviceKind, String infor, int dataCoding) throws java.rmi.RemoteException{
    if (brandNameWS == null)
      _initBrandNameWSProxy();
    return brandNameWS.uploadSMS(username, password, serviceId, userId, contentType, serviceKind, infor, dataCoding);
  }

  public String checkSMS(String username, String password, long requestId) throws java.rmi.RemoteException{
    if (brandNameWS == null)
      _initBrandNameWSProxy();
    return brandNameWS.checkSMS(username, password, requestId);
  }

  public UploadMultySmsResponse uploadMultiSMS(String username, String password, String serviceId, String list_msisdn, String contentType, String serviceKind, String infor, int dataCoding) throws java.rmi.RemoteException{
    if (brandNameWS == null)
      _initBrandNameWSProxy();
    return brandNameWS.uploadMultiSMS(username, password, serviceId, list_msisdn, contentType, serviceKind, infor, dataCoding);
  }
  
  
}