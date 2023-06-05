/**
 * SendMTPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.example.onekids_project.integration.sms.neo;

public interface SendMTPortType extends java.rmi.Remote {
    public String insertSMS(String username, String password, String receiver, String content, Integer loaisp, String brandname, Integer isUnicode, String target) throws java.rmi.RemoteException;
    public String sendFromServiceNumber(String username, String password, String receiver, String content, String target) throws java.rmi.RemoteException;
    public String sendMessage(String username, String password, String receiver, String content, Integer repeat, Integer repeatType, Integer loaisp, String brandname, String timestart, String timeend, String timesend, String target) throws java.rmi.RemoteException;
    public String sendFromBrandname(String username, String password, String receiver, String brandname, String content, String target) throws java.rmi.RemoteException;
    public String useCard(String username, String password, String issure, String cardCode, String cardSerial) throws java.rmi.RemoteException;
    public String sendSMS(String username, String password, String receiver, String content, Integer loaisp, String brandname, String target) throws java.rmi.RemoteException;
}
