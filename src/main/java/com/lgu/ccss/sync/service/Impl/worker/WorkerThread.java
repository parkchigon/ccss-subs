package com.lgu.ccss.sync.service.Impl.worker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgu.ccss.App;
import com.lgu.ccss.common.esb.ESBManager;
import com.lgu.ccss.common.esb.SB929Interface;
import com.lgu.ccss.common.model.AIAuthInfoVO;
import com.lgu.ccss.common.model.ESBTriggerVO;
import com.lgu.ccss.common.model.MembVO;
import com.lgu.ccss.common.tlo.TloData;
import com.lgu.ccss.common.tlo.TloUtil;
import com.lgu.ccss.common.util.ResultCode;
import com.lgu.ccss.common.util.ResultCodeUtil;
import com.lgu.ccss.config.PropertyConfig;
import com.lgu.ccss.sync.mapper.SyncMapper;
import com.lgu.common.ai.AiPlatform;
import com.lgu.common.ai.AiPlatformIF;
import com.lgu.common.api.ApiRequestVO;
import com.lgu.common.api.CommonReqVO;
import com.lgu.common.api.LogData;
import com.lgu.common.ncas.NCASConst;
import com.lgu.common.ncas.NCASErrorCode;
import com.lgu.common.ncas.NCASQueryManager;
import com.lgu.common.ncas.NCASResultData;
import com.lgu.common.ncas.SubsInfo;
import com.lgu.common.tlo.TloWriter;
import com.lgu.common.util.DateUtils;
import com.lgu.common.util.keygenerator.KeyGenerator;

