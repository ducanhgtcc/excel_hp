package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.school.AppIconParent;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.AppIconParentRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.system.IconParentConfigRequest;
import com.example.onekids_project.response.school.AppIconResponse;
import com.example.onekids_project.response.school.ListAppIconResponse;
import com.example.onekids_project.response.system.IconParentConfigResponse;
import com.example.onekids_project.service.servicecustom.AppIconParentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AppIconParentServiceImpl implements AppIconParentService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppIconParentRepository appIconParentRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public ListAppIconResponse getAppIconParent(Long idSchool) {
        AppIconParent appIconParent = appIconParentRepository.findAppIconParent(idSchool);
        if (appIconParent == null) {
            return null;
        }

        ListAppIconResponse listAppIconResponse = new ListAppIconResponse();
        if (appIconParent.isMessage()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getMessageName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isMedicine()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getMedicineName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isAbsent()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getAbsentName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isAlbum()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getAlbumName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isEvaluate()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getEvaluateName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isAttendance()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getAttendanceName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isStudentFees()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getStudentFeesName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isLearn()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getLearnName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isMenu()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getMenuName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isVideo()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getVideoName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isCamera()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getCameraName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isKidsInfo()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getKidsInfoName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isUtility()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getUtilityName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isFacebook()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getFacebookName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isFeedback()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(appIconParent.getFeedbackName());
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }
        if (appIconParent.isNews()) {
            AppIconResponse appIconResponse = new AppIconResponse();
            appIconResponse.setIconName(AppIconName.NEWS_NAME);
            appIconResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconResponse.getAppIconResponseList().add(appIconResponse);
        }

        List<AppIconResponse> appIconResponseList = listAppIconResponse.getAppIconResponseList();
        if (CollectionUtils.isEmpty(appIconResponseList)) {
            return null;
        }
        if (appIconResponseList.size() <= 10) {
            listAppIconResponse.setAppIconResponseList1(appIconResponseList);
        } else {
            listAppIconResponse.setAppIconResponseList1(listAppIconResponse.getAppIconResponseList().subList(0, 10));
            listAppIconResponse.setAppIconResponseList2(listAppIconResponse.getAppIconResponseList().subList(10, appIconResponseList.size()));
        }

        return listAppIconResponse;
    }

    @Override
    public boolean createAppIconParent(Long idSchool) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE);
        if (schoolOptional.isEmpty()) {
            return false;
        }
        Optional<AppIconParent> appIconParentOptional = appIconParentRepository.findBySchoolId(idSchool);
        if (appIconParentOptional.isPresent()) {
            return false;
        }
        School school = schoolOptional.get();
        AppIconParent appIconParent = new AppIconParent();
        appIconParent.setSchool(school);
        appIconParentRepository.save(appIconParent);
        return true;
    }

    @Override
    public IconParentConfigResponse findIconByIdSchoolConfig(Long idSchool) {
        AppIconParent appIconParent = appIconParentRepository.findBySchoolId(idSchool).orElseThrow(() -> new NotFoundException("not found icon parent by id"));
        IconParentConfigResponse iconParentConfigResponse = modelMapper.map(appIconParent, IconParentConfigResponse.class);
        return iconParentConfigResponse;
    }

    @Override
    public IconParentConfigResponse updateIconParentShool(Long id, IconParentConfigRequest iconParentConfigRequest) {
        AppIconParent appIconParent = appIconParentRepository.findBySchoolId(id).orElseThrow(() -> new NotFoundException("not found icon parent by id"));
        modelMapper.map(iconParentConfigRequest, appIconParent);
        AppIconParent appIconParentSaved = appIconParentRepository.save(appIconParent);
        IconParentConfigResponse iconParentConfigResponse = modelMapper.map(appIconParentSaved, IconParentConfigResponse.class);
        return iconParentConfigResponse;
    }
}
