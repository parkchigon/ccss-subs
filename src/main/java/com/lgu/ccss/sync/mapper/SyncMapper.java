package com.lgu.ccss.sync.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgu.ccss.common.model.AIAuthInfoVO;
import com.lgu.ccss.common.model.ESBTriggerVO;
import com.lgu.ccss.common.model.MembVO;


@Component
public class SyncMapper {

	@Autowired
	SyncMapperOracle sampleMapperOracle;

	@Autowired
	SyncMapperAltibase sampleMapperAltibase;

	public List<MembVO> selectMembStatusSche(MembVO membVO) {
		return sampleMapperOracle.selectMembStatusSche(membVO);
	}

	public int deleteDeviceSess(MembVO membVO) {
		return sampleMapperAltibase.deleteDeviceSess(membVO);
	}
	
	public int updateUpdateDT(MembVO membVO){
		return sampleMapperOracle.updateUpdateDT(membVO);
	}
	
	public AIAuthInfoVO selectAIAUTHToken(AIAuthInfoVO aiAuthInfoVO){
		return sampleMapperOracle.selectAIAUTHToken(aiAuthInfoVO);
	}
	
	public int deleteAIAUTHToken(AIAuthInfoVO aiAuthInfoVO) {
		return sampleMapperOracle.deleteAIAUTHToken(aiAuthInfoVO);
	}
	
	public int updateExpSMSSend(MembVO membVO)
	{
		return sampleMapperOracle.updateExpSMSSend(membVO);
	}
	
	public int updateLastExpSMSSend(MembVO membVO)
	{
		return sampleMapperOracle.updateLastExpSMSSend(membVO);
	}
	
	public int insertESBTrigger(ESBTriggerVO esbTriggerVo)
	{
		return sampleMapperOracle.insertESBTrigger(esbTriggerVo);
	}
	
	public List<ESBTriggerVO> selectExpiredReserveList(ESBTriggerVO esbTriggerVo)
	{
		return sampleMapperOracle.selectExpiredReserveList(esbTriggerVo);
	}
	
	public MembVO selectMember(MembVO membVo)
	{
		return sampleMapperOracle.selectMember(membVo);
	}
	
	public int updateExpiredResult(ESBTriggerVO esbTriggerVO)
	{
		return sampleMapperOracle.updateExpiredResult(esbTriggerVO);
	}
	
	// 닛산 LEAF 20190128 kangjin
	public String getConnDeviceModel(String membId)
	{
		return sampleMapperOracle.getConnDeviceModel(membId);
	}		
}
