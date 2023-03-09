package com.lgu.common.api;

public class ApiResponseVO {
	private CommonResVO common;
	private String resultCode;
	private String resultMsg;
	private Object resultData;
	
	public ApiResponseVO() {
		this.common = new CommonResVO();
	}
	public CommonResVO getCommon() {
		return common;
	}
	public void setCommon(CommonResVO common) {
		this.common = common;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public Object getResultData() {
		return resultData;
	}
	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	@Override
	public String toString() {
		return "ApiResponseVO [common=" + common + ", resultCode=" + resultCode + ", resultMsg=" + resultMsg
				+ ", resultData=" + resultData + "]";
	}
	
	
}
