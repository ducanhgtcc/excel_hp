package com.example.onekids_project.aspect;

import com.example.onekids_project.util.PrincipalUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * date 2021-09-23 11:40
 *
 * @author lavanviet
 */
@Aspect
@Component
public class CommonAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("@annotation(com.example.onekids_project.annotation.CommonAspectAnnotation)")
    public Object aroundTrackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        logger.info("Before method aspect: {}", joinPoint.getSignature());
        final Object proceed = joinPoint.proceed();
        final long executionTime = System.currentTimeMillis() - start;
        logger.info("After method aspect: {}, Executed in time: {} ms", joinPoint.getSignature().getName(), executionTime);
        return proceed;
    }
}
