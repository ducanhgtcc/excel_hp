package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuOrdinalParentService;
import com.example.onekids_project.repository.ClassMenuFileRepository;
import com.example.onekids_project.repository.ClassMenuRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-05-28 4:03 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class ClassMenuOrdinalParentServiceImpl implements ClassMenuOrdinalParentService {

    @Autowired
    private ClassMenuRepository classMenuRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    ClassMenuFileRepository classMenuFileRepository;

    @Override
    public List<String> classMenuOrdinalParent(UserPrincipal principal, LocalDate localDate) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        boolean day, week, file;
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        ClassMenu classMenu = classMenuRepository.searchDateMenu(idSchool, kids.getMaClass().getId(), localDate);
        if(classMenu != null){
            if (classMenu.getBreakfastContentList() != null || classMenu.getSecondBreakfastContentList() != null || classMenu.getLunchContentList() != null || classMenu.getAfternoonContentList() != null || classMenu.getSecondAfternoonContentList() != null || classMenu.getDinnerContentList() != null){
                day = localDate.equals(classMenu.getMenuDate());
            }else {
                day = false;
            }
        }else {
            day = false;
        }
        List<Long> idWeekList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            List<ClassMenu> classMenuList = classMenuRepository.searchClassMenuWeekList(idSchool, kids.getMaClass().getId(), localDate.plusDays(i));
            classMenuList.forEach(item -> {
                if (item.getId() != null){
                    idWeekList.add(item.getId());
                }
            });
        }
        week = CollectionUtils.isNotEmpty(idWeekList);
//        List<Long> idFileList = new ArrayList<>();

        LocalDate monday = ConvertData.getMondayOfWeek(localDate);
        ManuFile manuFile = classMenuFileRepository.searchMenuImageWeek(principal.getIdSchoolLogin(), kids.getMaClass().getId(), monday);
        file = manuFile.getUrlMenuFileList() != null;
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
