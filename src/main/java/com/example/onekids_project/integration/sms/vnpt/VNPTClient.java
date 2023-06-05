package com.example.onekids_project.integration.sms.vnpt;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.axis.AxisFault;

public class VNPTClient {
	public static void main(String[] args) {
		try {
			BrandNameWSPortBindingStub brandNameWSPortBindingStub = new BrandNameWSPortBindingStub(new URL("http://123.29.69.74:8088/BrandNameWS/BrandNameWS"), null);
			try {
				brandNameWSPortBindingStub.uploadMultiSMS("onegroup", "6f2e3319b34b33cd0937c0c9b4abac92", "THDAIMO", "0396928825", "0", "0", "Test guu tin nhan", 0);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
