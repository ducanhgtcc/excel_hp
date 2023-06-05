package com.example.onekids_project.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * date 2021-09-22 15:40
 *
 * @author lavanviet
 */
@Aspect
@Component
public class TestAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Pointcut(value = "execution(* com.example.onekids_project.controller.MaClassController..*(..)) ")
//    private void logDisplayingEmployee() {
//    }


//    @Before(value = "execution(* *..controller..*(..))")
//    private void logDisplayingEmployee3(JoinPoint joinPoint) {
//        logger.info("Before method common: {}", joinPoint.getSignature());
//    }

//    @Before(value = "logDisplayingEmployee()")
//    public void aroundAdvice(JoinPoint joinPoint) {
//        logger.info("Before method: {}", joinPoint.getSignature());
//    }

//    @Pointcut(value = "execution(* com.example.onekids_project.cronjobs.AttendanceKidsCronjobs.testA())")
//    private void logDisplayingEmployee1() {
//    }
//
//    @Around(value = "logDisplayingEmployee1()")
//    public Object aroundAdvice1(ProceedingJoinPoint joinPoint) throws Throwable {
//        final long start = System.currentTimeMillis();
//        logger.info("Before around method: {}", joinPoint.getSignature());
//        final Object proceed = joinPoint.proceed();
//        final long executionTime = System.currentTimeMillis() - start;
//        logger.info("After around method: {}, Executed in time: {} ms", joinPoint.getSignature().getName(), executionTime);
//        return proceed;
//    }
}
