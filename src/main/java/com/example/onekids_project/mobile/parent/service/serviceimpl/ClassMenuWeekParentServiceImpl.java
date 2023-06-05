package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuDateParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuWeekParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuWeekParentService;
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
public class ClassMenuWeekParentServiceImpl implements ClassMenuWeekParentService {

    @Autowired
    private ClassMenuRepository classMenuRepository;
    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public List<MenuWeekParentResponse> findWeekMenu(UserPrincipal userPrincipal, LocalDate localDate) {
        List<MenuWeekParentResponse> listMenuWeekParentResponse = new ArrayList<>();
        Long idSchool = userPrincipal.getIdSchoolLogin();
        Long idKid = userPrincipal.getIdKidLogin();     //  get Long idKid
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        for (int i = 0; i < 7; i++) {
            MenuWeekParentResponse menuWeekParentResponse = new MenuWeekParentResponse();
            List<ClassMenu> classMenuList = classMenuRepository.searchClassMenuWeekList(idSchool, kids.getMaClass().getId(), localDate.plusDays(i));
            List<MenuDateParentResponse> menuDateParentResponseList = new ArrayList<>();
            if (classMenuList != null) {

                classMenuList.forEach(item -> {
                    menuWeekParentResponse.setDate(item.getMenuDate().toString());
                    if (!StringUtils.isBlank(item.getBreakfastContentList())) {
                        MenuDateParentResponse breakFast = new MenuDateParentResponse();
                        String nameMeal = item.getBreakfastContentList();
                        List<String> nameMealConvert = ConvertStringMeal(nameMeal);
//                        breakFast.setNameMeal("Bữa Sáng");
                        breakFast.setNameMeal(AttendanceConstant.MOR);
                        breakFast.setTime(item.getBreakfastTime());
                        breakFast.setFoodList(nameMealConvert);
                        menuDateParentResponseList.add(breakFast);
                    }
                    if (!StringUtils.isBlank(item.getSecondBreakfastContentList())) {
                        MenuDateParentResponse mealSecondBreakfast = new MenuDateParentResponse();
                        String nameMealSecondBreakfast = item.getSecondBreakfastContentList();
//                        mealSecondBreakfast.setNameMeal("Bữa Phụ Sáng");
                        mealSecondBreakfast.setNameMeal(AttendanceConstant.SMOR);
                        mealSecondBreakfast.setTime(item.getSecondBreakfastTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondBreakfast);
                        mealSecondBreakfast.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateParentResponseList.add(mealSecondBreakfast);
                    }
                    if (!StringUtils.isBlank(item.getLunchContentList())) {
                        MenuDateParentResponse lunch = new MenuDateParentResponse();
                        String nameMealLunch = item.getLunchContentList();
//                        lunch.setNameMeal("Bữa Trưa");
                        lunch.setNameMeal(AttendanceConstant.LUN);
                        lunch.setTime(item.getLunchTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealLunch);
                        lunch.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateParentResponseList.add(lunch);
                    }
                    if (!StringUtils.isBlank(item.getAfternoonContentList())) {
                        MenuDateParentResponse afternoon = new MenuDateParentResponse();
                        String nameMealAfternoon = item.getAfternoonContentList();
//                        afternoon.setNameMeal("Bữa Chiều");
                        afternoon.setNameMeal(AttendanceConstant.AFT);
                        afternoon.setTime(item.getAfternoonTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealAfternoon);
                        afternoon.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateParentResponseList.add(afternoon);
                    }
                    if (!StringUtils.isBlank(item.getSecondAfternoonContentList())) {
                        MenuDateParentResponse secondAfternoon = new MenuDateParentResponse();
                        String nameMealSecondAfternoon = item.getSecondAfternoonContentList();
//                        secondAfternoon.setNameMeal("Bữa Phụ Chiều");
                        secondAfternoon.setNameMeal(AttendanceConstant.SAFT);
                        secondAfternoon.setTime(item.getSecondAfternoonTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealSecondAfternoon);
                        secondAfternoon.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateParentResponseList.add(secondAfternoon);
                    }
                    if (!StringUtils.isBlank(item.getDinnerContentList())) {
                        MenuDateParentResponse dinner = new MenuDateParentResponse();
                        String nameMealDinner = item.getDinnerContentList();
//                        dinner.setNameMeal("Bữa Tối");
                        dinner.setNameMeal(AttendanceConstant.EVN);
                        dinner.setTime(item.getDinnerTime());
                        List<String> nameMealSecondBreakfastConvert = ConvertStringMeal(nameMealDinner);
                        dinner.setFoodList(nameMealSecondBreakfastConvert);
                        menuDateParentResponseList.add(dinner);
                    }
                });
                if (!CollectionUtils.isEmpty(menuDateParentResponseList)) {
                    menuWeekParentResponse.setMenuDateParentResponseList(menuDateParentResponseList);
                }
            }
            if (!CollectionUtils.isEmpty(menuWeekParentResponse.getMenuDateParentResponseList())) {
                listMenuWeekParentResponse.add(menuWeekParentResponse);
            }
        }
        return listMenuWeekParentResponse;
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
