package com.example.onekids_project.util;

import com.example.onekids_project.common.MobileConstant;
import org.apache.commons.lang3.StringUtils;

public class StringDataUtils {

    public static String getSubStringLarge(String str) {
        return getSubString(str, MobileConstant.MAX_LENGHT_TEXT_LARGE);
    }

    public static String getSubStringSmall(String str) {
        return getSubString(str, MobileConstant.MAX_LENGHT_TEXT_SMALL);
    }

    public static String getSubStringCustom(String str, int length) {
        return getSubString(str, length);
    }

    private static String getSubString(String str, int length) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        str = str.trim();
        return str.length() <= length ? str : str.substring(0, length);
    }
}
