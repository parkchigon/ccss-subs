package com.lgu.common.api;

public class ApiRequestVO {
	private CommonReqVO common;
	private ParamVO param;
	
	public ApiRequestVO(){
		this.common = new CommonReqVO();
		this.param = new ParamVO();
	}
	public CommonReqVO getCommon() {
		return common;
	}
	public void setCommon(CommonReqVO common) {
		this.common = common;
	}
	public ParamVO getParam() {
		return param;
	}
	public void setParam(ParamVO param) {
		this.param = param;
	}
	
	@Override
	public String toString() {
		return "ApiRequestVO [common=" + common + ", param=" + param + "]";
	}
}
