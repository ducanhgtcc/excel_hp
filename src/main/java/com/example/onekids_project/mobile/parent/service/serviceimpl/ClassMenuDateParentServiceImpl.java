package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuDateParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuDateParentService;
import com.example.onekids_project.repository.ClassMenuRepository;
import com.example.onekids_project.repository.KidsRepository;
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
public class ClassMenuDateParentServiceImpl implements ClassMenuDateParentService {

    @Autowired
    private ClassMenuRepository classMenuRepository;
    @Autowired
    private KidsRepository kidsRepository;


    @Override
    public List<MenuDateParentResponse> findDateMenu(UserPrincipal userPrincipal, LocalDate date) {
        List<MenuDateParentResponse> menuDateParentResponseList = new ArrayList<>();
//        MenuDateParentResponse menuDateParentResponse = new MenuDateParentResponse();
        Long idSchool = userPrincipal.getIdSchoolLogin();
        Long idKid = userPrincipal.getIdKidLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        // Get List Class menu contains all menu food for kid.
        ClassMenu classMenuList = classMenuRepository.searchDateMenu(idSchool, kids.getMaClass().getId(), date);
        if (classMenuList == null) {
            return menuDateParentResponseList;
        }
        if (classMenuList.getBreakfastContentList() != null) {
            MenuDateParentResponse breakFast = new MenuDateParentResponse();
            String nameMeal = classMenuList.getBreakfastContentList();
            List<String> nameMealConvert = ConvertStringMeal(nameMeal);
//            breakFast.setNameMeal("Bữa Sáng");
            breakFast.setNameMeal(AttendanceConstant.MOR);
            breakFast.setTime(classMenuList.getBreakfastTime());
            breakFast.setFoodList(nameMealConvert);
            menuDateParentResponseList.add(breakFast);
        }
        if (classMenuList.getSecondBreakfastContentList() != null) {
            MenuDateParentResponse mealSecondBreakfast = new MenuDateParentResponse();
            String nameMealSecondBreakfast = classMenuList.getSecondBreakfastContentList();
//            mealSecondBreakfast.setNameMeal("Bữa Phụ Sáng");
            mealSecondBreakfast.setNameMeal(AttendanceConstant.SMOR);
            mealSecondBreakfast.setTime(classMenuList.getSecondBreakfastTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondBreakfast);
            mealSecondBreakfast.setFoodList(nameMealSecondBreakfastConvert);
            menuDateParentResponseList.add(mealSecondBreakfast);
        }
        if (classMenuList.getLunchContentList() != null) {
            MenuDateParentResponse lunch = new MenuDateParentResponse();
            String nameMealLunch = classMenuList.getLunchContentList();
//            lunch.setNameMeal("Bữa Trưa");
            lunch.setNameMeal(AttendanceConstant.LUN);
            lunch.setTime(classMenuList.getLunchTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealLunch);
            lunch.setFoodList(nameMealSecondBreakfastConvert);
            menuDateParentResponseList.add(lunch);
        }
        if (classMenuList.getAfternoonContentList() != null) {
            MenuDateParentResponse afternoon = new MenuDateParentResponse();
            String nameMealAfternoon = classMenuList.getAfternoonContentList();
//            afternoon.setNameMeal("Bữa Chiều");
            afternoon.setNameMeal(AttendanceConstant.AFT);
            afternoon.setTime(classMenuList.getAfternoonTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealAfternoon);
            afternoon.setFoodList(nameMealSecondBreakfastConvert);
            menuDateParentResponseList.add(afternoon);
        }
        if (classMenuList.getSecondAfternoonContentList() != null) {
            MenuDateParentResponse secondAfternoon = new MenuDateParentResponse();
            String nameMealSecondAfternoon = classMenuList.getSecondAfternoonContentList();
//            secondAfternoon.setNameMeal("Bữa Phụ Chiều");
            secondAfternoon.setNameMeal(AttendanceConstant.SAFT);
            secondAfternoon.setTime(classMenuList.getSecondAfternoonTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondAfternoon);
            secondAfternoon.setFoodList(nameMealSecondBreakfastConvert);
            menuDateParentResponseList.add(secondAfternoon);
        }
        if (classMenuList.getDinnerContentList() != null) {
            MenuDateParentResponse dinner = new MenuDateParentResponse();
            String nameMealDinner = classMenuList.getDinnerContentList();
//            dinner.setNameMeal("Bữa Tối");
            dinner.setNameMeal(AttendanceConstant.EVN);
            dinner.setTime(classMenuList.getDinnerTime());
            List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealDinner);
            dinner.setFoodList(nameMealSecondBreakfastConvert);
            menuDateParentResponseList.add(dinner);
        }
        // Add data list
//        menuDateParentResponseList.add();
        return menuDateParentResponseList;
    }

    @Override
    public List<Integer> findClassMenuMonthList(UserPrincipal principal, LocalDate localDate) {
        Long idKid = principal.getIdKidLogin();     //  get Long idKid
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        Long idClass = kids.getMaClass().getId();
        List<ClassMenu> classScheduleList = classMenuRepository.searchClassMenuMonthList(principal.getIdSchoolLogin(), idClass, localDate.getMonthValue(), localDate.getYear());
        List<Integer> scheduleDaysMonth = new ArrayList<>();
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
