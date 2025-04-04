package com.yoanan.foreignexchangeapp.service.impl;

import com.yoanan.foreignexchangeapp.controller.TransactionController;
import com.yoanan.foreignexchangeapp.model.entity.LogEntity;
import com.yoanan.foreignexchangeapp.repository.LogRepository;
import com.yoanan.foreignexchangeapp.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    // TODO refactor LogService to be universal
    private Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void createLog(String requestUrl, String methodName, String className, String arguments) {
        if (requestUrl != null  && methodName != null && className != null && arguments != null) {
            LocalDateTime localDateTime = LocalDateTime.now();
            LogEntity logEntity = new LogEntity(requestUrl, localDateTime, methodName, className, arguments);
            logRepository.save(logEntity);
        }
    }

    @Override
    @Scheduled(cron = "${log-info.clear-cron}") // At 12:02 PM, on day 1 of the month, every 6 months
    public void clearAllLogs() {
        LOGGER.info("Clear all logs info...");
        logRepository.deleteAll();
    }
}
