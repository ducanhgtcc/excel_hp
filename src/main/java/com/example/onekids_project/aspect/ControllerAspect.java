package com.example.onekids_project.aspect;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.util.PrincipalUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * date 2021-09-23 14:49
 *
 * @author lavanviet
 */
@Aspect
@Component
public class ControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void setTextWeb(String text, JoinPoint joinPoint) {
        logger.info("Before method web {}: {}", text, joinPoint.getSignature());
    }

    private void setTextMobile(String text, JoinPoint joinPoint) {
        logger.info("Before method mobile {}: {}", text, joinPoint.getSignature());
    }

    @Before(value = "within(com.example.onekids_project.controller..*)")
    private void webCommon(JoinPoint joinPoint) {
        this.setTextWeb("common", joinPoint);
    }

    @Before(value = "within(com.example.onekids_project.supperpluscontroller..*)")
    private void webSupperPlus(JoinPoint joinPoint) {
        this.setTextWeb("supper plus", joinPoint);
        CommonValidate.checkDataSupperPlus(PrincipalUtils.getUserPrincipal());
    }

    @Before(value = "within(com.example.onekids_project.master.controller..*)")
    private void webMaster(JoinPoint joinPoint) {
        this.setTextWeb("master", joinPoint);
        CommonValidate.checkDataAdmin(PrincipalUtils.getUserPrincipal());
    }

    @Before(value = "within(com.example.onekids_project.parentcontroller..*)")
    private void webParent(JoinPoint joinPoint) {
        this.setTextWeb("parent", joinPoint);
        CommonValidate.checkDataParent(PrincipalUtils.getUserPrincipal());
    }

    @Before(value = "execution(* com.example.onekids_project.mobile.plus.controller..*(..))")
    private void mobilePlus(JoinPoint joinPoint) {
        this.setTextMobile(AppTypeConstant.SCHOOL, joinPoint);
        CommonValidate.checkDataPlus(PrincipalUtils.getUserPrincipal());
    }

    @Before(value = "execution(* com.example.onekids_project.mobile.teacher.controller..*(..))")
    private void mobileTeacher(JoinPoint joinPoint) {
        this.setTextMobile(AppTypeConstant.TEACHER, joinPoint);
        CommonValidate.checkDataTeacherNoClass(PrincipalUtils.getUserPrincipal());
    }

    @Before(value = "execution(* com.example.onekids_project.mobile.parent.controller..*(..))")
    private void mobileParent(JoinPoint joinPoint) {
        this.setTextMobile(AppTypeConstant.PARENT, joinPoint);
        CommonValidate.checkDataParent(PrincipalUtils.getUserPrincipal());
    }

    @Before(value = "execution(* com.example.onekids_project.commoncontroller.CreateDataController..*(..))")
    private void checkSystem(JoinPoint joinPoint) {
        this.setTextWeb(AppTypeConstant.SYSTEM, joinPoint);
        CommonValidate.checkDataSystem(PrincipalUtils.getUserPrincipal());
    }
}
