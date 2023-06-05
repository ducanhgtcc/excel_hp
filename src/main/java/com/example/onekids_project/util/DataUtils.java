package com.example.onekids_project.util;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ConfigConstant;
import com.example.onekids_project.enums.EmployeeStatusEnum;
import com.example.onekids_project.security.payload.JwtDataObject;
import com.example.onekids_project.util.objectdata.StartEndDate;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class DataUtils {

    //Lấy Giá trị trạng thái Nhân viên trong ENum
    public static Map<String, String> getEmployeeStatus() {
        Map<String, String> results = new HashMap<>();
		/*
		Stream.of(EmployeeSatusEnum.values()).forEach(item->{
			results.put(item.name(),item.getValue());
		});
		*/
        for (EmployeeStatusEnum item : EmployeeStatusEnum.values()) {
            results.put(item.name(), item.getValue());
        }
        return results;
    }

    //Hàm sinh mã code cho trường
    private static final String SCHOOL = "SC";
    private static final String KID = "KD";
    private static final String STAFF = "ST";
    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
    private static final String specials = "~=+%^*/()[]{}/!@#$?|";
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
    private static final String ALL = alpha + alphaUpperCase + digits + specials;

    public static String randomSchoolCode() {
        StringBuilder schoolCode = new StringBuilder();
        String StrRandom = RandomStringUtils.random(3, true, false);
        schoolCode.append(SCHOOL).append(StrRandom.toUpperCase());
        return schoolCode.toString();
    }

    /**
     * lấy danh sách các tháng tính từ ngày truyền vào
     *
     * @return
     */
    public static List<JwtDataObject> getMonthList() {
        List<JwtDataObject> jwtDataObjectList = new ArrayList<>();
        StartEndDate model = getStartEndDate(ConfigConstant.MONTH_PAST, ConfigConstant.MONTH_FUTURE);
        LocalDate startDate = model.getStartDate();
        LocalDate start = LocalDate.of(startDate.getYear(), startDate.getMonthValue(), 1);
        do {
            JwtDataObject jwtDataObject = new JwtDataObject();
            String monthString = start.getMonthValue() < 10 ? "0" + start.getMonthValue() : "" + start.getMonthValue();
            jwtDataObject.setKey(start);
            jwtDataObject.setValue("Tháng " + monthString + "/" + start.getYear());
            if (LocalDate.now().getYear() == start.getYear() && LocalDate.now().getMonthValue() == start.getMonthValue()) {
                jwtDataObject.setStatus(AppConstant.APP_TRUE);
            }
            jwtDataObjectList.add(jwtDataObject);
            start = start.plusMonths(1);
        } while (start.isBefore(model.getEndDate()));
        jwtDataObjectList = jwtDataObjectList.stream().sorted(Comparator.comparing(JwtDataObject::getKey).reversed()).collect(Collectors.toList());
        return jwtDataObjectList;
    }

    /**
     * lấy danh sách các tuần tính từ ngày truyền vào
     *
     * @return
     */
    public static List<JwtDataObject> getWeekList() {
        List<JwtDataObject> jwtDataObjectList = new ArrayList<>();
        StartEndDate model = getStartEndDate(ConfigConstant.MONTH_PAST, ConfigConstant.MONTH_FUTURE);
        LocalDate monday = model.getStartDate();
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        do {
            int week = monday.minusDays(1).get(WeekFields.of(Locale.getDefault()).weekOfYear());
            JwtDataObject jwtDataObject = new JwtDataObject();
            week = convertWeek(monday, week);
            String weekString = week < 10 ? "0" + week : "" + week;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String datetStart = formatter.format(monday);
            String datetEnd = formatter.format(monday.plusDays(6));
            jwtDataObject.setKey(monday);
            jwtDataObject.setValue("Tuần " + weekString + " (" + datetStart + " - " + datetEnd + ")");
            if (LocalDate.now().isAfter(monday.minusDays(1)) && LocalDate.now().isBefore(monday.plusDays(7))) {
                jwtDataObject.setStatus(AppConstant.APP_TRUE);
            }
            jwtDataObjectList.add(jwtDataObject);
            monday = monday.plusDays(7);
        } while (monday.isBefore(model.getEndDate().plusDays(1)));
        jwtDataObjectList = jwtDataObjectList.stream().sorted(Comparator.comparing(JwtDataObject::getKey).reversed()).collect(Collectors.toList());
        return jwtDataObjectList;
    }

    /**
     * lấy danh sách các tuần tính từ ngày truyền vào
     *
     * @return
     */
    public static List<JwtDataObject> getWeekWebList() {
        List<JwtDataObject> jwtDataObjectList = new ArrayList<>();
        StartEndDate model = getStartEndDate(ConfigConstant.MONTH_PAST_WEB, ConfigConstant.MONTH_FUTURE_WEB);
        LocalDate monday = model.getStartDate();
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        do {
            int week = monday.minusDays(1).get(WeekFields.of(Locale.getDefault()).weekOfYear());
            JwtDataObject jwtDataObject = new JwtDataObject();
            week = convertWeek(monday, week);
            String weekString = week < 10 ? "0" + week : "" + week;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String datetStart = formatter.format(monday);
            String datetEnd = formatter.format(monday.plusDays(6));
            jwtDataObject.setKey(monday);
            jwtDataObject.setValue("Tuần " + weekString + " (" + datetStart + " - " + datetEnd + ")");
            if (LocalDate.now().isAfter(monday.minusDays(1)) && LocalDate.now().isBefore(monday.plusDays(7))) {
                jwtDataObject.setStatus(AppConstant.APP_TRUE);
            }
            jwtDataObjectList.add(jwtDataObject);
            monday = monday.plusDays(7);
        } while (monday.isBefore(model.getEndDate().plusDays(1)));
        return jwtDataObjectList;
    }

    /**
     * lấy ngày bắt đầu và ngày kết thúc
     *
     * @return
     */
    private static StartEndDate getStartEndDate(int startMonth, int endMonth) {
        StartEndDate model = new StartEndDate();
        LocalDate nowDate = LocalDate.now();
        LocalDate pastDate = nowDate.minusMonths(startMonth);
        LocalDate futureDate = nowDate.plusMonths(endMonth);
//
//        List<LocalDate> dateList = Arrays.asList(createdDate, startDate);
//        LocalDate maxDate = dateList.stream().max(LocalDate::compareTo).get();
//
//        LocalDate start = maxDate.isBefore(pastDate) ? pastDate : maxDate;
        model.setStartDate(pastDate);
        model.setEndDate(futureDate);

        return model;
    }

    public static int convertWeek(LocalDate date, int week) {
        if (date.getYear() == 2020) {
            return week;
        }
        if (date.getYear() == 2023) {
            return week;
        }
        if (date.getYear() == 2024) {
            if (date.isEqual(LocalDate.of(2024, 1, 1))) {
                return 1;
            } else {
                return week;
            }
        }
        return week - 1;
    }

}
