package com.lgu.common.ai;

public class AiBody {
	private String idTypeCd;
	private String deviceSN;
	private String authInfo;
	private String exitYn;
	
	private DeviceToken deviceToken;
	private PlatformToken platformToken;
	
	
	public String getIdTypeCd() {
		return idTypeCd;
	}
	public void setIdTypeCd(String idTypeCd) {
		this.idTypeCd = idTypeCd;
	}
	public String getDeviceSN() {
		return deviceSN;
	}
	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}
	public String getAuthInfo() {
		return authInfo;
	}
	public void setAuthInfo(String authInfo) {
		this.authInfo = authInfo;
	}
	
	
	
	
	public String getExitYn() {
		return exitYn;
	}
	public void setExitYn(String exitYn) {
		this.exitYn = exitYn;
	}
	public DeviceToken getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(DeviceToken deviceToken) {
		this.deviceToken = deviceToken;
	}
	public PlatformToken getPlatformToken() {
		return platformToken;
	}
	public void setPlatformToken(PlatformToken platformToken) {
		this.platformToken = platformToken;
	}
}
