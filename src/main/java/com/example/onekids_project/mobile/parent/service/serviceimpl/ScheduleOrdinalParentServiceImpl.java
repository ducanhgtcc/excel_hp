package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.entity.classes.ClassSchedule;
import com.example.onekids_project.entity.classes.ScheduleFile;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleOrdinalParentService;
import com.example.onekids_project.repository.ClassScheduleRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.ScheduleFileRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-05-28 2:05 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class ScheduleOrdinalParentServiceImpl implements ScheduleOrdinalParentService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    ScheduleFileRepository scheduleFileRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public List<String> scheduleOrdinalParent(UserPrincipal principal, LocalDate localDate) {
        Long idSchool = principal.getIdSchoolLogin();   //  get Long idSchool
        Long idKid = principal.getIdKidLogin();     //  get Long idKid
        boolean day ,week ,file;
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        ClassSchedule classScheduleDay = classScheduleRepository.findScheduleDate(idSchool, kids.getMaClass().getId(), localDate);
        if(classScheduleDay != null){
            day = localDate.equals(classScheduleDay.getScheduleDate());
        }else {
            day = false;
        }
        List<Long> idWeekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ClassSchedule classScheduleWeek = classScheduleRepository.findScheduleDate(idSchool, kids.getMaClass().getId(), localDate.plusDays(i));
            if(classScheduleWeek != null){
                if(classScheduleWeek.getId() != null){
                    idWeekList.add(classScheduleWeek.getId());
                }
            }
        }
        week = CollectionUtils.isNotEmpty(idWeekList);

        LocalDate monday = ConvertData.getMondayOfWeek(localDate);
        ScheduleFile scheduleFile = scheduleFileRepository.searchScheduleImageWeek(principal.getIdSchoolLogin(), kids.getMaClass().getId(), monday);
        file = scheduleFile.getUrlScheuldeFileList() != null;

        return this.setListString(day,week,file);
    }

    private List<String> setListString(boolean day, boolean week, boolean file){
        List<String> strList = new ArrayList<>();
        if (!day && week && file) {
            strList.add(0, "week");
            strList.add(1, "file");
            strList.add(2, "day");
        }else if (day && !week && file) {
            strList.add(0, "day");
            strList.add(1, "file");
            strList.add(2, "week");
        }else if (!day && !week && file) {
            strList.add(0, "file");
            strList.add(1, "day");
            strList.add(2, "week");

        }else if (!day && week && !file) {
            strList.add(0, "week");
            strList.add(1, "day");
            strList.add(2, "file");
        }else {
            strList.add(0, "day");
            strList.add(1, "week");
            strList.add(2, "file");
        }
        return strList;
    }
}
