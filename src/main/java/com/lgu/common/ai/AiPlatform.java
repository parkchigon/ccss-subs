package com.lgu.common.ai;

public class AiPlatform {
	private AiCommon common;
	private AiBody body;
	
	public AiPlatform() {
		this.common = new AiCommon();
		this.body = new AiBody();
	}
	public AiCommon getAiCommonVo() {
		return common;
	}
	public void setAiCommonVo(AiCommon common) {
		this.common = common;
	}
	public AiBody getAiBodyVo() {
		return body;
	}
	public void setAiBodyVo(AiBody body) {
		this.body = body;
	}
}
