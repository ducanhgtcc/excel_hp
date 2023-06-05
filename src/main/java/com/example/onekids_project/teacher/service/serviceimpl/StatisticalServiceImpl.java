package com.example.onekids_project.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.AttendanceArriveKids;
import com.example.onekids_project.entity.kids.AttendanceEatKids;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.repository.AttendanceKidsRepository;
import com.example.onekids_project.repository.GradeRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.teacher.response.statistical.StatisticalAttendanceArrive;
import com.example.onekids_project.teacher.response.statistical.StatisticalAttendanceEat;
import com.example.onekids_project.teacher.response.statistical.StatisticalDataResponse;
import com.example.onekids_project.teacher.service.servicecustom.StatisticalService;
import com.example.onekids_project.util.ConvertData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-04-19 10:41
 *
 * @author lavanviet
 */
@Service
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public StatisticalDataResponse getStatisticalData(UserPrincipal principal, LocalDate date) {
        StatisticalDataResponse response = new StatisticalDataResponse();
        Long idSchool = principal.getIdSchoolLogin();
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByIdSchoolAndAttendanceDate(idSchool, date);
        response.setStatisticalAttendanceArriveGradeList(this.getAttendanceArriveGrade(attendanceKidsList));
        response.setStatisticalAttendanceArriveClassList(this.getAttendanceArriveClass(attendanceKidsList));
        response.setStatisticalAttendanceEatGradeList(this.getAttendanceEatGrade(attendanceKidsList));
        response.setStatisticalAttendanceEatClassList(this.getAttendanceEatClass(attendanceKidsList));
        return response;
    }

    private List<StatisticalAttendanceArrive> getAttendanceArriveGrade(List<AttendanceKids> attendanceKidsList) {
        List<StatisticalAttendanceArrive> dataList = new ArrayList<>();
        List<Long> longList = attendanceKidsList.stream().map(AttendanceKids::getIdGrade).distinct().collect(Collectors.toList());
        List<Grade> gradeList = gradeRepository.findByIdIn(longList);
        gradeList.forEach(a -> {
            StatisticalAttendanceArrive model = new StatisticalAttendanceArrive();
            List<AttendanceArriveKids> arriveGradeList = attendanceKidsList.stream().filter(x -> x.getIdGrade().equals(a.getId())).map(AttendanceKids::getAttendanceArriveKids).collect(Collectors.toList());
            long countGoSchool = arriveGradeList.stream().filter(x -> x.isMorning() || x.isAfternoon() || x.isEvening()).count();
            long countNoAttendance = arriveGradeList.stream().filter(x -> !x.isMorning() && !x.isAfternoon() && !x.isEvening() && !x.isMorningYes() && !x.isAfternoonYes() && !x.isEveningYes() && !x.isMorningNo() && !x.isAfternoonNo() && !x.isEveningNo()).count();
            model.setId(a.getId());
            model.setName(a.getGradeName());
            model.setStudyingNumber(arriveGradeList.size());
            model.setGoSchoolNumber((int) countGoSchool);
            model.setNoAttendance((int) countNoAttendance);
            model.setAbsentNumber(model.getStudyingNumber() - model.getGoSchoolNumber() - model.getNoAttendance());
            dataList.add(model);
        });
        return dataList;
    }

    private List<StatisticalAttendanceArrive> getAttendanceArriveClass(List<AttendanceKids> attendanceKidsList) {
        List<StatisticalAttendanceArrive> dataList = new ArrayList<>();
        List<MaClass> maClassList = attendanceKidsList.stream().map(AttendanceKids::getMaClass).distinct().collect(Collectors.toList());
        maClassList.forEach(a -> {
            StatisticalAttendanceArrive model = new StatisticalAttendanceArrive();
            List<AttendanceArriveKids> arriveClassList = attendanceKidsList.stream().filter(x -> x.getMaClass().equals(a)).map(AttendanceKids::getAttendanceArriveKids).collect(Collectors.toList());
            long countGoSchool = arriveClassList.stream().filter(x -> x.isMorning() || x.isAfternoon() || x.isEvening()).count();
            long countNoAttendance = arriveClassList.stream().filter(x -> !x.isMorning() && !x.isAfternoon() && !x.isEvening() && !x.isMorningYes() && !x.isAfternoonYes() && !x.isEveningYes() && !x.isMorningNo() && !x.isAfternoonNo() && !x.isEveningNo()).count();
            model.setId(a.getId());
            model.setName(a.getClassName());
            model.setStudyingNumber(arriveClassList.size());
            model.setGoSchoolNumber((int) countGoSchool);
            model.setNoAttendance((int) countNoAttendance);
            model.setAbsentNumber(model.getStudyingNumber() - model.getGoSchoolNumber() - model.getNoAttendance());
            dataList.add(model);
        });
        return dataList;
    }

    private List<StatisticalAttendanceEat> getAttendanceEatGrade(List<AttendanceKids> attendanceKidsList) {
        List<StatisticalAttendanceEat> dataList = new ArrayList<>();
        if (attendanceKidsList.size() == 0) {
            return dataList;
        }
        List<Long> longList = attendanceKidsList.stream().map(AttendanceKids::getIdGrade).distinct().collect(Collectors.toList());
        List<Grade> gradeList = gradeRepository.findByIdIn(longList);
        int eatAllDay = 0;
        int eatMorning = 0;
        int eatMorningSecond = 0;
        int eatNoon = 0;
        int eatAfternoon = 0;
        int eatAfternoonSecond = 0;
        int eatEvening = 0;
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKidsList.get(0).getAttendanceConfig(), attendanceKidsList.get(0).getAttendanceDate());
        for (Grade a : gradeList) {
            StatisticalAttendanceEat model = new StatisticalAttendanceEat();
            List<AttendanceEatKids> leaveFilterList = attendanceKidsList.stream().filter(x -> x.getIdGrade().equals(a.getId())).map(AttendanceKids::getAttendanceEatKids).collect(Collectors.toList());
            for (AttendanceEatKids attendanceEatKids : leaveFilterList) {
                boolean checkEatMorning = attendanceConfigResponse.isMorningEat() ? attendanceEatKids.isBreakfast() : AppConstant.APP_TRUE;
                boolean checkEatMorningSecond = attendanceConfigResponse.isSecondMorningEat() ? attendanceEatKids.isSecondBreakfast() : AppConstant.APP_TRUE;
                boolean checkEatNoon = attendanceConfigResponse.isLunchEat() ? attendanceEatKids.isLunch() : AppConstant.APP_TRUE;
                boolean checkEatAfternoon = attendanceConfigResponse.isAfternoonEat() ? attendanceEatKids.isAfternoon() : AppConstant.APP_TRUE;
                boolean checkEatAfternoonSecod = attendanceConfigResponse.isSecondAfternoonEat() ? attendanceEatKids.isSecondAfternoon() : AppConstant.APP_TRUE;
                boolean checkEatEvening = attendanceConfigResponse.isEveningEat() ? attendanceEatKids.isDinner() : AppConstant.APP_TRUE;
                if (checkEatMorning && checkEatMorningSecond && checkEatNoon && checkEatAfternoon && checkEatAfternoonSecod && checkEatEvening) {
                    eatAllDay++;
                }
                if (attendanceConfigResponse.isMorningEat() && attendanceEatKids.isBreakfast()) {
                    eatMorning++;
                }
                if (attendanceConfigResponse.isSecondMorningEat() && attendanceEatKids.isSecondBreakfast()) {
                    eatMorningSecond++;
                }
                if (attendanceConfigResponse.isLunchEat() && attendanceEatKids.isLunch()) {
                    eatNoon++;
                }
                if (attendanceConfigResponse.isAfternoonEat() && attendanceEatKids.isAfternoon()) {
                    eatAfternoon++;
                }
                if (attendanceConfigResponse.isSecondAfternoonEat() && attendanceEatKids.isSecondAfternoon()) {
                    eatAfternoonSecond++;
                }
                if (attendanceConfigResponse.isEveningEat() && attendanceEatKids.isDinner()) {
                    eatEvening++;
                }
            }
            model.setId(a.getId());
            model.setName(a.getGradeName());
            model.setAllDay(eatAllDay);
            model.setBreakfast(eatMorning);
            model.setSecondBreakfast(eatMorningSecond);
            model.setLunch(eatNoon);
            model.setAfternoon(eatAfternoon);
            model.setSecondAfternoon(eatAfternoonSecond);
            model.setDinner(eatEvening);
            dataList.add(model);
        }
        return dataList;
    }

    private List<StatisticalAttendanceEat> getAttendanceEatClass(List<AttendanceKids> attendanceKidsList) {
        List<StatisticalAttendanceEat> dataList = new ArrayList<>();
        if (attendanceKidsList.size() == 0) {
            return dataList;
        }
        List<MaClass> maClassList = attendanceKidsList.stream().map(AttendanceKids::getMaClass).distinct().collect(Collectors.toList());
        int eatAllDay = 0;
        int eatMorning = 0;
        int eatMorningSecond = 0;
        int eatNoon = 0;
        int eatAfternoon = 0;
        int eatAfternoonSecond = 0;
        int eatEvening = 0;
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKidsList.get(0).getAttendanceConfig(), attendanceKidsList.get(0).getAttendanceDate());
        for (MaClass a : maClassList) {
            StatisticalAttendanceEat model = new StatisticalAttendanceEat();
            List<AttendanceEatKids> leaveFilterList = attendanceKidsList.stream().filter(x -> x.getMaClass().equals(a)).map(AttendanceKids::getAttendanceEatKids).collect(Collectors.toList());
            for (AttendanceEatKids attendanceEatKids : leaveFilterList) {
                boolean checkEatMorning = attendanceConfigResponse.isMorningEat() ? attendanceEatKids.isBreakfast() : AppConstant.APP_TRUE;
                boolean checkEatMorningSecond = attendanceConfigResponse.isSecondMorningEat() ? attendanceEatKids.isSecondBreakfast() : AppConstant.APP_TRUE;
                boolean checkEatNoon = attendanceConfigResponse.isLunchEat() ? attendanceEatKids.isLunch() : AppConstant.APP_TRUE;
                boolean checkEatAfternoon = attendanceConfigResponse.isAfternoonEat() ? attendanceEatKids.isAfternoon() : AppConstant.APP_TRUE;
                boolean checkEatAfternoonSecod = attendanceConfigResponse.isSecondAfternoonEat() ? attendanceEatKids.isSecondAfternoon() : AppConstant.APP_TRUE;
                boolean checkEatEvening = attendanceConfigResponse.isEveningEat() ? attendanceEatKids.isDinner() : AppConstant.APP_TRUE;
                if (checkEatMorning && checkEatMorningSecond && checkEatNoon && checkEatAfternoon && checkEatAfternoonSecod && checkEatEvening) {
                    eatAllDay++;
                }
                if (attendanceConfigResponse.isMorningEat() && attendanceEatKids.isBreakfast()) {
                    eatMorning++;
                }
                if (attendanceConfigResponse.isSecondMorningEat() && attendanceEatKids.isSecondBreakfast()) {
                    eatMorningSecond++;
                }
                if (attendanceConfigResponse.isLunchEat() && attendanceEatKids.isLunch()) {
                    eatNoon++;
                }
                if (attendanceConfigResponse.isAfternoonEat() && attendanceEatKids.isAfternoon()) {
                    eatAfternoon++;
                }
                if (attendanceConfigResponse.isSecondAfternoonEat() && attendanceEatKids.isSecondAfternoon()) {
                    eatAfternoonSecond++;
                }
                if (attendanceConfigResponse.isEveningEat() && attendanceEatKids.isDinner()) {
                    eatEvening++;
                }
            }
            model.setId(a.getId());
            model.setName(a.getClassName());
            model.setAllDay(eatAllDay);
            model.setBreakfast(eatMorning);
            model.setSecondBreakfast(eatMorningSecond);
            model.setLunch(eatNoon);
            model.setAfternoon(eatAfternoon);
            model.setSecondAfternoon(eatAfternoonSecond);
            model.setDinner(eatEvening);
            dataList.add(model);
        }
        return dataList;
    }
}
