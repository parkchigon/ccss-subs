package com.lgu.ccss.exprsv.service;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lgu.ccss.App;
import com.lgu.ccss.common.esb.ESBManager;
import com.lgu.ccss.common.esb.SB929Interface;
import com.lgu.ccss.common.model.ESBTriggerVO;
import com.lgu.ccss.common.model.MembVO;
import com.lgu.ccss.config.PropertyConfig;
import com.lgu.ccss.sync.mapper.SyncMapper;
import com.lgu.common.util.DateUtils;

@Service
public class ExpiredReserveServiceImpl implements ExpiredReserveService{
	private final Logger logger = LoggerFactory.getLogger(ExpiredReserveServiceImpl.class);
	
	@Autowired
	private SyncMapper syncMapper;
	
	@Autowired
	private PropertyConfig propertyConfig;
	
	@Value("#{config['db.expiredreserve.select.count']}")
	private String membRownum;
	
	
	public void doTask() {
		
		try{
			//expired reserve 검색
			
			ESBTriggerVO expireReserveSearchVo = new ESBTriggerVO();
			expireReserveSearchVo.setTrigDt(DateUtils.getCurrentDate());
			expireReserveSearchVo.setSvrID(App.serverID);
			expireReserveSearchVo.setTrigStatus(ESBTriggerVO.TRIGGER_STATUS_REQUEST);
			expireReserveSearchVo.setRownum(Integer.parseInt(membRownum));
			
			List<ESBTriggerVO> searchList =  syncMapper.selectExpiredReserveList(expireReserveSearchVo);
			
			if( searchList == null )
			{
				logger.info("Expire Reserve MEMB is null");
				return;
			}
			
			logger.debug("MEMB\r\n" + searchList);
			
			
			Iterator<ESBTriggerVO> iter = searchList.iterator();
			ESBTriggerVO esbTriggerVo = null;
			while( iter.hasNext() )
			{
				esbTriggerVo = (ESBTriggerVO)iter.next();
				
				if( esbTriggerVo.getTrigType().equals(SB929Interface.INTERFACE_ID) )
				{
					MembVO searchMembVO = new MembVO();
					searchMembVO.setMembId(esbTriggerVo.getMembID());
					
					MembVO membVO = syncMapper.selectMember(searchMembVO);
					
					if( membVO == null )
					{
						logger.error("Invalid Member MembID : " + esbTriggerVo.getMembID() + "\r\n" + esbTriggerVo);
						
						ESBTriggerVO resultVO = new ESBTriggerVO();
						resultVO.setTrigID(esbTriggerVo.getTrigID());
						resultVO.setTrigStatus(ESBTriggerVO.TRIGGER_STATUS_FAIL);
						resultVO.setResultCode(SB929Interface.RESULT_CODE_ESBIF_NO_MEMB);
						resultVO.setUpdID("SYSTEM");
						
						int updateRow = syncMapper.updateExpiredResult(resultVO);
						logger.error("Invalid Member MembID : " + esbTriggerVo.getMembID() + ", updateRow:" + updateRow);
						
						continue;
					}
					//String result = null;
					if( membVO.getPayResvYN().equals(MembVO.MEMB_PAY_RESV_YN_Y) )
					{
						logger.info("Pay Resv YES PayResvYN:" + membVO.getPayResvYN() );

						ESBTriggerVO resultVO = new ESBTriggerVO();
						resultVO.setTrigID(esbTriggerVo.getTrigID());
						resultVO.setTrigStatus(ESBTriggerVO.TRIGGER_STATUS_SUCCESS);
						resultVO.setResultCode(SB929Interface.RESULT_CODE_ESBIF_CHANGE_RATE2);
						resultVO.setUpdID("SYSTEM");
						
						int updateRow = syncMapper.updateExpiredResult(resultVO);
						logger.info("Pay Resv YES Member MembID : " + esbTriggerVo.getMembID() + ", updateRow:" + updateRow);
					} else
					{
						logger.info("Expired Reserve CTN : " +  esbTriggerVo.getDeviceCTN());
						ESBManager esbManager = ESBManager.getInstance(propertyConfig.getEsbUrl(), propertyConfig.getEsbSystemID());
						String resultCode = esbManager.sendExpireReserve(esbTriggerVo.getDeviceCTN());
						
						ESBTriggerVO resultVO = new ESBTriggerVO();
						resultVO.setTrigID(esbTriggerVo.getTrigID());
						if( resultCode != null && resultCode.equals(SB929Interface.RESULT_CODE_ESBIF_SUCCESS) )
						{
							resultVO.setTrigStatus(ESBTriggerVO.TRIGGER_STATUS_SUCCESS);
						} else
						{
							resultVO.setTrigStatus(ESBTriggerVO.TRIGGER_STATUS_FAIL);
							
						}
						resultVO.setResultCode(resultCode);
						resultVO.setUpdID("SYSTEM");
						
						int updateRow = syncMapper.updateExpiredResult(resultVO);
						logger.info("Expired Reserve CTN : " +  esbTriggerVo.getDeviceCTN() +", resultCode:" + resultCode + ", updateRow:" + updateRow);
					}
				} else
				{
					logger.error("Invalid Trigger Type : " + esbTriggerVo.getTrigType() + "\r\n" + esbTriggerVo);
				}
			}
		}catch(Exception e){
			logger.error("Exception !! :" ,e);
		}
	}
	
}
