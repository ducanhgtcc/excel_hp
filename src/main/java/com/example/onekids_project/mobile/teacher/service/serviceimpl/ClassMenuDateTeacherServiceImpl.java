package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuDateTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ClassMenuDateTeacherService;
import com.example.onekids_project.repository.ClassMenuRepository;
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
public class ClassMenuDateTeacherServiceImpl implements ClassMenuDateTeacherService {

    @Autowired
    ClassMenuRepository classMenuRepository;

    @Override
    public List<MenuDateTeacherResponse> findDateMenu(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<MenuDateTeacherResponse> menuDateTeacherResponseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        ClassMenu classMenuList = classMenuRepository.searchDateMenu(idSchool, idClass, localDate);
        if (classMenuList == null) {
            return menuDateTeacherResponseList;
        }
        if (classMenuList.getBreakfastContentList() != null) {
            MenuDateTeacherResponse breakFast = new MenuDateTeacherResponse();
            String nameMeal = classMenuList.getBreakfastContentList();
            List<String> nameMealConvert = ConvertStringMeal(nameMeal);
            breakFast.setNameMeal(AttendanceConstant.MOR);
            breakFast.setTime(classMenuList.getBreakfastTime());
            breakFast.setFoodList(nameMealConvert);
            menuDateTeacherResponseList.add(breakFast);
        }
        if (classMenuList.getSecondBreakfastContentList() != null) {
            MenuDateTeacherResponse mealSecondBreakfast = new MenuDateTeacherResponse();
            String nameMealSecondBreakfast = classMenuList.getSecondBreakfastContentList();
            mealSecondBreakfast.setNameMeal(AttendanceConstant.SMOR);
            mealSecondBreakfast.setTime(classMenuList.getSecondBreakfastTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondBreakfast);
            mealSecondBreakfast.setFoodList(nameMealSecondBreakfastConvert);
            menuDateTeacherResponseList.add(mealSecondBreakfast);
        }
        if (classMenuList.getLunchContentList() != null) {
            MenuDateTeacherResponse lunch = new MenuDateTeacherResponse();
            String nameMealLunch = classMenuList.getLunchContentList();
            lunch.setNameMeal(AttendanceConstant.LUN);
            lunch.setTime(classMenuList.getLunchTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealLunch);
            lunch.setFoodList(nameMealSecondBreakfastConvert);
            menuDateTeacherResponseList.add(lunch);
        }
        if (classMenuList.getAfternoonContentList() != null) {
            MenuDateTeacherResponse afternoon = new MenuDateTeacherResponse();
            String nameMealAfternoon = classMenuList.getAfternoonContentList();
            afternoon.setNameMeal(AttendanceConstant.AFT);
            afternoon.setTime(classMenuList.getAfternoonTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealAfternoon);
            afternoon.setFoodList(nameMealSecondBreakfastConvert);
            menuDateTeacherResponseList.add(afternoon);
        }
        if (classMenuList.getSecondAfternoonContentList() != null) {
            MenuDateTeacherResponse secondAfternoon = new MenuDateTeacherResponse();
            String nameMealSecondAfternoon = classMenuList.getSecondAfternoonContentList();
            secondAfternoon.setNameMeal(AttendanceConstant.SAFT);
            secondAfternoon.setTime(classMenuList.getSecondAfternoonTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondAfternoon);
            secondAfternoon.setFoodList(nameMealSecondBreakfastConvert);
            menuDateTeacherResponseList.add(secondAfternoon);
        }
        if (classMenuList.getDinnerContentList() != null) {
            MenuDateTeacherResponse dinner = new MenuDateTeacherResponse();
            String nameMealDinner = classMenuList.getDinnerContentList();
            dinner.setNameMeal(AttendanceConstant.EVN);
            dinner.setTime(classMenuList.getDinnerTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealDinner);
            dinner.setFoodList(nameMealSecondBreakfastConvert);
            menuDateTeacherResponseList.add(dinner);
        }
        return menuDateTeacherResponseList;
    }

    @Override
    public List<Integer> findClassMenuMonthList(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        List<Integer> scheduleDaysMonth = new ArrayList<>();
        List<ClassMenu> classScheduleList = classMenuRepository.searchClassMenuMonthList(idSchool, idClass, localDate.getMonthValue(), localDate.getYear());
        for (ClassMenu classMenu : classScheduleList) {
            String menuMorning = classMenu.getBreakfastContentList();
            String menuSecondBreakfast = classMenu.getSecondBreakfastContentList();
            String menuLunch = classMenu.getLunchContentList();
            String menuAfternoon = classMenu.getAfternoonContentList();
            String menuSecondAfternoon = classMenu.getSecondAfternoonContentList();
            String menuDinner = classMenu.getDinnerContentList();

            if (!StringUtils.isBlank(menuMorning) || !StringUtils.isBlank(menuSecondBreakfast) || !StringUtils.isBlank(menuLunch) || !StringUtils.isBlank(menuAfternoon) ||
                    !StringUtils.isBlank(menuSecondAfternoon) || !StringUtils.isBlank(menuDinner)) {
                scheduleDaysMonth.add(classMenu.getMenuDate().getDayOfMonth());
            }
        }
        return scheduleDaysMonth;
    }

    private List<String> ConvertStringMeal(String nameMeal) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String[] strArray = nameMeal.split("\n");
        for (int i = 0; i < strArray.length; i++) {
            list.add(strArray[i]);
        }
        return list;
    }
}
