package com.lgu.ccss.common.esb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.RequestRecord;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.ResponseBody;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.RequestBody;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.BusinessHeader;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.DsInputInVO;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.DsOutputOutVO;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.ESBHeader;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.ResponseRecord;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.RetrieveInetCustTelno;
import lguplus.u3.webservice.sb609.RetrieveInetCustTelnoServiceStub.RetrieveInetCustTelnoResponse;


public class SB609Interface {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String ESB_URL = null;
	private String ESB_SYSTEM_ID = null;
	
	
	public SB609Interface(String url, String systemID)
	{
		this.ESB_URL = url;
		this.ESB_SYSTEM_ID = systemID;
	}
	
	public String getCustomerCTN(String subsNo)
	{
		RequestRecord reqRecord = new RequestRecord();	   
	   	RequestBody reqBody = new RequestBody();
	   	
		DsInputInVO dsInputIn = new DsInputInVO();
		dsInputIn.setEntrNo(subsNo);
		dsInputIn.setNextOperatorId(ESBManager.createNextOperatorId());
		reqBody.setDsInputInVO(dsInputIn);
	   	reqRecord.setRequestBody(reqBody);

        //ESBHeader(ServiceID,TransactionID,SystemID,ErrCode,ErrMsg,Reserved) setting
	   	ESBHeader header = new ESBHeader();
		header.setServiceID("SB609");
		header.setTransactionID(ESBManager.createTxId());  //ex) "201103050901019991234567"
		header.setSystemID(this.ESB_SYSTEM_ID);   //Client SystemId
		header.setErrCode("");
		header.setErrMsg("");
		header.setReserved("");
		reqRecord.setESBHeader(header);

		String customerCTN = null;
	   	ResponseRecord result = null ;
	   	
	   	try {
		   	
			RetrieveInetCustTelno reqIn = new RetrieveInetCustTelno();
			reqIn.setRequestRecord(reqRecord);
			//targetEndpoint 반드시 설정해야 함  (CSSI ex -> ESBUTIL.getServerURL() + "/CSSI/OSO/RetrieveTskMidProc")
			RetrieveInetCustTelnoServiceStub stub = new RetrieveInetCustTelnoServiceStub(this.ESB_URL + RetrieveInetCustTelnoServiceStub.ESB_SB609_PATH);
			RetrieveInetCustTelnoResponse res = stub.retrieveInetCustTelno(reqIn);
			result = res.getResponseRecord();
		   
			header = result.getESBHeader();
			
			

			//header.getErrCode().equals("") Success
		   	if("".equals(header.getErrCode())) {
			
				BusinessHeader bizHeader = result.getBusinessHeader();
				//bizHeader.getResultCode().equals("N0000") Success
			   
				if( bizHeader.getResultCode().equals(ESBConstant.SUCCESS) )
				{
				   	ResponseBody resBody = result.getResponseBody();
				   	if( resBody != null) {
				   	
				   		DsOutputOutVO dsOutputOut = resBody.getDsOutputOutVO();
	
						if ( dsOutputOut != null ){ 
							
							logger.debug("[SB609] CustNo:" + dsOutputOut.getCustNo());
							logger.debug("[SB609] CustNm:" + dsOutputOut.getCustNm());
							logger.debug("[SB609] Hpno:" + dsOutputOut.getHpno());
							logger.debug("[SB609] EmailAddr:" + dsOutputOut.getEmailAddr());
							
							customerCTN = dsOutputOut.getHpno();
						} else
						{
							logger.error("[SB609] dsOutputOut NULL");
						}
				   	} else
				   		logger.error("[SB609] resBody NULL");
				} else
				{
					logger.error("[SB609] resultCode:" + bizHeader.getResultCode());
				}
		   	}else{
		   		logger.error("[SB609] ErrCode:" + header.getErrCode());
		   	}
	   	}
	   	catch(Exception re) {
	   		logger.error("[SB609] Exception", re);
	   	}
	   	
	   	return customerCTN;
	}
}
