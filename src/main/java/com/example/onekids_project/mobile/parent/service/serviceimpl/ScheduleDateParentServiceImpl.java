package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.entity.classes.ScheduleAfternoon;
import com.example.onekids_project.entity.classes.ScheduleEvening;
import com.example.onekids_project.entity.classes.ScheduleMorning;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleDateParentResponse;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleDateParentService;
import com.example.onekids_project.repository.ClassScheduleRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleDateParentServiceImpl implements ScheduleDateParentService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public ScheduleDateParentResponse findScheduleforDay(@CurrentUser UserPrincipal userPrincipal, LocalDate localDate) {
        ScheduleDateParentResponse model = new ScheduleDateParentResponse(); // create model for ScheduleDataResponse
        Long idSchool = userPrincipal.getIdSchoolLogin();   //  get Long idSchool
        Long idKid = userPrincipal.getIdKidLogin();     //  get Long idKid
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));     //  get kids by IdAndDelActiveTrue
        ClassSchedule classSchedule = classScheduleRepository.findScheduleDate(idSchool, kids.getMaClass().getId(), localDate);      // get ClassSchedule follow ScheduleRepository
        List<ScheduleParentResponse> morningList = new ArrayList<>();
        List<ScheduleParentResponse> afternoonList = new ArrayList<>();
        List<ScheduleParentResponse> eveningList = new ArrayList<>();
        if (classSchedule != null) {
            List<ScheduleMorning> scheduleMorningList = classSchedule.getScheduleMorningList();
            List<ScheduleAfternoon> scheduleAfternoonList = classSchedule.getScheduleAfternoonList();
            List<ScheduleEvening> scheduleEveningList = classSchedule.getScheduleEveningList();
            if (!CollectionUtils.isEmpty(scheduleMorningList)){
                morningList = listMapper.mapList(scheduleMorningList, ScheduleParentResponse.class);
            }
            if (!CollectionUtils.isEmpty(scheduleAfternoonList)){
                afternoonList = listMapper.mapList(scheduleAfternoonList, ScheduleParentResponse.class);
            }
            if (!CollectionUtils.isEmpty(scheduleEveningList)){
                eveningList = listMapper.mapList(scheduleEveningList, ScheduleParentResponse.class);
            }
            model.setTitle(StringUtils.isNotEmpty(classSchedule.getScheduleTitle()) ? classSchedule.getScheduleTitle() : "");
        }else {
            model.setTitle("");
        }
        model.setMorningList(morningList);
        model.setAfternoonList(afternoonList);
        model.setEveningList(eveningList);
        return model;
    }

    @Override
    public List<Integer> findClassScheduleMonthList(UserPrincipal principal, LocalDate localDate) {
        Long idKid = principal.getIdKidLogin();     //  get Long idKid
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        Long idClass = kids.getMaClass().getId();
        List<ClassSchedule> classScheduleList = classScheduleRepository.findClassScheduleMonthList(principal.getIdSchoolLogin(), idClass, localDate.getMonthValue(), localDate.getYear());
        List<Integer> scheduleDaysMonth = new ArrayList<>();
        for (ClassSchedule eleClassSchedule : classScheduleList) {
            List<ScheduleMorning> scheduleMorningList = eleClassSchedule.getScheduleMorningList();
            List<ScheduleAfternoon> scheduleAfternoonList = eleClassSchedule.getScheduleAfternoonList();
            List<ScheduleEvening> scheduleEveningList = eleClassSchedule.getScheduleEveningList();
            if (!CollectionUtils.isEmpty(scheduleMorningList) || !CollectionUtils.isEmpty(scheduleAfternoonList) || !CollectionUtils.isEmpty(scheduleEveningList)) {
                scheduleDaysMonth.add(eleClassSchedule.getScheduleDate().getDayOfMonth());
            }
        }
        return scheduleDaysMonth;
    }
}


