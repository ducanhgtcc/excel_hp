package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.ScheduleFile;
import com.example.onekids_project.entity.classes.UrlScheuldeFile;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ListScheduleFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleImageWeekTeachResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ScheduleImageFileTeacherService;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.ScheduleFileRepository;
import com.example.onekids_project.repository.UrlScheduleFileRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
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
public class ScheduleImageFileTeacherServiceImpl implements ScheduleImageFileTeacherService {

    @Autowired
    ScheduleFileRepository scheduleFileRepository;

    @Autowired
    KidsRepository kidsRepository;

    @Autowired
    UrlScheduleFileRepository urlScheduleFileRepository;

    @Autowired
    ListMapper listMapper;

    @Override
    public ScheduleImageWeekTeachResponse findImageWeek(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        ScheduleImageWeekTeachResponse scheduleImageWeekTeachResponse = new ScheduleImageWeekTeachResponse();
        List<String> pictureList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();     //  get Long idKid
        LocalDate monday = ConvertData.getMondayOfWeek(localDate);

        ScheduleFile scheduleFile = scheduleFileRepository.searchScheduleImageWeek(idSchool, idClass, monday);

        Set<UrlScheuldeFile> urls = scheduleFile.getUrlScheuldeFileList();
        if (CollectionUtils.isEmpty(urls)) {
            scheduleImageWeekTeachResponse.setPictureList(new ArrayList<>());
            scheduleImageWeekTeachResponse.setWeekName("");
            return scheduleImageWeekTeachResponse;
        }
        urls.forEach(x -> {
            if (StringUtils.isNotBlank(x.getNamePicture())) {
                pictureList.add(x.getUrlPicture());
            } else if (StringUtils.isNotBlank(x.getUrlFile())) {
                fileList.add(x.getUrlFile());
            }
        });

        if (CollectionUtils.isEmpty(pictureList)) {
            scheduleImageWeekTeachResponse.setPictureList(new ArrayList<>());
            scheduleImageWeekTeachResponse.setWeekName("");

            if (!CollectionUtils.isEmpty(fileList)) {
                pictureList.add(MobileConstant.NO_IMAGE);
                scheduleImageWeekTeachResponse.setPictureList(pictureList);
            }

        } else {
            scheduleImageWeekTeachResponse.setWeekName(ConvertData.convertDateToWeekname(scheduleFile.getFromFileTsime()));
            scheduleImageWeekTeachResponse.setPictureList(pictureList);
        }
        return scheduleImageWeekTeachResponse;
    }

    @Override
    public ListScheduleFileTeacherResponse findFileAllWeek(UserPrincipal principal, Integer pageNumber) {
        CommonValidate.checkDataTeacher(principal);
        ListScheduleFileTeacherResponse listScheduleFileTeacherResponse = new ListScheduleFileTeacherResponse(); // schedule list
        List<ScheduleFileTeacherResponse> dataList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();

        List<ScheduleFile> scheduleFileList = scheduleFileRepository.searchScheduleFilePageNumber(idSchool, idClass, pageNumber);
        scheduleFileList.forEach(x -> {
            List<UrlScheuldeFile> urlScheuldeFileList = List.copyOf(x.getUrlScheuldeFileList());
            ScheduleFileTeacherResponse model = new ScheduleFileTeacherResponse();
            List<AttachFileMobileResponse> fileList = new ArrayList<>();
            List<String> pictureList = new ArrayList<>();

            urlScheuldeFileList.forEach(y -> {
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
            model.setDate(x.getFromFileTsime());
            model.setWeek(ConvertData.convertDateToWeekname(x.getFromFileTsime()));
            model.setFileList(fileList);

            if (CollectionUtils.isEmpty(fileList) && CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTsime());
                model.setWeek("");
                model.setFileList(new ArrayList<>());

            }
            if (CollectionUtils.isEmpty(fileList) && !CollectionUtils.isEmpty(pictureList)) {
                model.setDate(x.getFromFileTsime());
                model.setWeek(ConvertData.convertDateToWeekname(x.getFromFileTsime()));
                model.setFileList(new ArrayList<>());
            }

            if (!Strings.isBlank(model.getWeek())) {
                dataList.add(model);
            }

        });

//        if (CollectionUtils.isEmpty(listScheduleFileTeacherResponse.getDataList())) {
//            dataList.add(model);
//        }
        long count = dataList.size();
        boolean lastPage = count < MobileConstant.MAX_PAGE_ITEM;
        listScheduleFileTeacherResponse.setDataList(dataList);
        listScheduleFileTeacherResponse.setLastPage(lastPage);
//        long countEmpty = listScheduleFileTeacherResponse.getDataList().stream().filter(x -> !CollectionUtils.isEmpty(x.getFileList())).count();
//        if (countEmpty == 0) {
//            listScheduleFileTeacherResponse.setDataList(new ArrayList<>());
//        }
        return listScheduleFileTeacherResponse;
    }
}
