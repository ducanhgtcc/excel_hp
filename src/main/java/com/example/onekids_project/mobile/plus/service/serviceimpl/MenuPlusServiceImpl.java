package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.common.MenuConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.ClassMenu;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.classes.UrlMenuFile;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.menu.MenuDatePlusRequest;
import com.example.onekids_project.mobile.plus.response.menu.MenuClassResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuClassWeekResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuDatePlusResponse;
import com.example.onekids_project.mobile.plus.response.menu.MenuWeekPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.MenuPlusService;
import com.example.onekids_project.mobile.request.MenuFileRequest;
import com.example.onekids_project.mobile.response.*;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.FileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MenuPlusServiceImpl implements MenuPlusService {

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private ClassMenuRepository menuRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private ClassMenuFileRepository classMenuFileRepository;

    @Autowired
    private ListMapper listMapper;

    @Override
    public List<MenuClassResponse> searchMenuClass(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataPlus(principal);
        List<MenuClassResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        maClassList.forEach(x -> {
            MenuClassResponse response = new MenuClassResponse();
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByExEmployeeClassList_MaClass_IdAndDelActiveTrue(x.getId());
            response.setFeatureClassResponse(ClassFeatureUtils.setFeatureClass(infoEmployeeSchoolList, x));
            List<ClassMenu> menuList = menuRepository.findByDelActiveTrueAndMaClass_IdAndMenuDateAndIsApprovedTrue(x.getId(), localDate);
            response.setMenu(AppConstant.APP_FALSE);
            if (!CollectionUtils.isEmpty(menuList)) {
                ClassMenu menu = menuList.get(0);
                if (!Strings.isEmpty(menu.getBreakfastContentList()) || !Strings.isEmpty(menu.getSecondBreakfastContentList()) || !Strings.isEmpty(menu.getLunchContentList()) || !Strings.isEmpty(menu.getAfternoonContentList()) || !Strings.isEmpty(menu.getSecondAfternoonContentList()) || !Strings.isEmpty(menu.getDinnerContentList())) {
                    response.setMenu(AppConstant.APP_TRUE);
                }
            }
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<MenuDatePlusResponse> searchMenuDate(UserPrincipal principal, MenuDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<MenuDatePlusResponse> responseList = new ArrayList<>();
        ClassMenu classMenu = menuRepository.searchDateMenu(principal.getIdSchoolLogin(), request.getIdClass(), request.getDate());
        if (classMenu == null) {
            return responseList;
        }
        return this.setPropertiesMenu(classMenu);
    }

    @Override
    public List<Integer> searchMenuMonth(UserPrincipal principal, MenuDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = request.getIdClass();
        List<Integer> responseList = new ArrayList<>();
        List<ClassMenu> classScheduleList = menuRepository.searchClassMenuMonthList(idSchool, idClass, request.getDate().getMonthValue(), request.getDate().getYear());
        for (ClassMenu classMenu : classScheduleList) {
            String menuMorning = classMenu.getBreakfastContentList();
            String menuSecondBreakfast = classMenu.getSecondBreakfastContentList();
            String menuLunch = classMenu.getLunchContentList();
            String menuAfternoon = classMenu.getAfternoonContentList();
            String menuSecondAfternoon = classMenu.getSecondAfternoonContentList();
            String menuDinner = classMenu.getDinnerContentList();

            if (!StringUtils.isBlank(menuMorning) || !StringUtils.isBlank(menuSecondBreakfast) || !StringUtils.isBlank(menuLunch) || !StringUtils.isBlank(menuAfternoon) ||
                    !StringUtils.isBlank(menuSecondAfternoon) || !StringUtils.isBlank(menuDinner)) {
                responseList.add(classMenu.getMenuDate().getDayOfMonth());
            }
        }
        return responseList;
    }

    @Override
    public List<MenuClassWeekResponse> searchMenuClassWeek(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataPlus(principal);
        List<MenuClassWeekResponse> responseList = new ArrayList<>();
        LocalDate monday = ConvertData.getMondayOfWeek(localDate);
        LocalDate dateEnd = monday.plusDays(6);
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        maClassList.forEach(y -> {
            List<MenuStatusDay> dayList = new ArrayList<>();
            MenuClassWeekResponse response = new MenuClassWeekResponse();
            List<ClassMenu> menuList = menuRepository.findByDelActiveTrueAndMaClass_IdAndMenuDateBetween(y.getId(), monday, dateEnd);
            response.setMenu(AppConstant.APP_FALSE);
            menuList.forEach(x -> {
                MenuStatusDay statusDay = new MenuStatusDay();
                if (!AttendanceKidsUtil.checkStudyInSchool(BeanDataUtils.getAttendanceConfigDate(principal.getIdSchoolLogin(), x.getMenuDate()))) {
                    statusDay.setKeyDay(ConvertData.convetDayString(x.getMenuDate().getDayOfWeek().toString()));
                    statusDay.setStatus(AttendanceConstant.TYPE_ABSENT);
                } else if (!Strings.isEmpty(x.getBreakfastContentList()) || !Strings.isEmpty(x.getSecondBreakfastContentList()) || !Strings.isEmpty(x.getAfternoonContentList()) || !Strings.isEmpty(x.getSecondAfternoonContentList()) || !Strings.isEmpty(x.getLunchContentList()) || !Strings.isEmpty(x.getDinnerContentList())) {
                    statusDay.setKeyDay(ConvertData.convetDayString(x.getMenuDate().getDayOfWeek().toString()));
                    statusDay.setStatus(AppConstant.YES);
                } else {
                    statusDay.setKeyDay(ConvertData.convetDayString(x.getMenuDate().getDayOfWeek().toString()));
                    statusDay.setStatus(AppConstant.NO);
                }
                dayList.add(statusDay);
                response.setMenu(AppConstant.APP_TRUE);
            });
            if (CollectionUtils.isEmpty(menuList)) {
                this.setStatusDay(monday, principal, dayList);
            }
            response.setNameClass(y.getClassName());
            response.setId(y.getId());
            response.setStatusDayList(dayList);
            responseList.add(response);
        });
        return responseList;
    }

    private List<MenuStatusDay> setStatusDay(LocalDate monday, UserPrincipal principal, List<MenuStatusDay> dayList) {
        for (int i = 0; i < 7; i++) {
            MenuStatusDay statusDay = new MenuStatusDay();
            LocalDate date = monday.plusDays(i);
            if (!AttendanceKidsUtil.checkStudyInSchool(BeanDataUtils.getAttendanceConfigDate(principal.getIdSchoolLogin(), date))) {
                statusDay.setKeyDay(ConvertData.convetDayString(date.getDayOfWeek().toString()));
                statusDay.setStatus(AttendanceConstant.TYPE_ABSENT);
            } else {
                statusDay.setKeyDay(ConvertData.convetDayString(date.getDayOfWeek().toString()));
                statusDay.setStatus(AppConstant.NO);
            }
            dayList.add(statusDay);
        }
        return dayList;
    }

    @Override
    public List<MenuWeekPlusResponse> searchMenuWeek(UserPrincipal principal, MenuDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<MenuWeekPlusResponse> responseList = new ArrayList<>();
        LocalDate monday = ConvertData.getMondayOfWeek(request.getDate());
        for (int i = 0; i < 7; i++) {
            MenuWeekPlusResponse response = new MenuWeekPlusResponse();
            List<ClassMenu> classMenuList = menuRepository.searchClassMenuWeekList(principal.getIdSchoolLogin(), request.getIdClass(), monday.plusDays(i));
            if (classMenuList != null) {
                classMenuList.forEach(item -> {
                    response.setDate(item.getMenuDate());
                    response.setMenuDateList(this.setPropertiesMenu(item));
                    responseList.add(response);
                });
            }
        }
        return responseList;
    }

    @Override
    public List<FeatureClassResponse> searchMenuFileClass(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<FeatureClassResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        maClassList.forEach(x -> {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByExEmployeeClassList_MaClass_IdAndDelActiveTrue(x.getId());
            FeatureClassResponse response = ClassFeatureUtils.setFeatureClass(infoEmployeeSchoolList, x);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public ImageWeekResponse searchMenuImage(UserPrincipal principal, MenuDatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate monday = ConvertData.getMondayOfWeek(request.getDate());
        ManuFile manuFile = classMenuFileRepository.searchMenuImageWeek(principal.getIdSchoolLogin(), request.getIdClass(), monday);
        return this.setPropertiesMenuImage(manuFile);
    }

    @Override
    public ListFileWeekResponse searchMenuFile(UserPrincipal principal, MenuFileRequest request) {
        CommonValidate.checkDataPlus(principal);
        ListFileWeekResponse response = new ListFileWeekResponse();
        List<ManuFile> manuFileList = classMenuFileRepository.searchMenuFilePageNumber(principal.getIdSchoolLogin(), request.getId(), request.getPageNumber());
        List<FileWeekResponse> dataList = this.setPropertiesMenuFile(manuFileList);
        long count = dataList.size();
        boolean lastPage = count < MobileConstant.MAX_PAGE_ITEM;
        response.setDataList(dataList);
        response.setLastPage(lastPage);
        return response;
    }

    private List<FileWeekResponse> setPropertiesMenuFile(List<ManuFile> manuFileList) {
        List<FileWeekResponse> dataList = new ArrayList<>();
        manuFileList.forEach(x -> {
            List<UrlMenuFile> urlMenuFileList = List.copyOf(x.getUrlMenuFileList());
            FileWeekResponse model = new FileWeekResponse();
            List<String> pictureList = new ArrayList<>();
            List<FileResponse> fileList = new ArrayList<>();
            urlMenuFileList.forEach(y -> {
                if (StringUtils.isNotBlank(y.getNameFile())) {
                    FileResponse file = new FileResponse();
                    file.setName(y.getNameFile());
                    file.setUrl(y.getUrlFile());
                    fileList.add(file);
                }
                if (StringUtils.isNotBlank(y.getNamePicture())) {
                    pictureList.add(y.getNamePicture());
                }
            });
            model.setDate(x.getFromFileTime());
            model.setWeekName(ConvertData.convertDateToWeekname(x.getFromFileTime()));
            model.setFileList(fileList);
            if (CollectionUtils.isEmpty(fileList) && CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTime());
                model.setWeekName("");
                model.setFileList(new ArrayList<>());
            }
            if (CollectionUtils.isEmpty(fileList) && !CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTime());
                model.setWeekName(ConvertData.convertDateToWeekname(x.getFromFileTime()));
                model.setFileList(new ArrayList<>());
            }
            if (!Strings.isBlank(model.getWeekName())) {
                dataList.add(model);
            }
        });
        return dataList;
    }

    private ImageWeekResponse setPropertiesMenuImage(ManuFile manuFile) {
        ImageWeekResponse response = new ImageWeekResponse();
        List<String> pictureList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        Set<UrlMenuFile> urls = manuFile.getUrlMenuFileList();
        if (CollectionUtils.isEmpty(urls)) {
            response.setPictureList(new ArrayList<>());
            response.setWeekName("");
            return response;
        }
        urls.forEach(x -> {
            if (StringUtils.isNotBlank(x.getNamePicture())) {
                pictureList.add(x.getUrlPicture());
            }
            if (StringUtils.isNotBlank(x.getNameFile())) {
                fileList.add(x.getNameFile());
            }
        });
        if (CollectionUtils.isEmpty(pictureList)) {
            response.setPictureList(new ArrayList<>());
            response.setWeekName("");
            if (!CollectionUtils.isEmpty(fileList)) {
                pictureList.add(MobileConstant.NO_IMAGE);
                response.setPictureList(pictureList);
            }
        } else {
            response.setWeekName(ConvertData.convertDateToWeekname(manuFile.getFromFileTime()));
            response.setPictureList(pictureList);
        }
        return response;
    }

    private List<MenuDatePlusResponse> setPropertiesMenu(ClassMenu classMenu) {
        List<MenuDatePlusResponse> responseList = new ArrayList<>();
        if (classMenu.getBreakfastContentList() != null) {
            MenuDatePlusResponse breakFast = new MenuDatePlusResponse();
            String nameMeal = classMenu.getBreakfastContentList();
            List<String> nameMealConvert = MenuUtils.ConvertStringMeal(nameMeal);
            breakFast.setNameMeal(MenuConstant.MOR);
            breakFast.setTime(classMenu.getBreakfastTime());
            breakFast.setFoodList(nameMealConvert);
            responseList.add(breakFast);
        }
        if (classMenu.getSecondBreakfastContentList() != null) {
            MenuDatePlusResponse mealSecondBreakfast = new MenuDatePlusResponse();
            String nameMealSecondBreakfast = classMenu.getSecondBreakfastContentList();
            mealSecondBreakfast.setNameMeal(MenuConstant.SMOR);
            mealSecondBreakfast.setTime(classMenu.getSecondBreakfastTime());
            List<String> nameMealSecondBreakfastConvert = MenuUtils.ConvertStringMeal(nameMealSecondBreakfast);
            mealSecondBreakfast.setFoodList(nameMealSecondBreakfastConvert);
            responseList.add(mealSecondBreakfast);
        }
        if (classMenu.getLunchContentList() != null) {
            MenuDatePlusResponse lunch = new MenuDatePlusResponse();
            String nameMealLunch = classMenu.getLunchContentList();
            lunch.setNameMeal(MenuConstant.LUN);
            lunch.setTime(classMenu.getLunchTime());
            List<String> nameMealSecondBreakfastConvert = MenuUtils.ConvertStringMeal(nameMealLunch);
            lunch.setFoodList(nameMealSecondBreakfastConvert);
            responseList.add(lunch);
        }
        if (classMenu.getAfternoonContentList() != null) {
            MenuDatePlusResponse afternoon = new MenuDatePlusResponse();
            String nameMealAfternoon = classMenu.getAfternoonContentList();
            afternoon.setNameMeal(MenuConstant.AFT);
            afternoon.setTime(classMenu.getAfternoonTime());
            List<String> nameMealSecondBreakfastConvert = MenuUtils.ConvertStringMeal(nameMealAfternoon);
            afternoon.setFoodList(nameMealSecondBreakfastConvert);
            responseList.add(afternoon);
        }
        if (classMenu.getSecondAfternoonContentList() != null) {
            MenuDatePlusResponse secondAfternoon = new MenuDatePlusResponse();
            String nameMealSecondAfternoon = classMenu.getSecondAfternoonContentList();
            secondAfternoon.setNameMeal(MenuConstant.SAFT);
            secondAfternoon.setTime(classMenu.getSecondAfternoonTime());
            List<String> nameMealSecondBreakfastConvert = MenuUtils.ConvertStringMeal(nameMealSecondAfternoon);
            secondAfternoon.setFoodList(nameMealSecondBreakfastConvert);
            responseList.add(secondAfternoon);
        }
        if (classMenu.getDinnerContentList() != null) {
            MenuDatePlusResponse dinner = new MenuDatePlusResponse();
            String nameMealDinner = classMenu.getDinnerContentList();
            dinner.setNameMeal(MenuConstant.EVN);
            dinner.setTime(classMenu.getDinnerTime());
            List<String> nameMealSecondBreakfastConvert = MenuUtils.ConvertStringMeal(nameMealDinner);
            dinner.setFoodList(nameMealSecondBreakfastConvert);
            responseList.add(dinner);
        }
        return responseList;
    }
}
