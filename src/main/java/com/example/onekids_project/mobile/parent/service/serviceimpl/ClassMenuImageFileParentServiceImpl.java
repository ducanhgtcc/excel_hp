package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.ManuFile;
import com.example.onekids_project.entity.classes.UrlMenuFile;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.parent.response.menuclass.ListMenuFileParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuFileParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuImageWeekParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuImageFileParentService;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.repository.ClassMenuFileRepository;
import com.example.onekids_project.repository.KidsRepository;
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
public class ClassMenuImageFileParentServiceImpl implements ClassMenuImageFileParentService {

    @Autowired
    KidsRepository kidsRepository;

    @Autowired
    ClassMenuFileRepository classMenuFileRepository;

    @Override
    public MenuImageWeekParentResponse findImageWeek(UserPrincipal userPrincipal, LocalDate localDate) {
        MenuImageWeekParentResponse menuImageWeekParentResponse = new MenuImageWeekParentResponse();
        Long idKid = userPrincipal.getIdKidLogin();     //  get Long idKid
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        Long idClass = kids.getMaClass().getId(); // idclass
        LocalDate monday = ConvertData.getMondayOfWeek(localDate);
        ManuFile manuFile = classMenuFileRepository.searchMenuImageWeek(userPrincipal.getIdSchoolLogin(), idClass, monday);
        Set<UrlMenuFile> urls = manuFile.getUrlMenuFileList();
        if (urls == null || urls.size() == 0) {
            menuImageWeekParentResponse.setPictureList(new ArrayList<>());
            menuImageWeekParentResponse.setWeekName("");
            return menuImageWeekParentResponse;
        }
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
            menuImageWeekParentResponse.setPictureList(new ArrayList<>());
            menuImageWeekParentResponse.setWeekName("");

            if (!CollectionUtils.isEmpty(fileList)) {
                pictureList.add(MobileConstant.NO_IMAGE);
                menuImageWeekParentResponse.setPictureList(pictureList);
            }

        } else {
            menuImageWeekParentResponse.setWeekName(ConvertData.convertDateToWeekname(manuFile.getFromFileTime()));
            menuImageWeekParentResponse.setPictureList(pictureList);
        }
        return menuImageWeekParentResponse;
    }

    @Override
    public ListMenuFileParentResponse findFileAllWeek(UserPrincipal userPrincipal, Pageable pageable, LocalDate localDate) {
        ListMenuFileParentResponse listMenuFileParentResponse = new ListMenuFileParentResponse(); // menu list
        List<MenuFileParentResponse> dataList = new ArrayList<>();
        Long idKid = userPrincipal.getIdKidLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found kids by id in schedule mobile"));
        Long idClass = kids.getMaClass().getId();
        // List all menu file.
        List<ManuFile> manuFileList = classMenuFileRepository.searchMenuFile(userPrincipal.getIdSchoolLogin(), idClass, localDate);
        manuFileList.forEach(x -> {
            List<UrlMenuFile> urlMenuFileList = List.copyOf(x.getUrlMenuFileList());
            MenuFileParentResponse model = new MenuFileParentResponse();
            model.setDate(x.getFromFileTime());
            model.setWeek(ConvertData.convertDateToWeekname(x.getFromFileTime()));
            List<AttachFileMobileResponse> fileList = new ArrayList<>();
            List<String> pictureList = new ArrayList<>();
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
            model.setFileList(fileList);
            model.setDate(x.getFromFileTime());
            model.setWeek(ConvertData.convertDateToWeekname(x.getFromFileTime()));
            if (CollectionUtils.isEmpty(fileList) && CollectionUtils.isEmpty(pictureList)) {
                model.setFileList(new ArrayList<>());
                model.setDate(x.getFromFileTime());
                model.setWeek("");
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
        long count = classMenuFileRepository.countMenuFile(userPrincipal.getIdSchoolLogin(), idClass, localDate);
        boolean lastPage = count < 20;
        listMenuFileParentResponse.setDataList(dataList);
        listMenuFileParentResponse.setLastPage(lastPage);
//        long countEmpty = listMenuFileParentResponse.getDataList().stream().filter(x -> !CollectionUtils.isEmpty(x.getFileList())).count();
//        if (countEmpty == 0) {
//            listMenuFileParentResponse.setDataList(new ArrayList<>());
//        }
        return listMenuFileParentResponse;
    }
}