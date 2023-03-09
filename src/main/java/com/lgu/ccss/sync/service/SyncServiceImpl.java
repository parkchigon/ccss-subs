package com.lgu.ccss.sync.service;


import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lgu.ccss.App;
import com.lgu.ccss.common.model.MembVO;
import com.lgu.ccss.sync.mapper.SyncMapper;
import com.lgu.ccss.sync.service.Impl.worker.WorkerThread;
import com.lgu.common.util.DateUtils;



@Service
public class SyncServiceImpl implements SyncService{
	private final Logger logger = LoggerFactory.getLogger(SyncServiceImpl.class);
	
	@Autowired
	private SyncMapper syncMapper;
	
	@Value("#{config['worker.threadCnt']}")
	private String threadCnt;
	
	@Value("#{config['db.memb.select.count']}")
	private String membRownum;
	
	@Value("#{config['db.memb.lastlogindt.duration']}")
	private String lastlogindt;
	
	@Value("#{config['db.memb.updatedt.duration']}")
	private String updatedt;
	
	private static WorkerThread[] workerThreadArr = null;
	
	public void doTask() {
		
		try{
			if( workerThreadArr == null )
			{
				makeWorkerThread();
			}
			
			int notCompleteQueueCount = this.getThreadQueueCount();
			if( notCompleteQueueCount > 0 )
			{
				logger.info("Before job not complete! queueCount:" + notCompleteQueueCount);
				return;
			}
			
			//oralce DB 조회
			MembVO searchMembVO = new MembVO();
			searchMembVO.setLatestLoginDt(DateUtils.getCurrentDate(Calendar.HOUR_OF_DAY, -1 * Integer.parseInt(lastlogindt) ));
			searchMembVO.setSyncDt(DateUtils.getCurrentDate(Calendar.HOUR_OF_DAY, -1 * Integer.parseInt(updatedt)));
			searchMembVO.setRownum(Integer.parseInt(membRownum));
			searchMembVO.setServerID(App.serverID);
			searchMembVO.setMarketType(MembVO.MEMB_MACKET_TYPE_AM);
			
			List<MembVO> searchMembList = syncMapper.selectMembStatusSche(searchMembVO);			
			
			if( searchMembList == null )
				return;
			
			logger.info("MEMB\r\n" + searchMembList);
			
			for( int i = 0; i < searchMembList.size(); i++ )
			{
				workerThreadArr[i%Integer.parseInt(threadCnt)].addQueue(searchMembList.get(i));
			}
			
			
		}catch(Exception e){
			logger.error("Exception !! :" ,e);
		}
	}
	
	public void makeWorkerThread ()
	{
		workerThreadArr = new WorkerThread[Integer.parseInt(threadCnt)];
		for (int i = 0; i < Integer.parseInt(threadCnt); i++) 
		{
			WorkerThread workerThread = new WorkerThread (i + 1);
			workerThread.setWorkerNum(i);
			workerThreadArr[i]=workerThread;
			logger.info("Make WorkerThread " + i);
		}
	}
	
	public int getThreadQueueCount()
	{
		int queueCount = 0;
		
		for (int i = 0; i < Integer.parseInt(threadCnt); i++) 
		{
			queueCount = queueCount + workerThreadArr[i].getQueueCount();
		}
		
		return queueCount;
	}
}
