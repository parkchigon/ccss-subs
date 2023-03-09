package com.lgu.ccss.sync.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lgu.ccss.sync.service.SyncService;

@Service
public class SyncScheduler {
	private final Logger logger = LoggerFactory.getLogger(SyncScheduler.class);

	@Autowired
	private SyncService syncService;
	
	//@Scheduled(fixedRate=300000)
	
	//0 0/5 14,18 * * ?
	@Scheduled(cron = "${delay.cron}")
	public void startWork() {

		try {
			logger.info("###### START SYNC DAEMON #####");
				
			syncService.doTask();

		} catch (Exception e) {
			logger.error("{}", e);

		} finally {
			logger.info("###### END SYNC DAEMON #####");
		}

		// logger.info("$$$$$$$" +
		// CommMessageUtil.getMessage("result_success"));
	}
	
	
}
