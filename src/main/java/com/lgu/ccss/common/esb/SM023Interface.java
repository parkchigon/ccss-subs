package com.lgu.ccss.common.esb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.BusinessHeader;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.CreateSmsSend;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.CreateSmsSendBDInVO;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.CreateSmsSendResponse;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.ESBHeader;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.RequestBody;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.RequestRecord;
import lguplus.u3.webservice.sm023.CreateSmsSendServiceStub.ResponseRecord;

public class SM023Interface {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String ESB_URL = null;
	private String ESB_SYSTEM_ID = null;
	
	public SM023Interface(String url, String systemID)
	{
		this.ESB_URL = url;
		this.ESB_SYSTEM_ID = systemID;
	}
	
	public boolean sendSMS(String subsNo, String ctn, String smsMessage, String sendTime, String replyNo)
	{
		RequestRecord reqRecord = new RequestRecord();	   
	   	RequestBody reqBody = new RequestBody();
	   	
		CreateSmsSendBDInVO CreateSmsSendBDIn = new CreateSmsSendBDInVO();
		CreateSmsSendBDIn.setEntrNo(subsNo);
		CreateSmsSendBDIn.setCtn(ctn);
		CreateSmsSendBDIn.setSmsSendPhr(smsMessage);
		
		if( sendTime != null && sendTime.length() > 0)
		{
			CreateSmsSendBDIn.setOnlnYn(CreateSmsSend.ONLN_YN_Y);
			CreateSmsSendBDIn.setRtmYn(CreateSmsSend.RTM_YN_N);
			CreateSmsSendBDIn.setSendRqstDttm(sendTime);
		} else
		{
			CreateSmsSendBDIn.setOnlnYn(CreateSmsSend.ONLN_YN_N);
			CreateSmsSendBDIn.setRtmYn(CreateSmsSend.RTM_YN_Y);
		}
		
		CreateSmsSendBDIn.setRplyNo(replyNo);
		CreateSmsSendBDIn.setSendRsnCd(CreateSmsSend.SENDRSN_CD);
		CreateSmsSendBDIn.setApplDvCd(CreateSmsSend.APPLDV_CD);
		CreateSmsSendBDIn.setSendFormCd(CreateSmsSend.SENDFORM_CD);
		
		CreateSmsSendBDIn.setLtpymYn(CreateSmsSend.LTOYM_YN_N);
			
		CreateSmsSendBDIn.setNextOperatorId(ESBManager.createNextOperatorId());
		reqBody.setCreateSmsSendBDInVO(CreateSmsSendBDIn);		  	   

	   	reqRecord.setRequestBody(reqBody);

        //ESBHeader(ServiceID,TransactionID,SystemID,ErrCode,ErrMsg,Reserved) setting
	   	ESBHeader header = new ESBHeader();
			header.setServiceID("SM023");
			header.setTransactionID(ESBManager.createTxId());  //ex) "201103050901019991234567"
			header.setSystemID(ESB_SYSTEM_ID);
			header.setErrCode("");
			header.setErrMsg("");
			header.setReserved("");
			reqRecord.setESBHeader(header);

	   	ResponseRecord result = null ;
	   	
	   	boolean sendFlag = false;
	   	
	   	try {
		   	
			CreateSmsSend reqIn = new CreateSmsSend();
			reqIn.setRequestRecord(reqRecord);
			
			
			//targetEndpoint 반드시 설정해야 함  (CSSI ex -> ESBUTIL.getServerURL() + "/CSSI/OSO/RetrieveTskMidProc")
			CreateSmsSendServiceStub stub = new CreateSmsSendServiceStub(this.ESB_URL + CreateSmsSendServiceStub.ESB_SB023_PATH);
			CreateSmsSendResponse res = stub.createSmsSend(reqIn);
			result = res.getResponseRecord();
		   
			header = result.getESBHeader();

			//header.getErrCode().equals("") Success
		   	if("".equals(header.getErrCode())) {
			
				BusinessHeader bizHeader = result.getBusinessHeader();
				
				if( bizHeader.getResultCode().equals(ESBConstant.SUCCESS))
					sendFlag = true;
				else
					logger.error("[ESB SB023] resultCode:" +  bizHeader.getResultCode());
					
		   	}else{
		   		logger.error("[ESB SB023] ErrCode:" +  header.getErrCode());
		   	}

		}
		catch(Exception ce) {
			logger.error("[ESB SB023] Exception", ce);
		}
	   	
	   	return sendFlag;
	}

}
