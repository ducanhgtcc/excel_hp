package com.example.onekids_project.util;

import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.enums.DateEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

public class DateCommonUtils {

    /**
     * chuyển một ngày bất kỳ trong tuần về ngày thứ 2
     *
     * @param date
     * @return
     */
    public static LocalDate getMonday(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().toString();
        if (DateEnum.TUESDAY.toString().equals(dayOfWeek)) {
            date = date.minusDays(1);
        } else if (DateEnum.WEDNESDAY.toString().equals(dayOfWeek)) {
            date = date.minusDays(2);
        } else if (DateEnum.THURSDAY.toString().equals(dayOfWeek)) {
            date = date.minusDays(3);
        } else if (DateEnum.FRIDAY.toString().equals(dayOfWeek)) {
            date = date.minusDays(4);
        } else if (DateEnum.SATURDAY.toString().equals(dayOfWeek)) {
            date = date.minusDays(5);
        } else if (DateEnum.SUNDAY.toString().equals(dayOfWeek)) {
            date = date.minusDays(6);
        }
        return date;
    }

    /**
     * check ngày truyền vào với ngày hiện tại
     *
     * @param date
     */
    public static void checkDateWithCurrentDate(LocalDate date) {
        if (date != null) {
            if (date.isAfter(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.DATE_AFTER);
            }
        }
    }

    public static Integer getMonthMax(int year) {
        LocalDate nowDate = LocalDate.now();
        int nowYear = nowDate.getYear();
        if (year > nowYear) {
            return null;
        } else if (year == nowYear) {
            return nowDate.getMonthValue();
        } else {
            return 12;
        }
    }
}
