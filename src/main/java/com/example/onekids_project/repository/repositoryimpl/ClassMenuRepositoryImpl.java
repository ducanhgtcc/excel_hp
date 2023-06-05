package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.repository.repositorycustom.ClassMenuRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassMenuRepositoryImpl extends BaseRepositoryimpl<ClassMenu> implements ClassMenuRepositoryCustom {
    //    @Override
//    public ClassMenu searchDateMenu(Long idSchool, Long idClass, LocalDate date) {
//        return null;
//    }
//    @Override
//    public ClassMenu searchDateMenu(Long idSchool, Long idClass, LocalDate date) {
//        return null;
//    }
    @Override
    public ClassMenu searchDateMenu(Long idSchool, Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {     // đặt tên theo idSchool truyền vào
            queryStr.append("and id_school=:idSchool ");    // đặt tên theo idschool giống tên trường trong database,
            mapParams.put("idSchool", idSchool); // đặt tên theo idSchool truyền vào
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (date != null) {
            queryStr.append("and menu_date =:menuDate ");
            mapParams.put("menuDate", date);
        }
        queryStr.append("and is_approved = true ");
        List<ClassMenu> classMenuList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(classMenuList)) {
            return null;
        }
        return classMenuList.get(0);
    }

    @Override
    public List<ClassMenu> searchClassMenuMonthList(Long idSchool, Long idClass, Integer month, Integer year) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (month != null) {
            queryStr.append("and month(menu_date) =:month ");
            mapParams.put("month", month);
        }
        if (year != null) {
            queryStr.append("and year(menu_date) =:year ");
            mapParams.put("year", year);
        }
        queryStr.append("and is_approved =true ");
        List<ClassMenu> classMenuList = findAllNoPaging(queryStr.toString(), mapParams);
        return classMenuList;
    }

    @Override
    public List<ClassMenu> searchClassMenuWeekList(Long idSchool, Long idClass, LocalDate date) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class =:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (date != null) {
            queryStr.append("and menu_date =:date ");
            mapParams.put("date", date);
        }
        queryStr.append("and is_approved =true ");
        List<ClassMenu> classMenuList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(classMenuList)) {
            return classMenuList;
        }
        return classMenuList;
    }
}
