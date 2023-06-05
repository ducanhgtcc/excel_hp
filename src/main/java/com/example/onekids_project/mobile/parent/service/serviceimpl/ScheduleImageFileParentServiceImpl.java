package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.ScheduleFile;
import com.example.onekids_project.entity.classes.UrlScheuldeFile;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ListScheduleFileParentResponse;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleFileParentResponse;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleImageParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleImageFileParentService;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.ScheduleFileRepository;
import com.example.onekids_project.repository.UrlScheduleFileRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleImageFileParentServiceImpl implements ScheduleImageFileParentService {

    @Autowired
    ScheduleFileRepository scheduleFileRepository;

    @Autowired
    KidsRepository kidsRepository;

    @Autowired
    UrlScheduleFileRepository urlScheduleFileRepository;

    @Autowired
    ListMapper listMapper;

    @Override
    public ScheduleImageParentResponse findImageWeek(UserPrincipal userPrincipal, LocalDate localDate) {
        ScheduleImageParentResponse scheduleImageParentResponse = new ScheduleImageParentResponse();
        Long idKid = userPrincipal.getIdKidLogin();     //  get Long idKid
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        Long idClass = kids.getMaClass().getId(); // idclass

        LocalDate monday = ConvertData.getMondayOfWeek(localDate);
        ScheduleFile scheduleFile = scheduleFileRepository.searchScheduleImageWeek(userPrincipal.getIdSchoolLogin(), idClass, monday);
        if (scheduleFile.getUrlScheuldeFileList() == null) {
            scheduleImageParentResponse.setPictureList(new ArrayList<>());
            scheduleImageParentResponse.setWeekName("");
            return scheduleImageParentResponse;
        }
        Set<UrlScheuldeFile> urls = scheduleFile.getUrlScheuldeFileList();
        List<String> pictureList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        urls.forEach(x -> {
            if (StringUtils.isNotBlank(x.getNamePicture())) {
                pictureList.add(x.getUrlPicture());
            }
            if (StringUtils.isNotBlank(x.getNameFile())) {
                fileList.add(x.getNameFile());
            }
        });


        if (CollectionUtils.isEmpty(pictureList)) {
            scheduleImageParentResponse.setPictureList(new ArrayList<>());
            scheduleImageParentResponse.setWeekName("");

            if (!CollectionUtils.isEmpty(fileList)) {
                pictureList.add(MobileConstant.NO_IMAGE);
                scheduleImageParentResponse.setPictureList(pictureList);
            }

        } else {
            scheduleImageParentResponse.setWeekName(ConvertData.convertDateToWeekname(scheduleFile.getFromFileTsime()));
            scheduleImageParentResponse.setPictureList(pictureList);
        }

        return scheduleImageParentResponse;
    }

    @Override
    public ListScheduleFileParentResponse findFileAllWeek(UserPrincipal userPrincipal, Pageable pageable, LocalDate localDate) {
        ListScheduleFileParentResponse listScheduleFileParentResponse = new ListScheduleFileParentResponse(); // schedule list
        List<ScheduleFileParentResponse> dataList = new ArrayList<>();
        Long idKid = userPrincipal.getIdKidLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        Long idClass = kids.getMaClass().getId();
        // List all schedule file.
        List<ScheduleFile> scheduleFileList = scheduleFileRepository.searchScheduleFile(userPrincipal.getIdSchoolLogin(), idClass, localDate);
        scheduleFileList.forEach(x -> {
            List<UrlScheuldeFile> urlScheuldeFileList = List.copyOf(x.getUrlScheuldeFileList());
            ScheduleFileParentResponse model = new ScheduleFileParentResponse();
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

            model.setFileList(fileList);
            model.setDate(x.getFromFileTsime());
            model.setWeek(ConvertData.convertDateToWeekname(x.getFromFileTsime()));
            if (CollectionUtils.isEmpty(fileList) && CollectionUtils.isEmpty(pictureList)) {
                model.setFileList(new ArrayList<>());
                model.setDate(x.getFromFileTsime());
                model.setWeek("");
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
        long count = scheduleFileRepository.countScheduleFile(userPrincipal.getIdSchoolLogin(), idClass, localDate);
        boolean lastPage = count <= 20;
        listScheduleFileParentResponse.setDataList(dataList);
        listScheduleFileParentResponse.setLastPage(lastPage);
        
//        long countEmpty = listScheduleFileParentResponse.getDataList().stream().filter(x -> !CollectionUtils.isEmpty(x.getFileList())).count();
//        if (countEmpty == 0) {
//        listScheduleFileParentResponse.setDataList(new ArrayList<>());
//        }
        return listScheduleFileParentResponse;
    }
}
