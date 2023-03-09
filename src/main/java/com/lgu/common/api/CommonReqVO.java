package com.lgu.common.api;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotEmpty;

public class CommonReqVO{
	
	@NotEmpty
	private String apiId;
	private String ccssToken;
	private DeviceVO device;
	
	private LogData logData;
	
	public CommonReqVO() {
		this.logData = new LogData();
		this.device = new DeviceVO();
	}
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getCcssToken() {
		return ccssToken;
	}
	public void setCcssToken(String ccssToken) {
		this.ccssToken = ccssToken;
	}
	public DeviceVO getDevice() {
		return device;
	}
	public void setDevice(DeviceVO device) {
		this.device = device;
	}
	public LogData getLogData() {
		return logData;
	}
	public void setLogData(LogData logData) {
		this.logData = logData;
	}
	
	@Override
	public String toString() {
		return "CommonReqVO [apiId=" + apiId + ", ccssToken=" + ccssToken + ", device=" + device + ", logData="
				+ logData + "]";
	}
}