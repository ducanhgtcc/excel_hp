package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.system.SysInfor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lavanviet
 */
public class SysInforUtil {
    public static int getLimitDeviceNumber(SysInfor sysInfor, String appType) {
        if (StringUtils.equals(appType, AppTypeConstant.PARENT)) {
            return sysInfor.getLimitDeviceParentNumber();
        } else if (StringUtils.equals(appType, AppTypeConstant.TEACHER)) {
            return sysInfor.getLimitDeviceTeacherNumber();
        } else if (StringUtils.equals(appType, AppTypeConstant.SCHOOL)) {
            return sysInfor.getLimitDevicePlusNumber();
        }
        return 0;
    }
}
