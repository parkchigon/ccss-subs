package com.lgu.ccss.common.model;

import java.util.Date;

public class MembVO {
	
	public static String MEMB_USE_STATUS_ACTIVE = "01";
	public static String MEMB_USE_STATUS_SUSPEND = "02";
	public static String MEMB_USE_STATUS_CANCEL = "09";
	
	public static String MEMB_MACKET_TYPE_AM = "AM";
	public static String MEMB_MACKET_TYPE_BM = "BM";
	
	public static String MEMB_PAY_RESV_YN_Y = "Y";
	public static String MEMB_PAY_RESV_YN_N = "N";
	
	public static String MEMB_EXP_SMS_SEND_YN_Y = "Y";
	public static String MEMB_EXP_SMS_SEND_YN_N = "N";
	
	public static String LAST_MEMB_EXP_SMS_SEND_YN_Y = "Y";
	public static String LAST_MEMB_EXP_SMS_SEND_YN_N = "N";
	
	private String membId;
	private String membNo;
	private String useStatus;
	private String useYn;
	private String newRejoinYn;
	private String productNm;
	private Date joinDt;
	private Date latestLoginDt;
	private Integer loginFailCnt;
	private String regId;
	private Date regDt;
	private String updId;
	private Date updDt;
	private String deviceCTN;
	private String connDeviceID;
	private String serverID;
	private String marketType;
	private String payResvYN;
	private Date payResvDT;
	private String expSMSSendYN;
	private Date expSMSSendDT;
	private int rownum;
	private String deviceModelNM;
	private String uiccid;
	private Date syncDt;
	private String lastExpSMSSendYN;
	private Date lastExpSMSSendDT;
	
	public String getMembId() {
		return membId;
	}
	public void setMembId(String membId) {
		this.membId = membId;
	}
	public String getMembNo() {
		return membNo;
	}
	public void setMembNo(String subNo) {
		this.membNo = subNo;
	}
	public String getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getNewRejoinYn() {
		return newRejoinYn;
	}
	public void setNewRejoinYn(String newRejoinYn) {
		this.newRejoinYn = newRejoinYn;
	}
	public String getProductNm() {
		return productNm;
	}
	public void setProductNm(String productNm) {
		this.productNm = productNm;
	}
	public Date getJoinDt() {
		return joinDt;
	}
	public void setJoinDt(Date joinDt) {
		this.joinDt = joinDt;
	}
	public Date getLatestLoginDt() {
		return latestLoginDt;
	}
	public void setLatestLoginDt(Date latestLoginDt) {
		this.latestLoginDt = latestLoginDt;
	}
	public Integer getLoginFailCnt() {
		return loginFailCnt;
	}
	public void setLoginFailCnt(Integer loginFailCnt) {
		this.loginFailCnt = loginFailCnt;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public String getUpdId() {
		return updId;
	}
	public void setUpdId(String updId) {
		this.updId = updId;
	}
	public Date getUpdDt() {
		return updDt;
	}
	public void setUpdDt(Date updDt) {
		this.updDt = updDt;
	}
	public String getDeviceCTN() {
		return deviceCTN;
	}
	public void setDeviceCTN(String deviceCTN) {
		this.deviceCTN = deviceCTN;
	}	
	public String getConnDeviceID() {
		return connDeviceID;
	}
	public void setConnDeviceID(String connDeviceID) {
		this.connDeviceID = connDeviceID;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}	
	public String getServerID() {
		return serverID;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	public String getMarketType() {
		return marketType;
	}
	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}
	public String getPayResvYN() {
		return payResvYN;
	}
	public void setPayResvYN(String payResvYN) {
		this.payResvYN = payResvYN;
	}
	public Date getPayResvDT() {
		return payResvDT;
	}
	public void setPayResvDT(Date payResvDT) {
		this.payResvDT = payResvDT;
	}
	public String getExpSMSSendYN() {
		return expSMSSendYN;
	}
	public void setExpSMSSendYN(String expSMSSendYN) {
		this.expSMSSendYN = expSMSSendYN;
	}
	public Date getExpSMSSendDT() {
		return expSMSSendDT;
	}
	public void setExpSMSSendDT(Date expSMSSendDT) {
		this.expSMSSendDT = expSMSSendDT;
	}
	public String getDeviceModelNM() {
		return deviceModelNM;
	}
	public void setDeviceModelNM(String deviceModelNM) {
		this.deviceModelNM = deviceModelNM;
	}
	public String getUiccid() {
		return uiccid;
	}
	public void setUiccid(String uiccid) {
		this.uiccid = uiccid;
	}
	public Date getSyncDt() {
		return syncDt;
	}
	public void setSyncDt(Date syncDt) {
		this.syncDt = syncDt;
	}
	public String getLastExpSMSSendYN() {
		return lastExpSMSSendYN;
	}
	public void setLastExpSMSSendYN(String lastExpSMSSendYN) {
		this.lastExpSMSSendYN = lastExpSMSSendYN;
	}
	public Date getLastExpSMSSendDT() {
		return lastExpSMSSendDT;
	}
	public void setLastExpSMSSendDT(Date lastExpSMSSendDT) {
		this.lastExpSMSSendDT = lastExpSMSSendDT;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n");
		sb.append("====================== MembVO ======================").append("\r\n");
	    sb.append("membId         :").append(membId).append("\r\n");
	    sb.append("membNo         :").append(membNo).append("\r\n");
	    sb.append("useStatus      :").append(useStatus).append("\r\n");
	    sb.append("useYn          :").append(useYn).append("\r\n");
	    sb.append("newRejoinYn    :").append(newRejoinYn).append("\r\n");
	    sb.append("productNm      :").append(productNm).append("\r\n");
	    sb.append("joinDt         :").append(joinDt).append("\r\n");
	    sb.append("latestLoginDt  :").append(latestLoginDt).append("\r\n");
	    sb.append("loginFailCnt   :").append(loginFailCnt).append("\r\n");
	    sb.append("marketType     :").append(marketType).append("\r\n");
	    sb.append("payResvYN      :").append(payResvYN).append("\r\n");
	    sb.append("payResvDT      :").append(payResvDT).append("\r\n");
	    sb.append("expSMSSendYN   :").append(expSMSSendYN).append("\r\n");
	    sb.append("expSMSSendDT   :").append(expSMSSendDT).append("\r\n");
	    sb.append("regId          :").append(regId).append("\r\n");
	    sb.append("regDt          :").append(regDt).append("\r\n");
	    sb.append("updId          :").append(updId ).append("\r\n");
	    sb.append("updDt          :").append(updDt).append("\r\n");
	    sb.append("deviceCTN  	  :").append(deviceCTN).append("\r\n");
	    sb.append("deviceModelNM  :").append(deviceModelNM).append("\r\n");
	    sb.append("uiccid  		  :").append(uiccid).append("\r\n");
	    sb.append("syncDt  		  :").append(syncDt).append("\r\n");
	    sb.append("lastExpSMSSendYN   :").append(lastExpSMSSendYN).append("\r\n");
	    sb.append("lastExpSMSSendDT   :").append(lastExpSMSSendDT).append("\r\n");
	    
		return sb.toString();
				
	}
	
	
}
