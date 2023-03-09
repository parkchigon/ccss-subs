package com.lgu.ccss.sync.mapper;

import java.util.List;

import com.lgu.ccss.config.annontation.Master;
import com.lgu.ccss.common.model.AIAuthInfoVO;
import com.lgu.ccss.common.model.ESBTriggerVO;
import com.lgu.ccss.common.model.MembVO;

@Master
public interface SyncMapperOracle {
	List<MembVO> selectMembStatusSche(MembVO membVO);
	int updateUpdateDT(MembVO membVO);
	AIAuthInfoVO selectAIAUTHToken(AIAuthInfoVO aiAuthInfoVO);
	int deleteAIAUTHToken(AIAuthInfoVO aiAuthInfoVO);
	int updateExpSMSSend(MembVO membVO);
	int updateLastExpSMSSend(MembVO membVO);
	public int insertESBTrigger(ESBTriggerVO esbTriggerVo);
	public List<ESBTriggerVO> selectExpiredReserveList(ESBTriggerVO esbTriggerVo);
	public MembVO selectMember(MembVO membVo);
	public int updateExpiredResult(ESBTriggerVO esbTriggerVO);
	// 닛산 LEAF 20190128 kangjin
	public String getConnDeviceModel(String membId);	
}
