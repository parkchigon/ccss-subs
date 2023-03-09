package com.lgu.ccss.common.esb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESBManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String ESB_URL = null;
	private String ESB_SYSTEM_ID = null;
	
	private static ESBManager instance = null;
	
	public static final String REPLY_CTN = "019-114";
	
	public static synchronized ESBManager getInstance(String url, String systemID)
	{
		if( instance == null )
			instance = new ESBManager(url, systemID);
		
		return instance;
	}
	
	private ESBManager(String url, String systemID)
	{
		this.ESB_URL = url;
		this.ESB_SYSTEM_ID = systemID;
	}
	
	
	
	public boolean sendSMS(String subsNo, String ctn, String smsMessage, String sendTime, String replyNo)
	{
		SM023Interface sm023if = new SM023Interface(this.ESB_URL, this.ESB_SYSTEM_ID);
		return sm023if.sendSMS(subsNo, ctn, smsMessage, sendTime, replyNo);
	}
	
	public String getCustomerCTN(String subsNo,String pdnCTN)
	{
		SB609Interface sb609if = new SB609Interface(this.ESB_URL, this.ESB_SYSTEM_ID);
		return sb609if.getCustomerCTN(subsNo);
	}
	
	public String sendExpireReserve(String pdnCTN)
	{
		SB929Interface sb929if = new SB929Interface(this.ESB_URL, this.ESB_SYSTEM_ID);
		return sb929if.sendExpireReserve(changeCTNFormat(pdnCTN));
	}
	
	/**
     * @Part :
     * @Author :
     * @Component :
     * @Primitive :
     * @type :
     * @Description : generate transaction id - 20110305090101999(17자리) +
     *              1234567(7자리)
     */
    public static synchronized String createTxId() {
          Date date = new Date();
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
          String time = simpleDateFormat.format(date);
          String random = String.format("%07d", (new Random().nextInt(9999999)));
          return time + random;
    }



    /**
     * @Part :
     * @Author :
     * @Component :
     * @Primitive :
     * @type :
     * @Description :
     */
    public static synchronized String createNextOperatorId() {
         return String.valueOf(System.currentTimeMillis());
    }


    public String changeCTNFormat(String strMin) {
		if (strMin == null || strMin.length() == 0)
			return null;
		int len = strMin.length();
		String newMin = "", first = "", last = "";
		try {
			switch (len) {
			case 12: // 0190 1234 5678
				newMin = strMin;
				break;
			case 11: // 019 1234 5678
				first = strMin.substring(0, 3);
				last = strMin.substring(3, strMin.length());
				newMin = first + "0" + last;
				break;
			case 10: // 019 123 4567
				first = strMin.substring(0, 3);
				last = strMin.substring(3, strMin.length());
				newMin = first + "00" + last;
				break;
			default:
				return null;
			}
			if (newMin.length() != 12) {
				logger.error("changeCTNFormat Invalid MIN " + newMin.length());
				return null;
			}
			return newMin;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
