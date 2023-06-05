package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.classes.UrlMenuFile;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.ListMenuFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuImageWeekTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ClassMenuImageFileTeacherService;
import com.example.onekids_project.repository.ClassMenuFileRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ClassMenuImageFileTeacherServiceImpl implements ClassMenuImageFileTeacherService {

    @Autowired
    ClassMenuFileRepository classMenuFileRepository;

    @Override
    public MenuImageWeekTeacherResponse findImageWeek(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        MenuImageWeekTeacherResponse menuImageWeekTeacherResponse = new MenuImageWeekTeacherResponse();
        List<String> pictureList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();     //  get Long idKid
        Long idClass = principal.getIdClassLogin(); // idclass


        LocalDate monday = localDate;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        ManuFile manuFile = classMenuFileRepository.searchMenuImageWeek(idSchool, idClass, monday);

        Set<UrlMenuFile> urls = manuFile.getUrlMenuFileList();
        if (CollectionUtils.isEmpty(urls)) {
            menuImageWeekTeacherResponse.setPictureList(new ArrayList<>());
            menuImageWeekTeacherResponse.setWeekName("");
            return menuImageWeekTeacherResponse;
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
            menuImageWeekTeacherResponse.setPictureList(new ArrayList<>());
            menuImageWeekTeacherResponse.setWeekName("");

            if (!CollectionUtils.isEmpty(fileList)) {
                pictureList.add(MobileConstant.NO_IMAGE);
                menuImageWeekTeacherResponse.setPictureList(pictureList);
            }

        } else {
            menuImageWeekTeacherResponse.setWeekName(ConvertData.convertDateToWeekname(manuFile.getFromFileTime()));
            menuImageWeekTeacherResponse.setPictureList(pictureList);
        }

        return menuImageWeekTeacherResponse;
    }

    @Override
    public ListMenuFileTeacherResponse findFileAllWeek(UserPrincipal principal, Integer pageNumber) {
        CommonValidate.checkDataTeacher(principal);
        ListMenuFileTeacherResponse listMenuFileTeacherResponse = new ListMenuFileTeacherResponse(); // menu list
        List<MenuFileTeacherResponse> dataList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        // List all menu file.
        List<ManuFile> manuFileList = classMenuFileRepository.searchMenuFilePageNumber(idSchool, idClass, pageNumber);
        manuFileList.forEach(x -> {
            List<UrlMenuFile> urlMenuFileList = List.copyOf(x.getUrlMenuFileList());
            MenuFileTeacherResponse model = new MenuFileTeacherResponse();
            List<String> pictureList = new ArrayList<>();

            List<AttachFileMobileResponse> fileList = new ArrayList<>();
            urlMenuFileList.forEach(y -> {
                if (StringUtils.isNotBlank(y.getNameFile())) {
                    AttachFileMobileResponse file = new AttachFileMobileResponse();
                    file.setName(y.getNameFile());
                    file.setUrl(y.getUrlFile());
                    fileList.add(file);
                }
                if (StringUtils.isNotBlank(y.getNamePicture())) {
                    pictureList.add(y.getNamePicture());
                }
            });

            model.setDate(x.getFromFileTime());
            model.setWeek(ConvertData.convertDateToWeekname(x.getFromFileTime()));
            model.setFileList(fileList);
            if (CollectionUtils.isEmpty(fileList) && CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTime());
                model.setWeek("");
                model.setFileList(new ArrayList<>());
            }
            if (CollectionUtils.isEmpty(fileList) && !CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTime());
                model.setWeek(ConvertData.convertDateToWeekname(x.getFromFileTime()));
                model.setFileList(new ArrayList<>());
            }
            if (!Strings.isBlank(model.getWeek())) {
                dataList.add(model);
            }

        });
        long count = dataList.size();
        boolean lastPage = count < MobileConstant.MAX_PAGE_ITEM;
        listMenuFileTeacherResponse.setDataList(dataList);
        listMenuFileTeacherResponse.setLastPage(lastPage);

//        long countEmpty = listMenuFileTeacherResponse.getDataList().stream().filter(x -> !CollectionUtils.isEmpty(x.getFileList())).count();
//        if (countEmpty == 0) {
//            listMenuFileTeacherResponse.setDataList(new ArrayList<>());
//        }
        return listMenuFileTeacherResponse;
    }
}
