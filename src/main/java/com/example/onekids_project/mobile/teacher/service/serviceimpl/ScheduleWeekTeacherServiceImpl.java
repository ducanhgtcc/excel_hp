package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.entity.classes.ScheduleAfternoon;
import com.example.onekids_project.entity.classes.ScheduleEvening;
import com.example.onekids_project.entity.classes.ScheduleMorning;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleWeekTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ScheduleWeekTeacherService;
import com.example.onekids_project.repository.ClassScheduleRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleWeekTeacherServiceImpl implements ScheduleWeekTeacherService {

    @Autowired
    ClassScheduleRepository classScheduleRepository;

    @Autowired
    ListMapper listMapper;

    @Override
    public List<ScheduleWeekTeacherResponse> findScheduleWeek(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        List<ScheduleWeekTeacherResponse> scheduleWeekResponseList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            ScheduleWeekTeacherResponse scheduleWeekTeacherResponse = new ScheduleWeekTeacherResponse();
            ClassSchedule classSchedule = classScheduleRepository.findScheduleDate(idSchool, idClass, localDate.plusDays(i));
            if (classSchedule != null) {
                scheduleWeekTeacherResponse.setDate(classSchedule.getScheduleDate().toString());
                scheduleWeekTeacherResponse.setTitle(!Strings.isEmpty(classSchedule.getScheduleTitle())?classSchedule.getScheduleTitle():"");
                // List Schedule Morning, Afternoon, Evening
                List<ScheduleMorning> scheduleMorningList = new ArrayList<>();
                List<ScheduleAfternoon> scheduleAfternoonList = new ArrayList<>();
                List<ScheduleEvening> scheduleEveningList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(classSchedule.getScheduleMorningList())) {
                    scheduleMorningList = classSchedule.getScheduleMorningList();
                }
                if (!CollectionUtils.isEmpty(classSchedule.getScheduleAfternoonList())) {
                    scheduleAfternoonList = classSchedule.getScheduleAfternoonList();
                }
                if (!CollectionUtils.isEmpty(classSchedule.getScheduleEveningList())) {
                    scheduleEveningList = classSchedule.getScheduleEveningList();
                }
                List<ScheduleTeacherResponse> morningList = listMapper.mapList(scheduleMorningList, ScheduleTeacherResponse.class);
                scheduleWeekTeacherResponse.setMorningList(morningList);
                List<ScheduleTeacherResponse> afternoonList = listMapper.mapList(scheduleAfternoonList, ScheduleTeacherResponse.class);
                scheduleWeekTeacherResponse.setAfternoonList(afternoonList);
                List<ScheduleTeacherResponse> eveningList = listMapper.mapList(scheduleEveningList, ScheduleTeacherResponse.class);
                scheduleWeekTeacherResponse.setEveningList(eveningList);
                if (!CollectionUtils.isEmpty(scheduleWeekTeacherResponse.getMorningList()) || !CollectionUtils.isEmpty(scheduleWeekTeacherResponse.getAfternoonList())
                        && !CollectionUtils.isEmpty(scheduleWeekTeacherResponse.getEveningList())) {
                    scheduleWeekResponseList.add(scheduleWeekTeacherResponse);
                }
            }
        }
        return scheduleWeekResponseList;
    }
}
