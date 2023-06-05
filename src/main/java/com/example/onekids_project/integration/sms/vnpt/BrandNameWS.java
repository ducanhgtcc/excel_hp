/**
 * BrandNameWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.example.onekids_project.integration.sms.vnpt;

public interface BrandNameWS extends java.rmi.Remote {
    public long uploadSMS(String username, String password, String serviceId, String userId, String contentType, String serviceKind, String infor, int dataCoding) throws java.rmi.RemoteException;
    public String checkSMS(String username, String password, long requestId) throws java.rmi.RemoteException;
    public UploadMultySmsResponse uploadMultiSMS(String username, String password, String serviceId, String list_msisdn, String contentType, String serviceKind, String infor, int dataCoding) throws java.rmi.RemoteException;
}