public class WorkerThread
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	RealWorker	worker		= null;

	int threadID;
	int workerNum;
	String DEBUG_STRING = null;
	
	private Vector<MembVO> msgVector = null;
	
	private SyncMapper syncMapper;
	
	private NCASQueryManager nCASQueryManager;
	
	private PropertyConfig propertyConfig;
	
	private AiPlatformIF aiPlatformIF;
	
	private String expSMSDuration;
	private String expSMSText;
	private String lastExpSMSDuration;
	private String lastExpSMSText;
	private String expReserveDuration;		
	
	public WorkerThread()
	{
	}

	public WorkerThread (int tid)
	{
		threadID=tid;
		DEBUG_STRING = "[WorkerThread-" + threadID + "] : ";
		worker	= new RealWorker();
		worker.start();
		
		msgVector = new Vector<MembVO>(1000);
		
		syncMapper = (SyncMapper)App.ctx.getBean(SyncMapper.class);
		nCASQueryManager = (NCASQueryManager)App.ctx.getBean(NCASQueryManager.class);
		propertyConfig = (PropertyConfig)App.ctx.getBean(PropertyConfig.class);
		aiPlatformIF = (AiPlatformIF)App.ctx.getBean(AiPlatformIF.class);
		
		TloWriter.tloFilePath = propertyConfig.getTloConfigPath();
		
	}

	public void setWorkerNum( int workerNum	)
	{
		this.workerNum	= workerNum;
	}

	public synchronized void wake ()
	{
		this.notify ();
	}

	private class RealWorker extends Thread
	{
	
		public RealWorker()
		{
			setDaemon(true);
			setName("WorkerThread-"+(threadID+100));
		}
		
		private boolean runFlag = true;

		public void run()
		{
			while (runFlag) {
				String tlokey = null;
				ResultCode tloErrorCode = ResultCodeUtil.RC_2S000000;
				try {
					Object object = null;
					synchronized (msgVector) {
						if (msgVector.size() > 0)
							object = msgVector.remove(0);
					}
					if (object == null) {
						sleep(20);
						continue;
					}
					logger.info("CALL run ======================> RUN START");
					MembVO membVO = ((MembVO) object);
					NCASResultData resultData = nCASQueryManager.querySubsInfo(membVO.getDeviceCTN());
					
					tlokey = membVO.getDeviceCTN();
					TLOImpl.initTLO(membVO.getDeviceCTN(), membVO.getMembId());
					logger.debug("NCAS Query===========>\r\n" + resultData);
					
					if( resultData == null )
					{
						logger.error("NCAS RSLT ERROR. CTN:" + membVO.getDeviceCTN());
						continue;
					}
					
					SubsInfo subsInfo = resultData.getSubsInfo();
					
					// 해지
					if ( resultData.getRespCode().equals(NCASErrorCode.ERRORCODE_NO_LGT) || 
							resultData.getRespCode().equals(NCASErrorCode.ERRORCODE_PORTEDOUT_SKT) ||
							resultData.getRespCode().equals(NCASErrorCode.ERRORCODE_PORTEDOUT_KTF)
							) {
						changeStatusToCancel(membVO);

					} else {
						// 정상 => 일시정지
						if (membVO.getUseStatus().equals(MembVO.MEMB_USE_STATUS_ACTIVE)
								&& subsInfo.getCtn_stus_code().equals(NCASConst.CTN_STUS_CODE_SUSPEND)) {
							
							changeStatusToSuspend(membVO);
						} // 일시정지 => 정상
						else if (membVO.getUseStatus().equals(MembVO.MEMB_USE_STATUS_SUSPEND)
								&& subsInfo.getCtn_stus_code().equals(NCASConst.CTN_STUS_CODE_ACTIVE)) {
							changeStatusToActive(membVO);
						} // 상태변경 없음.
						else {
							int updateMembCount = syncMapper.updateUpdateDT(membVO);
							logger.debug("NO_CHANGE_MEMB UPDATE:" + updateMembCount);
							
							if( resultData.getRespCode().equals(NCASErrorCode.ERRORCODE_NCAS_ERROR) )
								 tloErrorCode = ResultCodeUtil.RC_5N000001;
						}
					}
					
					// 닛산 LEAF 20190128 kangjin
					String connDeviceId = syncMapper.getConnDeviceModel(membVO.getMembId());
					
					// 닛산 LEAF 20190128 kangjin
					if("EL1".equals(connDeviceId)) {	
						
						expSMSDuration = propertyConfig.getEl1EsbExpiredsmsDuration();
						expSMSText = propertyConfig.getEl1EsbExpiredsmsText();
						lastExpSMSDuration = propertyConfig.getEl1EsbLastexpiredsmsDuration();
						lastExpSMSText = propertyConfig.getEl1EsbLastexpiredsmsText();
						expReserveDuration = propertyConfig.getEl1EsbExpiredreserveDuration();	
						
					}else {
						
						expSMSDuration = propertyConfig.getExpSMSDuration();
						expSMSText = propertyConfig.getExpSMSText();
						lastExpSMSDuration = propertyConfig.getLastExpSMSDuration();
						lastExpSMSText = propertyConfig.getLastExpSMSText();
						expReserveDuration = propertyConfig.getEsbExpireDuration();		
						
					}					
					
					
					logger.debug("MEMB Expired SMS isExpireTime:" + membVO.getJoinDt() + ",duration:" + expSMSDuration 
					+ ", diff:" + DateUtils.isExpireTime(membVO.getJoinDt(), expSMSDuration)
					+ ", membVO.getPayResvYN():" + membVO.getPayResvYN()
					+ ", membVO.getExpSMSSendYN():" + membVO.getExpSMSSendYN());
					
					//335일 만료 SMS 전송 ESB
					if( DateUtils.isExpireTime(membVO.getJoinDt(), expSMSDuration) 
							&& membVO.getPayResvYN().equals(MembVO.MEMB_PAY_RESV_YN_N) 
							&& membVO.getExpSMSSendYN().equals(MembVO.MEMB_EXP_SMS_SEND_YN_N) )
					{
						logger.info("MEMB sendServiceExpiredMessage MEMB_ID:" + membVO.getDeviceCTN() + " isExpireTime:" + membVO.getJoinDt() + ",duration:" + expSMSDuration);
						sendServiceExpiredMessage(subsInfo, membVO);
					}
					
					//365일 만료 SMS 전송 ESB
					if( DateUtils.isExpireTime(membVO.getJoinDt(), lastExpSMSDuration) 
							&& membVO.getPayResvYN().equals(MembVO.MEMB_PAY_RESV_YN_N)
							&& membVO.getLastExpSMSSendYN().equals(MembVO.LAST_MEMB_EXP_SMS_SEND_YN_N) )
					{
						logger.info("MEMB sendLastServiceExpiredMessage MEMB_ID:" + membVO.getDeviceCTN() + " isExpireTime:" + membVO.getJoinDt() + ",duration:" + lastExpSMSDuration);
						sendLastServiceExpiredMessage(subsInfo, membVO);
					}
					
					logger.debug("MEMB Expired Reserve isExpireTime:" + membVO.getJoinDt() + ",duration:" + expReserveDuration 
					+ ", diff:" + DateUtils.isExpireTime(membVO.getJoinDt(), expReserveDuration)
					+ ", membVO.getPayResvYN():" + membVO.getPayResvYN()
					+ ", membVO.getUseStatus():" + membVO.getUseStatus());
					
					//365일 해지예약 ESB
					if( DateUtils.isExpireTime(membVO.getJoinDt(), expReserveDuration) 
							&& membVO.getPayResvYN().equals(MembVO.MEMB_PAY_RESV_YN_N) 
							&& !membVO.getUseStatus().equals(MembVO.MEMB_USE_STATUS_CANCEL)
							)
					{
						logger.info("MEMB Expired Reserve sendServiceExpiredMessage MEMB_ID:" + membVO.getDeviceCTN() + " isExpireTime:" + membVO.getJoinDt() + ",duration:" + expReserveDuration);
						reservedExpiredESB(membVO);
					}
					logger.info("CALL run ======================> RUN FINISH");
					sleep(100);
				} catch (Exception e) {
					tloErrorCode = ResultCodeUtil.RC_4C005000;
					logger.error("Exception ", e);
				} finally
				{
					if( tlokey != null )
					{
						Map<String,String> tloDataMap = new HashMap<String,String>();
						tloDataMap.put(TloData.RESULT_CODE, tloErrorCode.getCode());
						
						TloUtil.setTloData(tlokey,tloDataMap);
						TLOImpl.writeTLO(tlokey);
					}
				}
			}
			logger.error("Thread Exit!! ");
		}
	}
	
	public void addQueue(MembVO membVO)
	{
		synchronized(msgVector)
		{
			msgVector.add(membVO);
		}	
	}
	
	public int getQueueCount()
	{
		return msgVector.size();
	}
	
	private void changeStatusToCancel(MembVO membVO)
	{
		// AI 인증해제
		AIAuthInfoVO aiAuthInfoVO = new AIAuthInfoVO();
		aiAuthInfoVO.setMembId(membVO.getMembId());
		AIAuthInfoVO selectAIAuthInfoVO = syncMapper.selectAIAUTHToken(aiAuthInfoVO);

		if (selectAIAuthInfoVO != null) {
			// @@@ not implement. AI Expired 처리
			ApiRequestVO apiReqVO = new ApiRequestVO();
			CommonReqVO commonReq = new CommonReqVO();
			LogData logData = new LogData();
		
			logData.setClientIp(System.getProperty("SERVER_IP"));
			logData.setDevInfo("PAD");
			logData.setOsInfo("Linux");
			logData.setNwInfo("4G");
			logData.setDevModel(membVO.getDeviceModelNM());
			logData.setCarrierType("L");
			logData.setSvcType("SVC_003");
			logData.setDevType("DEV_003");
		
			commonReq.setLogData(logData);
			apiReqVO.setCommon(commonReq);
			
			AiPlatform aiPlatformRes = aiPlatformIF.expirePlatformToken(apiReqVO, selectAIAuthInfoVO.getPlatformToken(), membVO.getDeviceCTN(), membVO.getUiccid(), 
					propertyConfig.getAiAuthUrl(), propertyConfig.getAiSvrKey());
			
			if( aiPlatformRes == null )
			{
				logger.error("aiPlatformRes Error");
			}
			
			// AI Token 삭제
			int resultRowCount = syncMapper.deleteAIAUTHToken(aiAuthInfoVO);
			logger.debug("AI_AUTH DELETE:" + resultRowCount);
		}
		
		int deleteDeviceSessCount = syncMapper.deleteDeviceSess(membVO);
		logger.debug("SESSION DELETE:" + deleteDeviceSessCount);
		
		// 상태변경
		membVO.setUseStatus(MembVO.MEMB_USE_STATUS_CANCEL);
		
		int updateMembCount = syncMapper.updateUpdateDT(membVO);
		logger.debug("changeStatusToCancel MEMB UPDATE:" + updateMembCount);
	}
	
	private void changeStatusToSuspend(MembVO membVO)
	{
		membVO.setUseStatus(MembVO.MEMB_USE_STATUS_SUSPEND);
		int updateMembCount = syncMapper.updateUpdateDT(membVO);
		logger.debug("changeStatusToSuspend MEMB UPDATE:" + updateMembCount);
	}
	
	private void changeStatusToActive(MembVO membVO)
	{
		membVO.setUseStatus(MembVO.MEMB_USE_STATUS_ACTIVE);
		int updateMembCount = syncMapper.updateUpdateDT(membVO);
		logger.debug("changeStatusToActive MEMB UPDATE:" + updateMembCount);
	}
	
	private void sendServiceExpiredMessage(SubsInfo subsInfo, MembVO membVO)
	{
		try
		{
			ESBManager esbManager = ESBManager.getInstance(propertyConfig.getEsbUrl(), propertyConfig.getEsbSystemID());
			String customerCTN = esbManager.getCustomerCTN(subsInfo.getSub_no(), membVO.getDeviceCTN());
			
			logger.info("EXP_SMS_Send CTN:" + subsInfo.getCtn() + ",SUBS_NO:" + subsInfo.getSub_no()
					+", CUSTOMER CTN:" + customerCTN);
			
			if( customerCTN != null && customerCTN.length() >= 0 )
			{
				Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
				java.util.Date ago = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
				String sendDay = formatter.format(ago);
				
				boolean sendFlag = esbManager.sendSMS(subsInfo.getSub_no(), customerCTN, expSMSText , sendDay + propertyConfig.getExpSMSSendTime(), ESBManager.REPLY_CTN);
				
				if( sendFlag )
				{
					membVO.setExpSMSSendYN(MembVO.MEMB_EXP_SMS_SEND_YN_Y);
					int updateCount = syncMapper.updateExpSMSSend(membVO);
					logger.info("EXP_SMS_Send SendFlag:" + sendFlag +", updateCount:" + updateCount);
				} 
				else
					logger.info("EXP_SMS_Send SendFlag:" + sendFlag + ",SUBS_NO:" + subsInfo.getSub_no()
					+", CUSTOMER CTN:" + customerCTN);
			} else
			{
				logger.error("Invalid CTN IS NULL! SUBS_NO:" + subsInfo.getSub_no() + ", customerCTN:" + customerCTN );
			}
		} catch( Exception ex )
		{
			logger.error("Exception", ex);
		}
		
	}
	
	private void sendLastServiceExpiredMessage(SubsInfo subsInfo, MembVO membVO)
	{
		try
		{
			ESBManager esbManager = ESBManager.getInstance(propertyConfig.getEsbUrl(), propertyConfig.getEsbSystemID());
			String customerCTN = esbManager.getCustomerCTN(subsInfo.getSub_no(), membVO.getDeviceCTN());
			
			logger.info("LAST_EXP_SMS_Send CTN:" + subsInfo.getCtn() + ",SUBS_NO:" + subsInfo.getSub_no()
					+", CUSTOMER CTN:" + customerCTN);
			
			if( customerCTN != null && customerCTN.length() >= 0 )
			{
				Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
				java.util.Date ago = cal.getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
				String sendDay = formatter.format(ago);
				
				boolean sendFlag = esbManager.sendSMS(subsInfo.getSub_no(), customerCTN, lastExpSMSText , sendDay + propertyConfig.getLastExpSMSSendTime(), ESBManager.REPLY_CTN);
				
				if( sendFlag )
				{
					membVO.setLastExpSMSSendYN(MembVO.LAST_MEMB_EXP_SMS_SEND_YN_Y);
					int updateCount = syncMapper.updateLastExpSMSSend(membVO);
					logger.info("LAST_EXP_SMS_Send SendFlag:" + sendFlag +", updateCount:" + updateCount);
				} 
				else
					logger.info("LAST_EXP_SMS_Send SendFlag:" + sendFlag + ",SUBS_NO:" + subsInfo.getSub_no()
					+", CUSTOMER CTN:" + customerCTN);
			} else
			{
				logger.error("Invalid CTN IS NULL! SUBS_NO:" + subsInfo.getSub_no() + ", customerCTN:" + customerCTN );
			}
		} catch( Exception ex )
		{
			logger.error("Exception", ex);
		}
		
	}
	
	private void reservedExpiredESB(MembVO membVO)
	{
		try
		{	
			logger.info("RESERVE Expired ESB CTN:" + membVO.getDeviceCTN());
			
			ESBTriggerVO esbTriggerVo = new ESBTriggerVO();
			esbTriggerVo.setTrigID(KeyGenerator.createCommonShardKey(Integer.parseInt(App.serverID)));
			esbTriggerVo.setDeviceCTN(membVO.getDeviceCTN());
			esbTriggerVo.setMembID(membVO.getMembId());
			esbTriggerVo.setSvrID(App.serverID);
			
			Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
			java.util.Date ago = cal.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			String sendDay = formatter.format(ago) + propertyConfig.getEsbExpireSendtime();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
			Date expiredSendDate = format.parse(sendDay);
			
			esbTriggerVo.setTrigDt(expiredSendDate);
			esbTriggerVo.setRegID("SYSTEM");
			esbTriggerVo.setTrigStatus(ESBTriggerVO.TRIGGER_STATUS_REQUEST);
			esbTriggerVo.setTrigType(SB929Interface.INTERFACE_ID);
			esbTriggerVo.setUpdID("SYSTEM");
			
			int resultCount = syncMapper.insertESBTrigger(esbTriggerVo);
			
			logger.debug("RESERVE Expired ESB insertESBTrigger count:" + resultCount);
			
		} catch( Exception ex )
		{
			logger.error("Exception", ex);
		}
		
	}

}
