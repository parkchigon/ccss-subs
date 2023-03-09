package com.lgu.common.api;

public class ParamVO {
	
	private String serial;
	private String ctn;
	private String aiToken;
	private String ccssToken;
	private String isAgree;
	private String version;		// voideguide Param
	private Object body;
	private String esn;
	private String usimModel;
	private String usimSn;
	private String firmwareInfo;
	private String termsNo;
	
	public ParamVO() {
		body = new Object();
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getCtn() {
		return ctn;
	}
	public void setCtn(String ctn) {
		this.ctn = ctn;
	}
	public String getAiToken() {
		return aiToken;
	}
	public void setAiToken(String aiToken) {
		this.aiToken = aiToken;
	}
	public String getCcssToken() {
		return ccssToken;
	}
	public void setCcssToken(String ccssToken) {
		this.ccssToken = ccssToken;
	}
	public String getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEsn() {
		return esn;
	}
	public void setEsn(String esn) {
		this.esn = esn;
	}
	public String getUsimModel() {
		return usimModel;
	}
	public void setUsimModel(String usimModel) {
		this.usimModel = usimModel;
	}
	public String getUsimSn() {
		return usimSn;
	}
	public void setUsimSn(String usimSn) {
		this.usimSn = usimSn;
	}
	public String getFirmwareInfo() {
		return firmwareInfo;
	}
	public void setFirmwareInfo(String firmwareInfo) {
		this.firmwareInfo = firmwareInfo;
	}
	public String getTermsNo() {
		return termsNo;
	}
	public void setTermsNo(String termsNo) {
		this.termsNo = termsNo;
	}
	@Override
	public String toString() {
		return "ParamVO [serial=" + serial + ", ctn=" + ctn + ", aiToken=" + aiToken + ", ccssToken=" + ccssToken
				+ ", isAgree=" + isAgree + ", version=" + version + ", body=" + body + ", esn=" + esn + ", usimModel="
				+ usimModel + ", usimSn=" + usimSn + ", firmwareInfo=" + firmwareInfo + ", termsNo=" + termsNo + "]";
	}
	
	
}
