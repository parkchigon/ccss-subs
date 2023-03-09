package com.lgu.ccss.sync.mapper;

import com.lgu.ccss.common.model.MembVO;
import com.lgu.ccss.config.annontation.Slave;

@Slave
public interface SyncMapperAltibase {
	int deleteDeviceSess(MembVO membVO);
}
