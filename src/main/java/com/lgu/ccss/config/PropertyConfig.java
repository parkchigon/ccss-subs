package com.lgu.ccss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfig {
		
	@Value("#{config['esb.expiredsms.duration']}")
	private String expSMSDuration;

	@Value("#{config['esb.expiredsms.sendtime']}")
	private String expSMSSendTime;
	
	@Value("#{config['esb.expiredsms.text']}")
	private String expSMSText;
	
	@Value("#{config['esb.lastexpiredsms.duration']}")
	private String lastExpSMSDuration;

	@Value("#{config['esb.lastexpiredsms.sendtime']}")
	private String lastExpSMSSendTime;
	
	@Value("#{config['esb.lastexpiredsms.text']}")
	private String lastExpSMSText;
	
	@Value("#{config['esb.url']}")
	private String esbUrl;

	@Value("#{config['esb.systemid']}")
	private String esbSystemID;
	
	@Value("#{config['tlo.config.path']}")
	private String tloConfigPath;

	@Value("#{config['ai.svrKey']}")
	private String aiSvrKey;
	
	@Value("#{config['ai.auth.url']}")
	private String aiAuthUrl;
	
	@Value("#{config['ai.weather.connection.timeout']}")
	private String aiAuthConnectionTimeout;
	
	@Value("#{config['ai.auth.timeout']}")
	private String aiAuthTimeout;
	
	@Value("#{config['esb.expiredreserve.duration']}")
	private String esbExpireDuration;
	
	@Value("#{config['esb.expiredreserve.sendtime']}")
	private String esbExpireSendtime;
	
	//Nissan ESB Triggering
	@Value("#{config['el1.esb.expiredsms.duration']}")
	private String el1EsbExpiredsmsDuration;
	
	@Value("#{config['el1.esb.expiredsms.text']}")
	private String el1EsbExpiredsmsText;
	
	@Value("#{config['el1.esb.lastexpiredsms.duration']}")
	private String el1EsbLastexpiredsmsDuration;
	
	@Value("#{config['el1.esb.lastexpiredsms.text']}")
	private String el1EsbLastexpiredsmsText;
	
	@Value("#{config['el1.esb.expiredreserve.duration']}")
	private String el1EsbExpiredreserveDuration;	
	
	public String getExpSMSDuration() {
		return expSMSDuration;
	}

	public void setExpSMSDuration(String expSMSDuration) {
		this.expSMSDuration = expSMSDuration;
	}

	public String getExpSMSSendTime() {
		return expSMSSendTime;
	}

	public void setExpSMSSendTime(String expSMSSendTime) {
		this.expSMSSendTime = expSMSSendTime;
	}
	
	public String getExpSMSText() {
		return expSMSText;
	}

	public void setExpSMSText(String expSMSText) {
		this.expSMSText = expSMSText;
	}

	public String getLastExpSMSDuration() {
		return lastExpSMSDuration;
	}

	public void setLastExpSMSDuration(String lastExpSMSDuration) {
		this.lastExpSMSDuration = lastExpSMSDuration;
	}

	public String getLastExpSMSSendTime() {
		return lastExpSMSSendTime;
	}

	public void setLastExpSMSSendTime(String lastExpSMSSendTime) {
		this.lastExpSMSSendTime = lastExpSMSSendTime;
	}

	public String getLastExpSMSText() {
		return lastExpSMSText;
	}

	public void setLastExpSMSText(String lastExpSMSText) {
		this.lastExpSMSText = lastExpSMSText;
	}

	public String getEsbUrl() {
		return esbUrl;
	}

	public void setEsbUrl(String esbUrl) {
		this.esbUrl = esbUrl;
	}

	public String getEsbSystemID() {
		return esbSystemID;
	}

	public void setEsbSystemID(String esbSystemID) {
		this.esbSystemID = esbSystemID;
	}

	public String getTloConfigPath() {
		return tloConfigPath;
	}

	public void setTloConfigPath(String tloConfigPath) {
		this.tloConfigPath = tloConfigPath;
	}

	public String getAiSvrKey() {
		return aiSvrKey;
	}

	public void setAiSvrKey(String aiSvrKey) {
		this.aiSvrKey = aiSvrKey;
	}

	public String getAiAuthUrl() {
		return aiAuthUrl;
	}

	public void setAiAuthUrl(String aiAuthUrl) {
		this.aiAuthUrl = aiAuthUrl;
	}

	public String getAiAuthConnectionTimeout() {
		return aiAuthConnectionTimeout;
	}

	public void setAiAuthConnectionTimeout(String aiAuthConnectionTimeout) {
		this.aiAuthConnectionTimeout = aiAuthConnectionTimeout;
	}

	public String getAiAuthTimeout() {
		return aiAuthTimeout;
	}

	public void setAiAuthTimeout(String aiAuthTimeout) {
		this.aiAuthTimeout = aiAuthTimeout;
	}

	public String getEsbExpireDuration() {
		return esbExpireDuration;
	}

	public void setEsbExpireDuration(String esbExpireDuration) {
		this.esbExpireDuration = esbExpireDuration;
	}

	public String getEsbExpireSendtime() {
		return esbExpireSendtime;
	}

	public void setEsbExpireSendtime(String esbExpireSendtime) {
		this.esbExpireSendtime = esbExpireSendtime;
	}

	public String getEl1EsbExpiredsmsDuration() {
		return el1EsbExpiredsmsDuration;
	}

	public void setEl1EsbExpiredsmsDuration(String el1EsbExpiredsmsDuration) {
		this.el1EsbExpiredsmsDuration = el1EsbExpiredsmsDuration;
	}

	public String getEl1EsbExpiredsmsText() {
		return el1EsbExpiredsmsText;
	}

	public void setEl1EsbExpiredsmsText(String el1EsbExpiredsmsText) {
		this.el1EsbExpiredsmsText = el1EsbExpiredsmsText;
	}

	public String getEl1EsbLastexpiredsmsDuration() {
		return el1EsbLastexpiredsmsDuration;
	}

	public void setEl1EsbLastexpiredsmsDuration(String el1EsbLastexpiredsmsDuration) {
		this.el1EsbLastexpiredsmsDuration = el1EsbLastexpiredsmsDuration;
	}

	public String getEl1EsbLastexpiredsmsText() {
		return el1EsbLastexpiredsmsText;
	}

	public void setEl1EsbLastexpiredsmsText(String el1EsbLastexpiredsmsText) {
		this.el1EsbLastexpiredsmsText = el1EsbLastexpiredsmsText;
	}

	public String getEl1EsbExpiredreserveDuration() {
		return el1EsbExpiredreserveDuration;
	}

	public void setEl1EsbExpiredreserveDuration(String el1EsbExpiredreserveDuration) {
		this.el1EsbExpiredreserveDuration = el1EsbExpiredreserveDuration;
	}
	
	
	
}
