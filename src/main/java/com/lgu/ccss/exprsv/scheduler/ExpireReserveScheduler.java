package com.lgu.ccss.exprsv.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lgu.ccss.exprsv.service.ExpiredReserveService;


@Service
public class ExpireReserveScheduler {
	private final Logger logger = LoggerFactory.getLogger(ExpireReserveScheduler.class);

	@Autowired
	private ExpiredReserveService service;
	
	//@Scheduled(fixedRate=300000)
	
	//0 0/5 14,18 * * ?
	@Scheduled(cron = "${esb.delay.cron}")
	public void startWork() {

		try {
			logger.info("###### START Expired Reserve DAEMON #####");
				
			service.doTask();

		} catch (Exception e) {
			logger.error("{}", e);

		} finally {
			logger.info("###### END Expired Reserve DAEMON #####");
		}

	}
	
	
}
