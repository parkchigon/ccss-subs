package com.lgu.common.ai;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson;
import com.lgu.common.api.ApiRequestVO;
import com.lgu.common.api.LogData;
import com.lgu.common.util.HttpReqUtil;
import com.lgu.common.util.Pair;

import javassist.NotFoundException;

@Component
public class AiPlatformIF {
	private static final Logger logger = LoggerFactory.getLogger(AiPlatformIF.class);
	
	
	public AiPlatform createDeviceToken(ApiRequestVO loginAiVo, String deviceCtn, String deviceSerial,String aiServerHost,String aiServerPort, String svrKey) 
			throws ParseException, IOException, NotFoundException {
		// TODO Auto-generated method stub
		logger.debug("createDeviceToken----->");
		JSONObject resultJson = null;
			
		LogData logData = loginAiVo.getCommon().getLogData();

		AiPlatform aiPlatformVo = new AiPlatform();
		aiPlatformVo.getAiCommonVo().setClientIp(logData.getClientIp());
		aiPlatformVo.getAiCommonVo().setDevInfo(logData.getDevInfo());
		aiPlatformVo.getAiCommonVo().setOsInfo(logData.getOsInfo());
		aiPlatformVo.getAiCommonVo().setNwInfo(logData.getNwInfo());
		aiPlatformVo.getAiCommonVo().setDevModel(logData.getDevModel());
		aiPlatformVo.getAiCommonVo().setCarrierType(logData.getCarrierType());
		aiPlatformVo.getAiCommonVo().setServiceType(logData.getSvcType());
		
		aiPlatformVo.getAiBodyVo().setIdTypeCd("CTN");
		aiPlatformVo.getAiBodyVo().setDeviceSN(deviceSerial);
		
		Gson gson = new Gson();
		String aiPlatformJson = gson.toJson(aiPlatformVo);
		logger.debug("createDeviceToken Req Data : "+aiPlatformJson);
		ArrayList<Pair<String, String>> headerList = new ArrayList<>();
		headerList.add(Pair.createPair(AIAuthAPIConstant.HD_NAME_SVRKEY, svrKey));
		headerList.add(Pair.createPair(AIAuthAPIConstant.HD_NAME_CUSTOMID, deviceCtn));
		headerList.add(Pair.createPair(AIAuthAPIConstant.HD_NAME_CONTENTTYPE, AIAuthAPIConstant.HD_VALUE_CONTENTTYPE_JSON_UTF8));
		String requestUri = aiServerHost+":"+aiServerPort+"/auth/createDeviceToken";

		/*MDC.put(MDCConstant.TRACE_DEST, TraceUtils.EP_AI_AUTH);
		MDC.put(MDCConstant.TRACE_OPERATION, APIConstant.AI_AUTH_CREATE_DEVICE_TOKEN);*/
		
		resultJson = HttpReqUtil.httpRequest(requestUri, headerList, aiPlatformJson);
		
		AiPlatform aiPlatformResVo = new AiPlatform();
		aiPlatformResVo = gson.fromJson(resultJson.toJSONString(), AiPlatform.class);

		return aiPlatformResVo;
	}
	
