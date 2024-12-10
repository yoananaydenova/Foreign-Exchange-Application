package com.yoanan.foreignexchangeapp.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yoanan.foreignexchangeapp.controller.TransactionController;
import com.yoanan.foreignexchangeapp.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;


@Aspect
@Component
public class LoggingAdvice {

    private static final String POINTCUT = "within(com.yoanan.foreignexchangeapp.controller.*)";

    Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final LogService logService;

    public LoggingAdvice(LogService logService) {
        this.logService = logService;
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "e")
    public void logAfterException(JoinPoint jp, Exception e) throws JsonProcessingException {

        //Get HttpServletRequest object get related data in the request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestUrl = request.getRequestURL().toString();

        String methodName = jp.getSignature().getName();

        String className = jp.getTarget().getClass().getSimpleName();

        log.error("Exception during: {} with ex: {}", constructLogMsg(jp), e.toString());

        logService.createLog(requestUrl, methodName, className, e.toString());
    }

    private String constructLogMsg(JoinPoint jp) {
        var args = Arrays.asList(jp.getArgs()).stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        var sb = new StringBuilder("@");
        sb.append(method.getName());
        sb.append(":");
        sb.append(args);
        return sb.toString();
    }

}
