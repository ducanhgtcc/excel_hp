package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.entity.classes.ScheduleAfternoon;
import com.example.onekids_project.entity.classes.ScheduleEvening;
import com.example.onekids_project.entity.classes.ScheduleMorning;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleParentResponse;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleWeekParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleWeekParentService;
import com.example.onekids_project.repository.ClassScheduleRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleWeekParentServiceImpl implements ScheduleWeekParentService {

    @Autowired
    KidsRepository kidsRepository;

    @Autowired
    ClassScheduleRepository classScheduleRepository;
    @Autowired
    private ListMapper listMapper;

    @Override
    public List<ScheduleWeekParentResponse> findScheduleWeek(UserPrincipal userPrincipal, LocalDate localDate) {
        List<ScheduleWeekParentResponse> listScheduleWeekParentResponse = new ArrayList<>();
        Long idSchool = userPrincipal.getIdSchoolLogin();
        Long idKid = userPrincipal.getIdKidLogin();     //  get Long idKid
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));

//        String title="";
        for (int i = 0; i < 7; i++) {
            ScheduleWeekParentResponse scheduleWeekParentResponse = new ScheduleWeekParentResponse();
            ClassSchedule classSchedule = classScheduleRepository.findScheduleDate(idSchool, kids.getMaClass().getId(), localDate.plusDays(i));
            List<ScheduleParentResponse> morningList = new ArrayList<>();
            List<ScheduleParentResponse> afternoonList = new ArrayList<>();
            List<ScheduleParentResponse> eveningList = new ArrayList<>();
            if (classSchedule != null) {
                scheduleWeekParentResponse.setDate(classSchedule.getScheduleDate().toString());
                scheduleWeekParentResponse.setTitle(!Strings.isEmpty(classSchedule.getScheduleTitle()) ? classSchedule.getScheduleTitle() : "");
                // List Schedule Morning, Afternoon, Evening
                List<ScheduleMorning> scheduleMorningList = classSchedule.getScheduleMorningList();
                List<ScheduleAfternoon> scheduleAfternoonList = classSchedule.getScheduleAfternoonList();
                List<ScheduleEvening> scheduleEveningList = classSchedule.getScheduleEveningList();
                if (!CollectionUtils.isEmpty(scheduleMorningList)) {
                    morningList = listMapper.mapList(scheduleMorningList, ScheduleParentResponse.class);
                }
                if (!CollectionUtils.isEmpty(scheduleAfternoonList)) {
                    afternoonList = listMapper.mapList(scheduleAfternoonList, ScheduleParentResponse.class);
                }
                if (!CollectionUtils.isEmpty(scheduleEveningList)) {
                    eveningList = listMapper.mapList(scheduleEveningList, ScheduleParentResponse.class);
                }

                scheduleWeekParentResponse.setMorningList(morningList);
                scheduleWeekParentResponse.setAfternoonList(afternoonList);
                scheduleWeekParentResponse.setEveningList(eveningList);
                if (!CollectionUtils.isEmpty(scheduleWeekParentResponse.getMorningList()) || !CollectionUtils.isEmpty(scheduleWeekParentResponse.getAfternoonList())
                        && !CollectionUtils.isEmpty(scheduleWeekParentResponse.getEveningList())) {
                    listScheduleWeekParentResponse.add(scheduleWeekParentResponse);
                }
            }
        }
        return listScheduleWeekParentResponse;
    }
}
