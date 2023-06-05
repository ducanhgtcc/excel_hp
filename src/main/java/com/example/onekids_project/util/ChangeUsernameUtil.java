package com.example.onekids_project.util;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import org.apache.commons.lang3.StringUtils;

public class ChangeUsernameUtil {
    public static String changeUsername(String appType) {
        if (StringUtils.isNotBlank(appType)) {
            if (AppTypeConstant.SCHOOL.equals(appType)) {
                return AppConstant.USERNAME_PLUS;
            }
            if (AppTypeConstant.TEACHER.equals(appType)) {
                return AppConstant.USERNAME_TEACHER;
            }
            if (AppTypeConstant.PARENT.equals(appType)) {
                return AppConstant.USERNAME_PARENT;
            }
        }
        return "";
    }
}
