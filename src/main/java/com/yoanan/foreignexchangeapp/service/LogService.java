package com.yoanan.foreignexchangeapp.service;

import java.time.LocalDateTime;

public interface LogService {

    void createLog(String requestUrl, String methodName, String className, String arguments);

    void clearAllLogs();
}
