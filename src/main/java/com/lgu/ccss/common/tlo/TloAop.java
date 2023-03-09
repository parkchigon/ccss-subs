package com.lgu.ccss.common.tlo;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgu.common.ai.AiPlatform;
import com.lgu.common.ai.AuthErrorCode;
import com.lgu.common.ncas.NCASErrorCode;
import com.lgu.common.ncas.NCASResultData;

@Aspect
public class TloAop {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Pointcut("execution(* com.lgu.common.ncas.NCASQueryManager.querySubsInfo(..))")
	private void pointCutNcas() {
		
	}
	
	@Around("pointCutNcas()")
	public Object tloAopNcas(ProceedingJoinPoint joinPoint) throws Throwable {
		Map<String, String> tlo = new HashMap<String, String>();
		tlo.put(TloData.NCAS_SVC_CLASS, TloConst.NC01);
		tlo.put(TloData.NCAS_REQ_TIME, TloData.getNowDate());
		
		String key = null;
		Object obj = null;
		try {
			key = (String)joinPoint.getArgs()[0];	
			obj = joinPoint.proceed();
			return obj;
			
		} catch(Exception ex)
		{
			logger.error("TloAop tloAopNcas Exception", ex);
		
		} finally {
			
			NCASResultData ncasData = null;
			if( obj != null )
			{
				ncasData = (NCASResultData) obj;
				tlo.put(TloData.NCAS_RESULT_CODE, ncasData.getRespCode());
			} else
			{
				tlo.put(TloData.NCAS_RESULT_CODE, NCASErrorCode.ERRORCODE_NCAS_ERROR);
			}

			tlo.put(TloData.NCAS_RESULT_CODE, NCASErrorCode.ERRORCODE_NCAS_ERROR);
			tlo.put(TloData.NCAS_RES_TIME, TloData.getNowDate());
			TloUtil.setTloData(key, tlo);
		}
		return obj;
	}
	
	@Pointcut("execution(* com.lgu.common.ai.AiPlatformIF.expirePlatformToken(..))")
	private void pointCutAIAuth() {
		
	}
	
	@Around("pointCutAIAuth()")
	public Object tloAopAIAuth(ProceedingJoinPoint joinPoint) throws Throwable {
		
		logger.debug("TloAop tloAopAIAuth call");
		Map<String, String> tlo = new HashMap<String, String>();
		tlo.put(TloData.AI_AUTH_SVC_CLASS, TloConst.A008);
		tlo.put(TloData.AI_AUTH_REQ_TIME, TloData.getNowDate());
		
		String key = null;
		Object obj = null;
		try {
			key = (String)joinPoint.getArgs()[2];
			
			logger.debug("TloAop tloAopAIAuth key:" + key);
			
			obj = joinPoint.proceed();
			return obj;
			
		} catch(Exception ex)
		{
			logger.error("TloAop tloAopAIAuth Exception", ex);
		
		} finally {
			
			@SuppressWarnings("unused")
			AiPlatform aiPlatform = null;
			if( obj != null )
			{
				aiPlatform = (AiPlatform) obj;
				tlo.put(TloData.AI_AUTH_RESULT_CODE, AuthErrorCode.RC_20000000.getCode());
			} else
			{
				tlo.put(TloData.AI_AUTH_RESULT_CODE, AuthErrorCode.RC_60000000.getCode());
			}

			tlo.put(TloData.AI_AUTH_RES_TIME, TloData.getNowDate());
			TloUtil.setTloData(key, tlo);
		}
		return obj;
	}
	
	
	@Pointcut("execution(* com.lgu.ccss.common.esb.ESBManager.getCustomerCTN(..))")
	private void pointCutESBGetCustomerCTN() {
		
	}
	
	@Around("pointCutESBGetCustomerCTN()")
	public Object tloAopESBGetCustomerCTN(ProceedingJoinPoint joinPoint) throws Throwable {
		
		logger.debug("TloAop tloAopESBGetCustomerCTN call");
		Map<String, String> tlo = new HashMap<String, String>();
		tlo.put(TloData.ESB_SVC_CLASS, TloConst.ES01);
		tlo.put(TloData.ESB_REQ_TIME, TloData.getNowDate());
		
		String key = null;
		Object obj = null;
		try {
			key = (String)joinPoint.getArgs()[2];
			
			logger.debug("TloAop tloAopESBGetCustomerCTN key:" + key);
			
			obj = joinPoint.proceed();
			return obj;
			
		} catch(Exception ex)
		{
			logger.error("TloAop tloAopAIAuth Exception", ex);
		
		} finally {
			
			@SuppressWarnings("unused")
			AiPlatform aiPlatform = null;
			if( obj != null )
			{
				aiPlatform = (AiPlatform) obj;
				tlo.put(TloData.ESB_RESULT_CODE, TloConst.ESB_SUCCESS);
			} else
			{
				tlo.put(TloData.ESB_RESULT_CODE, TloConst.ESB_FAIL);
			}

			tlo.put(TloData.ESB_RES_TIME, TloData.getNowDate());
			TloUtil.setTloData(key, tlo);
		}
		return obj;
	}
	
}
