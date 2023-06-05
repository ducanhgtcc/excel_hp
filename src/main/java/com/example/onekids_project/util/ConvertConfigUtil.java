package com.example.onekids_project.util;

import com.example.onekids_project.mobile.parent.response.schoolconfig.AbsentConfig;
import com.example.onekids_project.mobile.teacher.response.home.AttendanceKidsConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EvaluateConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.LearnConfig;
import com.example.onekids_project.mobile.teacher.response.home.CommonConfigTeacher;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;

public class ConvertConfigUtil {
    /**
     * set learn config
     *
     * @param schoolConfigResponse
     * @return
     */
    public static LearnConfig setLearnConfig(SchoolConfigResponse schoolConfigResponse) {
        LearnConfig learnConfig = new LearnConfig();
        learnConfig.setMorning(schoolConfigResponse.isMorningAttendanceArrive());
        learnConfig.setAfternoon(schoolConfigResponse.isAfternoonAttendanceArrive());
        learnConfig.setEvening(schoolConfigResponse.isEveningAttendanceArrive());
        learnConfig.setMorningSaturday(schoolConfigResponse.isMorningSaturday());
        learnConfig.setAfternoonSaturday(schoolConfigResponse.isAfternoonSaturday());
        learnConfig.setEveningSaturday(schoolConfigResponse.isEveningSaturday());
        learnConfig.setSunday(schoolConfigResponse.isSunday());
        return learnConfig;
    }

    /**
     * set evaluate config
     *
     * @param schoolConfigResponse
     */
    public static EvaluateConfig setEvaluateConfig(SchoolConfigResponse schoolConfigResponse) {
        EvaluateConfig evaluateConfig = new EvaluateConfig();
        evaluateConfig.setDate(schoolConfigResponse.isEvaluateDate());
        evaluateConfig.setWeek(schoolConfigResponse.isEvaluateWeek());
        evaluateConfig.setMonth(schoolConfigResponse.isEvaluateMonth());
        evaluateConfig.setPeriodic(schoolConfigResponse.isEvaluatePeriod());
        return evaluateConfig;
    }

    /**
     * set ngày nghỉ và thời gian xin nghỉ phải trước mấy giờ
     *
     * @param schoolConfigResponse
     */
    public static AbsentConfig setAbsentConfig(SchoolConfigResponse schoolConfigResponse) {
        AbsentConfig absentConfig = new AbsentConfig();
        absentConfig.setNumber(schoolConfigResponse.getDateAbsent());
        absentConfig.setTime(schoolConfigResponse.getTimeAbsent());
        return absentConfig;
    }

    public static AttendanceKidsConfig setAttendanceConfig(SchoolConfigResponse schoolConfigResponse){
        AttendanceKidsConfig model=new AttendanceKidsConfig();
        model.setAgainDateNumber(schoolConfigResponse.getAgainAttendance());
        return model;
    }

    public static CommonConfigTeacher setCommonConfigTeacher(SchoolConfigResponse schoolConfigResponse){
        CommonConfigTeacher model=new CommonConfigTeacher();
        model.setParentInfo(schoolConfigResponse.isParentInfo());
        return model;
    }
}
