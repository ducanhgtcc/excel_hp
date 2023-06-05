package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.entity.classes.ScheduleAfternoon;
import com.example.onekids_project.entity.classes.ScheduleEvening;
import com.example.onekids_project.entity.classes.ScheduleMorning;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleDateTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ScheduleDateTeacherService;
import com.example.onekids_project.repository.ClassScheduleRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleDateTeacherServiceImpl implements ScheduleDateTeacherService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private MaClassRepository maClassRepository;

    @Override
    public ScheduleDateTeacherResponse findScheduleforDay(@CurrentUser UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        ScheduleDateTeacherResponse model = new ScheduleDateTeacherResponse(); // create model for ScheduleDataResponse
        Long idSchool = principal.getIdSchoolLogin();   //  get Long idSchool
        Long idClass = principal.getIdClassLogin();     //  get Long idClass
        List<ScheduleTeacherResponse> morningList = new ArrayList<>();
        List<ScheduleTeacherResponse> afternoonList = new ArrayList<>();
        List<ScheduleTeacherResponse> eveningList = new ArrayList<>();
        ClassSchedule classSchedule = classScheduleRepository.findScheduleDate(idSchool, idClass, localDate);     // get ClassSchedule follow ScheduleRepository
        if (classSchedule != null){
            List<ScheduleMorning> scheduleMorningList = classSchedule.getScheduleMorningList();
            List<ScheduleAfternoon> scheduleAfternoonList = classSchedule.getScheduleAfternoonList();
            List<ScheduleEvening> scheduleEveningList = classSchedule.getScheduleEveningList();

            if (!CollectionUtils.isEmpty(scheduleMorningList)) {
                morningList = listMapper.mapList(scheduleMorningList, ScheduleTeacherResponse.class);
            }
            if (!CollectionUtils.isEmpty(scheduleAfternoonList)) {
                afternoonList = listMapper.mapList(scheduleAfternoonList, ScheduleTeacherResponse.class);
            }
            if (!CollectionUtils.isEmpty(scheduleMorningList)) {
                eveningList = listMapper.mapList(scheduleEveningList, ScheduleTeacherResponse.class);
            }
            model.setTitle(StringUtils.isNotBlank(classSchedule.getScheduleTitle()) ? classSchedule.getScheduleTitle() : "");
        }else {
            model.setTitle("");
        }
        model.setMorningList(morningList);
        model.setAfternoonList(afternoonList);
        model.setEveningList(eveningList);
        return model;
    }

    @Override
    public List<Integer> findClassScheduleMonthList(@CurrentUser UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        List<Integer> scheduleDaysMonth = new ArrayList<>();
        List<ClassSchedule> classScheduleList = classScheduleRepository.findClassScheduleMonthList(idSchool, idClass, localDate.getMonthValue(), localDate.getYear());
        classScheduleList.forEach(x -> {
            List<ScheduleMorning> scheduleMorningList = x.getScheduleMorningList();
            List<ScheduleAfternoon> scheduleAfternoonList = x.getScheduleAfternoonList();
            List<ScheduleEvening> scheduleEveningList = x.getScheduleEveningList();
            if (!CollectionUtils.isEmpty(scheduleMorningList) || !CollectionUtils.isEmpty(scheduleAfternoonList) || !CollectionUtils.isEmpty(scheduleEveningList)) {
                scheduleDaysMonth.add(x.getScheduleDate().getDayOfMonth());
            }
        });
        return scheduleDaysMonth;
    }
}
