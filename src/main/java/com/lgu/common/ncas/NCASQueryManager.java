package com.lgu.common.ncas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lgtel.mmdb.CasCrypto;

@Component
public class NCASQueryManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("#{config['ncas.url']}")
	private String URL;
	@Value("#{config['ncas.cp.type']}")
	private String CP_TYPE;
	@Value("#{config['ncas.cp.id']}")
	public String CP_ID;
	@Value("#{config['ncas.cp.pwd']}")
	public String CP_PWD;
	@Value("#{config['ncas.cp.casecode']}")
	public String CASE_CODE;
	@Value("#{config['ncas.connection.timeout']}")
	public String CAS_CONNECTION_TIMEOUT;
	@Value("#{config['ncas.timeout']}")
	public String CAS_TIMEOUT;
	
	@Value("#{config['ncas.sim.use']}")
	public String SIM_CAS_USE;
	
	@Value("#{config['ncas.sim.url']}")
	public String SIM_CAS_URL;
	
	public NCASResultData querySubsInfo(String ctn)  throws IOException {
		NCASResultData result = null;
		
		if (SIM_CAS_USE.equals("Y")) {
			result = getSubsInfo(URL, ctn);
			if (result == null || result.getSubsInfo() == null  ) {
				logger.warn("TEST_NCAS Query : " + ctn);
				result = getSubsInfo(SIM_CAS_URL, ctn);
			}
			
		} else {
			result = getSubsInfo(URL, ctn);
		}
		
		return result;
	}

	@SuppressWarnings("deprecation")
	private NCASResultData getSubsInfo(String url, String ctn) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("?CPTYPE=" + CP_TYPE);
		sb.append("&CPID=" + CP_ID);
		sb.append("&CPPWD=" + URLEncoder.encode(CasCrypto.casCryptoEncode("E645919BADBAD0D9", "D076AEABE5BC7585",
				"DF89BCE93B70CD13", "D91C3245767F1C0E", CP_PWD)));
		sb.append("&CASECODE=" + CASE_CODE);
		sb.append("&CTN=" + changeCTNFormat(ctn));

		NCASResultData ncasRltData = null;
		
		HttpGet get = new HttpGet(url + sb.toString());
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				Integer.parseInt(CAS_CONNECTION_TIMEOUT));
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.parseInt(CAS_TIMEOUT));

		String errorCode = NCASErrorCode.ERRORCODE_NCAS_ERROR;
		HttpResponse response;
		try {
			
			response = client.execute(get);
			
			if( response.getStatusLine().getStatusCode() != HttpStatus.SC_OK )
			{
				logger.error("HTTP CODE:" + response.getStatusLine().getStatusCode());
				errorCode = response.getStatusLine().getStatusCode() + "";
			}

			for (Header header : response.getAllHeaders()) {
				if ("RESP".equals(header.getName())) {
					//tlo.put(TloData.NCAS_RES_TIME, TloData.getNowDate());
					ncasRltData = getSubsInfo(ctn, header);
				}
			}
			
		} catch (IOException e) {
			logger.error("NCAS getSubsInfo IOException", e);
			errorCode = NCASErrorCode.ERRORCODE_NCAS_ERROR;
		} finally {
			
		}
		
		if( ncasRltData == null )
		{
			ncasRltData = new NCASResultData(null, errorCode);
		}
		return ncasRltData;
	}
	
	private NCASResultData getSubsInfo(String ctn, Header header) {
		boolean casResult = false;
		SubsInfo subsInfo = null;
		
		String[] result = header.getValue().split("&");
		String key = null;
		String value = null;

		Map<String, String> resultBodyMap = new HashMap<String, String>();
		String resultCode = null;
		
		for (String data : result) {
			logger.debug("NCAS BODY DATA:" + data);

			if (data.split("=") == null) {
				continue;
			}

			key = data.split("=")[0];

			if (data.split("=").length == 2) {
				value = data.split("=")[1];
			}
			else {
				logger.debug("NCAS NO VALUE " + data);
			}
			
			if (key.equalsIgnoreCase("svc_auth_dt")) {
				String svcAuthDtStr = null;
				try {
					svcAuthDtStr = java.net.URLDecoder.decode(value, "UTF-8");
					logger.debug("svcAuthDt BODY DATA:" + svcAuthDtStr);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				String[] svcAuthDt = svcAuthDtStr.split("\\|");
				
				if (svcAuthDt != null && svcAuthDt.length ==14) {
					for(int i = 13; i>=11 ; i--){
						if (svcAuthDt[i]!=null&&!svcAuthDt[i].equals("0")) {
							String findRegDate = svcAuthDt[i].substring(0,4) + "-" + svcAuthDt[i].substring(4,6) + "-" + svcAuthDt[i].substring(6,8)+
									"+"+svcAuthDt[i].substring(8,10)+"%3A"+svcAuthDt[i].substring(10,12)+"%3A00.0";
							resultBodyMap.replace("REGDATE", findRegDate);
							break;
						}
					}
					for(int i = 0; i <= 4 ; i++){
						if (svcAuthDt[i]!=null&&!svcAuthDt[i].equals("0")) {
							String findRegDate = svcAuthDt[i].substring(0,4) + "-" + svcAuthDt[i].substring(4,6) + "-" + svcAuthDt[i].substring(6,8)+
									"+"+svcAuthDt[i].substring(8,10)+"%3A"+svcAuthDt[i].substring(10,12)+"%3A00.0";
							resultBodyMap.replace("REGDATE", findRegDate);
							break;
						}
					}
				}
			}

			if (NCASConst.ncas_field_respcode.equals(key)) {
				resultCode = value;
				if (NCASErrorCode.ERRORCODE_SUCCESS.equals(resultCode)) {
					casResult = true;
				} else {
					logger.error("NCAS ERROR. CTN:" + ctn + ", NCAS_CTN:" + changeCTNFormat(ctn) + ", respcode:"
							+ resultCode);
					break;
				}
				
				// ##### NCAS RESP_CODE TLO
			}

			resultBodyMap.put(key, value);
		}
		
		if (casResult && resultBodyMap != null) {
			subsInfo = new SubsInfo(resultBodyMap);
		} 
		
		NCASResultData rsltData = new NCASResultData(subsInfo, resultCode);
		
		return rsltData;
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
