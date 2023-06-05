package com.example.onekids_project.integration.sms.neo;

import java.net.URL;

public class NeoClient {

	public static void main(String[] args) {

		testNeo();
	}


	
	public static void testNeo() {
		try {
			SendMTSoap11BindingStub st = new SendMTSoap11BindingStub(new URL("http://g3g4.vn/smsws/services/SendMT?wsdl"), null);
			//String rs = st.insertSMS("tienphong_pm", "wpstoj", "0396928825", "noi dung tin nhan", 2, "Solienlac", 0, String.valueOf(System.currentTimeMillis()));
			String rs = st.insertSMS("tienphong_pm", "wpstoj", "0396928825", "Thi test noi dung tin nhan", 2, "Solienlac", 0, String.valueOf(System.currentTimeMillis()));
			
			System.out.println(rs);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void ogirin() {
		try {
			SendMTSoap11BindingStub st = new SendMTSoap11BindingStub(new URL("http://g3g4.vn/smsws/services/SendMT?wsdl"), null);
			String rs = st.insertSMS("username", "passs", "0906078658", "noi dung tin nhan", 2, "NEOJSC", 0, String.valueOf(System.currentTimeMillis()));
			System.out.println(rs);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