	public AiPlatform createPlatformToken(ApiRequestVO loginAiVo, String deviceToken, String deviceCtn, String deviceSerial,String aiServerHost,String aiServerPort, String svrKey) throws ParseException, IOException, NotFoundException {
		// TODO Auto-generated method stub	
		logger.debug("createPlatformToken----->");
		JSONObject resultJson = null;
		
		LogData logData = loginAiVo.getCommon().getLogData();

		AiPlatform aiPlatformVo = new AiPlatform();
		aiPlatformVo.getAiCommonVo().setClientIp(logData.getClientIp());
		aiPlatformVo.getAiCommonVo().setDevInfo(logData.getDevInfo());
		aiPlatformVo.getAiCommonVo().setOsInfo(logData.getOsInfo());
		aiPlatformVo.getAiCommonVo().setNwInfo(logData.getNwInfo());
		aiPlatformVo.getAiCommonVo().setDevModel(logData.getDevModel());
		aiPlatformVo.getAiCommonVo().setCarrierType(logData.getCarrierType());
		aiPlatformVo.getAiCommonVo().setServiceType(logData.getSvcType());
		aiPlatformVo.getAiCommonVo().setDeviceType(logData.getDevType());
		aiPlatformVo.getAiCommonVo().setIdTypeCd("CTN");
		aiPlatformVo.getAiCommonVo().setDeviceSN(deviceSerial);
		aiPlatformVo.getAiBodyVo().setAuthInfo(deviceCtn);
		
		Gson gson = new Gson();
		String aiPlatformJson = gson.toJson(aiPlatformVo);
		logger.debug("createPlatformToken Req Data : "+aiPlatformJson);
		ArrayList<Pair<String, String>> headerList = new ArrayList<>();
		headerList.add(Pair.createPair("svrKey", svrKey));
		headerList.add(Pair.createPair("customId", deviceCtn));
		headerList.add(Pair.createPair("deviceToken", deviceToken));
		headerList.add(Pair.createPair("Content-Type", "application/json;charset=UTF-8"));
		String requestUri = aiServerHost+":"+aiServerPort+"/auth/createPlatformToken";
		
/*		MDC.put(MDCConstant.TRACE_DEST, TraceUtils.EP_AI_AUTH);
		MDC.put(MDCConstant.TRACE_OPERATION, APIConstant.AI_AUTH_CREATE_PLATFORM_TOKEN);*/
		
		resultJson = HttpReqUtil.httpRequest(requestUri, headerList, aiPlatformJson);
		AiPlatform aiPlatformResVo = new AiPlatform();
		aiPlatformResVo = gson.fromJson(resultJson.toJSONString(), AiPlatform.class);
		
		return aiPlatformResVo;
	}
	
	public AiPlatform expirePlatformToken(ApiRequestVO loginAiVo, String platformToken, String deviceCtn, String deviceSerial,String aiServerURL, String svrKey)
	{
		// TODO Auto-generated method stub	
		logger.debug("expirePlatformToken----->");
		AiPlatform aiPlatformResVo =  null;
		
		try
		{
			JSONObject resultJson = null;
			LogData logData = loginAiVo.getCommon().getLogData();

			AiPlatform aiPlatformVo = new AiPlatform();
			aiPlatformVo.getAiCommonVo().setClientIp(logData.getClientIp());
			aiPlatformVo.getAiCommonVo().setDevInfo(logData.getDevInfo());
			aiPlatformVo.getAiCommonVo().setOsInfo(logData.getOsInfo());
			aiPlatformVo.getAiCommonVo().setNwInfo(logData.getNwInfo());
			aiPlatformVo.getAiCommonVo().setDevModel(logData.getDevModel());
			aiPlatformVo.getAiCommonVo().setCarrierType(logData.getCarrierType());
			/*aiPlatformVo.getAiCommonVo().setServiceType(logData.getSvcType());
			aiPlatformVo.getAiCommonVo().setDeviceType(logData.getDevType());
			aiPlatformVo.getAiCommonVo().setIdTypeCd("CTN");
			aiPlatformVo.getAiCommonVo().setDeviceSN(deviceSerial);
			*/
			aiPlatformVo.getAiBodyVo().setExitYn("Y");
			
			Gson gson = new Gson();
			String aiPlatformJson = gson.toJson(aiPlatformVo);
			logger.debug("expirePlatformToken Req Data : "+aiPlatformJson);
			ArrayList<Pair<String, String>> headerList = new ArrayList<>();
			headerList.add(Pair.createPair("svrKey", svrKey));
			headerList.add(Pair.createPair("customId", deviceCtn));
			headerList.add(Pair.createPair("platformToken", platformToken));
			headerList.add(Pair.createPair("Content-Type", "application/json;charset=UTF-8"));
			String requestUri = aiServerURL+"/auth/expirePlatformToken";
			
	/*		MDC.put(MDCConstant.TRACE_DEST, TraceUtils.EP_AI_AUTH);
			MDC.put(MDCConstant.TRACE_OPERATION, APIConstant.AI_AUTH_CREATE_PLATFORM_TOKEN);*/
			
			resultJson = HttpReqUtil.httpRequest(requestUri, headerList, aiPlatformJson);
			aiPlatformResVo = new AiPlatform();
			aiPlatformResVo = gson.fromJson(resultJson.toJSONString(), AiPlatform.class);
		} catch( Exception ex )
		{
			logger.error("AiPlatform expirePlatformToken Exception", ex);
		}
		
		
		return aiPlatformResVo;
	}
	
	
}
