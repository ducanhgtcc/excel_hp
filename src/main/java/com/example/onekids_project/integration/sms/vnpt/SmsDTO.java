package com.example.onekids_project.integration.sms.vnpt;

public class SmsDTO {
	
	private String username;
	private String password;
	private String serviceId;
	private String userId;
	private String infor;
	private String contentType;
	private String serviceKind;
	private int dataCoding;
	private String list_msisdnd;
	
	
	public SmsDTO(String username, String password, String serviceId, String userId, String infor, String contentType,
			String serviceKind, int dataCoding, String list_msisdnd) {
		super();
		this.username = username;
		this.password = password;
		this.serviceId = serviceId;
		this.userId = userId;
		this.infor = infor;
		this.contentType = contentType;
		this.serviceKind = serviceKind;
		this.dataCoding = dataCoding;
		this.list_msisdnd = list_msisdnd;
	}
	public String getList_msisdnd() {
		return list_msisdnd;
	}
	public void setList_msisdnd(String list_msisdnd) {
		this.list_msisdnd = list_msisdnd;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInfor() {
		return infor;
	}
	public void setInfor(String infor) {
		this.infor = infor;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getServiceKind() {
		return serviceKind;
	}
	public void setServiceKind(String serviceKind) {
		this.serviceKind = serviceKind;
	}
	public int getDataCoding() {
		return dataCoding;
	}
	public void setDataCoding(int dataCoding) {
		this.dataCoding = dataCoding;
	}
	public SmsDTO(String username, String password, String serviceId, String userId, String infor, String contentType,
			String serviceKind, int dataCoding) {
		super();
		this.username = username;
		this.password = password;
		this.serviceId = serviceId;
		this.userId = userId;
		this.infor = infor;
		this.contentType = contentType;
		this.serviceKind = serviceKind;
		this.dataCoding = dataCoding;
	}
	public SmsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "SmsDTO [username=" + username + ", password=" + password + ", serviceId=" + serviceId + ", userId="
				+ userId + ", infor=" + infor + ", contentType=" + contentType + ", serviceKind=" + serviceKind
				+ ", dataCoding=" + dataCoding + "]";
	}
	
	
}
