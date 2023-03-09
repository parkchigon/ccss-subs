package lguplus.u3.webservice;

import com.lgu.ccss.common.esb.ESBManager;

public class Sample2 {
	
	public static void main(String[] args)
	{
		// http://172.22.14.79:15011/CSSI/SM/CreateSmsSend 
		String url = "http://172.22.14.79:15011";
		String systemID = "CCSS0001";
		ESBManager esbManager = ESBManager.getInstance(url, systemID);
		String customerCTN = esbManager.getCustomerCTN("500082049324", "010022330811");
		System.out.println("TEST customerCTN:" + customerCTN);
		
		boolean sendFlag = esbManager.sendSMS("500082049324", "010022330811", "ESB 테스트 메시지", "20171023230000",  ESBManager.REPLY_CTN);
		System.out.println("TEST Result:" + sendFlag);
		
	}

}
