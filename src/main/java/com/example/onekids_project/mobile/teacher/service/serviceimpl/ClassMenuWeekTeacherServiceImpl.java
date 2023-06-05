package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuDateTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuWeekTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ClassMenuWeekTeacherService;
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
public class ClassMenuWeekTeacherServiceImpl implements ClassMenuWeekTeacherService {

    @Autowired
    ClassMenuRepository classMenuRepository;

    @Override
    public List<MenuWeekTeacherResponse> findWeekMenu(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<MenuWeekTeacherResponse> listMenuWeekTeacherResponses = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        for (int i = 0; i < 7; i++) {
            MenuWeekTeacherResponse menuWeekTeacherResponse = new MenuWeekTeacherResponse();
            List<ClassMenu> classMenuList = classMenuRepository.searchClassMenuWeekList(idSchool, idClass, localDate.plusDays(i));
            List<MenuDateTeacherResponse> menuDateTeacherResponseList = new ArrayList<>();
            if (classMenuList != null) {

                classMenuList.forEach(item -> {
                    menuWeekTeacherResponse.setDate(item.getMenuDate().toString());
                    if (!StringUtils.isBlank(item.getBreakfastContentList())) {
                        MenuDateTeacherResponse breakFast = new MenuDateTeacherResponse();
                        String nameMeal = item.getBreakfastContentList();
                        List<String> nameMealConvert = ConvertStringMeal(nameMeal);
                        breakFast.setNameMeal(AttendanceConstant.MOR);
                        breakFast.setTime(item.getBreakfastTime());
                        breakFast.setFoodList(nameMealConvert);
                        menuDateTeacherResponseList.add(breakFast);
                    }
                    if (!StringUtils.isBlank(item.getSecondBreakfastContentList())) {
                        MenuDateTeacherResponse mealSecondBreakfast = new MenuDateTeacherResponse();
                        String nameMealSecondBreakfast = item.getSecondBreakfastContentList();
                        mealSecondBreakfast.setNameMeal(AttendanceConstant.SMOR);
                        mealSecondBreakfast.setTime(item.getSecondBreakfastTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondBreakfast);
                        mealSecondBreakfast.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateTeacherResponseList.add(mealSecondBreakfast);
                    }
                    if (!StringUtils.isBlank(item.getLunchContentList())) {
                        MenuDateTeacherResponse lunch = new MenuDateTeacherResponse();
                        String nameMealLunch = item.getLunchContentList();
                        lunch.setNameMeal(AttendanceConstant.LUN);
                        lunch.setTime(item.getLunchTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealLunch);
                        lunch.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateTeacherResponseList.add(lunch);
                    }
                    if (!StringUtils.isBlank(item.getAfternoonContentList())) {
                        MenuDateTeacherResponse afternoon = new MenuDateTeacherResponse();
                        String nameMealAfternoon = item.getAfternoonContentList();
                        afternoon.setNameMeal(AttendanceConstant.AFT);
                        afternoon.setTime(item.getAfternoonTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealAfternoon);
                        afternoon.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateTeacherResponseList.add(afternoon);
                    }
                    if (!StringUtils.isBlank(item.getSecondAfternoonContentList())) {
                        MenuDateTeacherResponse secondAfternoon = new MenuDateTeacherResponse();
                        String nameMealSecondAfternoon = item.getSecondAfternoonContentList();
                        secondAfternoon.setNameMeal(AttendanceConstant.SAFT);
                        secondAfternoon.setTime(item.getSecondAfternoonTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondAfternoon);
                        secondAfternoon.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateTeacherResponseList.add(secondAfternoon);
                    }
                    if (!StringUtils.isBlank(item.getDinnerContentList())) {
                        MenuDateTeacherResponse dinner = new MenuDateTeacherResponse();
                        String nameMealDinner = item.getDinnerContentList();

                        dinner.setNameMeal(AttendanceConstant.EVN);
                        dinner.setTime(item.getDinnerTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealDinner);
                        dinner.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateTeacherResponseList.add(dinner);
                    }
                });
                if (!CollectionUtils.isEmpty(menuDateTeacherResponseList)) {
                    menuWeekTeacherResponse.setMenuDateTeacherResponseList(menuDateTeacherResponseList);
                }
            }
            if (!CollectionUtils.isEmpty(menuWeekTeacherResponse.getMenuDateTeacherResponseList())) {
                listMenuWeekTeacherResponses.add(menuWeekTeacherResponse);
            }
        }
        return listMenuWeekTeacherResponses;
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
