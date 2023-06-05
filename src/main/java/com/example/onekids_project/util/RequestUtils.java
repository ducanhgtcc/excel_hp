package com.example.onekids_project.util;

import com.example.onekids_project.security.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtils {
    private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    public static void getFirstRequest(UserPrincipal principal, Object... o) {
        logger.info("id: {}, username: {}, fullName: {}, idSchool: {}, requestData: {}", principal.getId(), principal.getUsername(), principal.getFullName(), principal.getIdSchoolLogin(), o);
    }

    public static void getFirstRequestExtend(UserPrincipal principal, Object o1, Object o2) {
        logger.info("id: {}, username: {}, fullName: {}, idSchool: {}, requestData1: {}, requestData2: {}", principal.getId(), principal.getUsername(), principal.getFullName(), principal.getIdSchoolLogin(), o1, o2);
    }

    public static void getFirstRequestPlus(UserPrincipal principal, Object... o) {
        logger.info("id: {}, username: {}, fullName: {}, idSchool: {}, requestData: {}", principal.getId(), principal.getUsername(), principal.getFullName(), principal.getIdSchoolLogin(), o);
    }

    public static void getFirstRequestTeacher(UserPrincipal principal, Object... o) {
        logger.info("id: {}, username: {}, fullName: {}, idSchool: {}, idKid: {}, requestData: {}", principal.getId(), principal.getUsername(), principal.getFullName(), principal.getIdSchoolLogin(), principal.getIdClassLogin(), o);
    }

    public static void getFirstRequestParent(UserPrincipal principal, Object... o) {
        logger.info("id: {}, username: {}, fullName: {}, idSchool: {}, idKid: {}, requestData: {}", principal.getId(), principal.getUsername(), principal.getFullName(), principal.getIdSchoolLogin(), principal.getIdSchoolLogin(), o);
    }
}
