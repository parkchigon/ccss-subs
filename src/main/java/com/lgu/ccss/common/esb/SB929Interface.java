package com.lgu.ccss.common.esb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.BusinessHeader;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.CreateTelnoExpryRsv;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.CreateTelnoExpryRsvResponse;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.DsExpryRsvInfoInVO;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.DsExpryRsvInfoOutVO;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.ESBHeader;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.RequestBody;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.RequestRecord;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.ResponseBody;
import lguplus.u3.webservice.sb929.CreateTelnoExpryRsvServiceStub.ResponseRecord;

public class SB929Interface {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unused")
	private String ESB_URL = null;
	private String ESB_SYSTEM_ID = null;
	
	public static String INTERFACE_ID = "SB929";
	
	public static String ESB_SB929_PATH = "/CSSI/SB/CreateTelnoExpryRsv";
	
	public static final String RESULT_CODE_ESBIF_SUCCESS 		= "N0000";
	public static final String RESULT_CODE_ESBIF_CHANGE_RATE2	= "2000";
	public static final String RESULT_CODE_ESBIF_NO_MEMB		= "4000";
	public static final String RESULT_CODE_ESBIF_ERROR			= "5000";
	public static final String RESULT_CODE_ETC					= "9999";
	
	
	
	public SB929Interface(String url, String systemID)
	{
		this.ESB_URL = url;
		this.ESB_SYSTEM_ID = systemID;
	}
	
	public String sendExpireReserve(String pdnCTN)
	{
		String resultCode = null;
		
		RequestRecord reqRecord = new RequestRecord();    
		RequestBody reqBody = new RequestBody();
	      
		DsExpryRsvInfoInVO[] dsExpryRsvInfoIn = new DsExpryRsvInfoInVO[1];
	    dsExpryRsvInfoIn[0] = new DsExpryRsvInfoInVO();
	    dsExpryRsvInfoIn[0].setSkipYn("N");
	    dsExpryRsvInfoIn[0].setProdNo(pdnCTN);
	    dsExpryRsvInfoIn[0].setDeleteTypeFlag("N");
	    dsExpryRsvInfoIn[0].setNextOperatorId(ESBManager.createNextOperatorId());
	    dsExpryRsvInfoIn[0].set_applicationId(INTERFACE_ID);
	    
	    reqBody.setDsExpryRsvInfoInVO(dsExpryRsvInfoIn);        
	    reqRecord.setRequestBody(reqBody);
	     
	    
		// ESBHeader(ServiceID,TransactionID,SystemID,ErrCode,ErrMsg,Reserved)
		// setting
		ESBHeader header = new ESBHeader();
		header.setServiceID(INTERFACE_ID);
		header.setTransactionID(ESBManager.createTxId());  //ex) "201103050901019991234567"
	    header.setSystemID(this.ESB_SYSTEM_ID);   //Client SystemId
	    header.setErrCode("");
	    header.setErrMsg("");
	    header.setReserved("");
	    reqRecord.setESBHeader(header);
	    ResponseRecord result = null ;
	    try {
	       
		    CreateTelnoExpryRsv reqIn = new CreateTelnoExpryRsv();
		    reqIn.setRequestRecord(reqRecord);
		    
		    //targetEndpoint 반드시 설정해야 함  (CSSI ex -> ESBUTIL.getServerURL() + "/CSSI/OSO/RetrieveTskMidProc")
		    CreateTelnoExpryRsvServiceStub stub = new CreateTelnoExpryRsvServiceStub(this.ESB_URL + ESB_SB929_PATH);//this.ESB_URL +"/CSSI/SB/CreateTelnoExpryRsv"
		    CreateTelnoExpryRsvResponse res = stub.createTelnoExpryRsv(reqIn);
		    result = res.getResponseRecord();
		      
		    header = result.getESBHeader();
		    //header.getErrCode().equals("") Success
		    if("".equals(header.getErrCode())) {
		    
		    	BusinessHeader bizHeader = result.getBusinessHeader();
		    	//bizHeader.getResultCode().equals("N0000") Success
		    	resultCode = bizHeader.getResultCode();
		        ResponseBody resBody = result.getResponseBody();
		        if(resBody != null) {
		        	DsExpryRsvInfoOutVO[] dsExpryRsvInfoOut = resBody.getDsExpryRsvInfoOutVO();
		        	
		        	if( dsExpryRsvInfoOut != null && dsExpryRsvInfoOut.length > 1 )
		        	{
		        		resultCode = dsExpryRsvInfoOut[0].getResultCode();
		        	}
		        }
		    } else
		    {
		    	resultCode = header.getErrCode();
		    	logger.error("ESBHeader errorcode:" + header.getErrCode());
		    }
	    }catch(Exception ex)
	    {
	    	resultCode = RESULT_CODE_ESBIF_ERROR;
	    	logger.error("Exception", ex);
	    }
	    
	    return resultCode;
	}
	
}
