package com.example.onekids_project.service.serviceimpl.attendancekids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.repository.AttendanceKidsRepository;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.response.attendancekids.AttendanceKidsStatisticalResponse;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsStatisticalService;
import com.example.onekids_project.util.ConvertData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-11 15:19
 *
 * @author lavanviet
 */
@Service
public class AttendanceKidsStatisticalServiceImpl implements AttendanceKidsStatisticalService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Override
    public AttendanceKidsStatisticalResponse attendanceKidsStatistical(Kids kids, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATE);
        }
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThanEqual(kids.getId(), startDate, endDate);
        AttendanceKidsStatisticalResponse response = new AttendanceKidsStatisticalResponse();
        this.setAttendanceCommon(attendanceKidsList, response);
        return response;
    }

    private void setAttendanceCommon(List<AttendanceKids> attendanceKidsList, AttendanceKidsStatisticalResponse response) {
        int allDay = 0;
        int allDayYes = 0;
        int allDayNo = 0;
        int morning = 0;
        int morningYes = 0;
        int morningNo = 0;
        int afternoon = 0;
        int afternoonYes = 0;
        int afternoonNo = 0;
        int evening = 0;
        int eveningYes = 0;
        int eveningNo = 0;

        int eatAllDay = 0;
        int eatMorning = 0;
        int eatMorningSecond = 0;
        int eatNoon = 0;
        int eatAfternoon = 0;
        int eatAfternoonSecond = 0;
        int eatEvening = 0;

        int minutesLate = 0;

        for (AttendanceKids x : attendanceKidsList) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(x.getAttendanceConfig(), x.getAttendanceDate());
            //điểm danh đi học
            //trường đi học ít nhất 1 buổi trong ngày
            if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isEveningAttendanceArrive()) {
                AttendanceArriveKids attendanceArriveKids = x.getAttendanceArriveKids();
                //cả ngày
                if (attendanceConfigResponse.isMorningAttendanceArrive() == attendanceArriveKids.isMorning() &&
                        attendanceConfigResponse.isAfternoonAttendanceArrive() == attendanceArriveKids.isAfternoon() &&
                        attendanceConfigResponse.isEveningAttendanceArrive() == attendanceArriveKids.isEvening()) {
                    allDay++;
                } else if (attendanceConfigResponse.isMorningAttendanceArrive() == attendanceArriveKids.isMorningYes() &&
                        attendanceConfigResponse.isAfternoonAttendanceArrive() == attendanceArriveKids.isAfternoonYes() &&
                        attendanceConfigResponse.isEveningAttendanceArrive() == attendanceArriveKids.isEveningYes()) {
                    allDayYes++;
                } else if (attendanceConfigResponse.isMorningAttendanceArrive() == attendanceArriveKids.isMorningNo() &&
                        attendanceConfigResponse.isAfternoonAttendanceArrive() == attendanceArriveKids.isAfternoonNo() &&
                        attendanceConfigResponse.isEveningAttendanceArrive() == attendanceArriveKids.isEveningNo()) {
                    allDayNo++;
                } else {
                    //sáng
                    if (attendanceConfigResponse.isMorningAttendanceArrive()) {
                        if (attendanceArriveKids.isMorning()) {
                            morning++;
                        }
                        if (attendanceArriveKids.isMorningYes()) {
                            morningYes++;
                        }
                        if (attendanceArriveKids.isMorningNo()) {
                            morningNo++;
                        }
                    }
                    //chiều
                    if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                        if (attendanceArriveKids.isAfternoon()) {
                            afternoon++;
                        }
                        if (attendanceArriveKids.isAfternoonYes()) {
                            afternoonYes++;
                        }
                        if (attendanceArriveKids.isAfternoonNo()) {
                            afternoonNo++;
                        }
                    }
                    //tối
                    if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                        if (attendanceArriveKids.isEvening()) {
                            evening++;
                        }
                        if (attendanceArriveKids.isEveningYes()) {
                            eveningYes++;
                        }
                        if (attendanceArriveKids.isEveningNo()) {
                            eveningNo++;
                        }
                    }
                }

            }
            //đếm số điểm danh ăn
            AttendanceEatKids attendanceEatKids = x.getAttendanceEatKids();
            if (attendanceConfigResponse.isMorningEat() || attendanceConfigResponse.isSecondMorningEat() || attendanceConfigResponse.isLunchEat() || attendanceConfigResponse.isAfternoonEat() || attendanceConfigResponse.isSecondAfternoonEat() || attendanceConfigResponse.isEveningEat()) {
                boolean checkEatMorning = attendanceConfigResponse.isMorningEat() ? attendanceEatKids.isBreakfast() : AppConstant.APP_TRUE;
                boolean checkEatMorningSecond = attendanceConfigResponse.isSecondMorningEat() ? attendanceEatKids.isSecondBreakfast() : AppConstant.APP_TRUE;
                boolean checkEatNoon = attendanceConfigResponse.isLunchEat() ? attendanceEatKids.isLunch() : AppConstant.APP_TRUE;
                boolean checkEatAfternoon = attendanceConfigResponse.isAfternoonEat() ? attendanceEatKids.isAfternoon() : AppConstant.APP_TRUE;
                boolean checkEatAfternoonSecod = attendanceConfigResponse.isSecondAfternoonEat() ? attendanceEatKids.isSecondAfternoon() : AppConstant.APP_TRUE;
                boolean checkEatEvening = attendanceConfigResponse.isEveningEat() ? attendanceEatKids.isDinner() : AppConstant.APP_TRUE;
                if (checkEatMorning && checkEatMorningSecond && checkEatNoon && checkEatAfternoon && checkEatAfternoonSecod && checkEatEvening) {
                    eatAllDay++;
                } else {
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
            }
            //đếm số phút điểm danh muộn
            AttendanceLeaveKids attendanceLeaveKids = x.getAttendanceLeaveKids();
            minutesLate += attendanceLeaveKids.getMinutePickupLate();
        }
        response.setMorning(morning);
        response.setMorningYes(morningYes);
        response.setMorningNo(morningNo);
        response.setAfternoon(afternoon);
        response.setAfternoonYes(afternoonYes);
        response.setAfternoonNo(afternoonNo);
        response.setEvening(evening);
        response.setEveningYes(eveningYes);
        response.setEveningNo(eveningNo);
        response.setAllDay(allDay);
        response.setAllDayYes(allDayYes);
        response.setAllDayNo(allDayNo);

        response.setEatMorning(eatMorning);
        response.setEatMorningSecond(eatMorningSecond);
        response.setEatNoon(eatNoon);
        response.setEatAfternoon(eatAfternoon);
        response.setEatAfternoonSecond(eatAfternoonSecond);
        response.setEatEvening(eatEvening);
        response.setEatAllDay(eatAllDay);

        response.setMinutesLate(minutesLate);
    }

}
