package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.school.AppIconPlus;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.AppIconPlusRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.system.IconPlusConfigRequest;
import com.example.onekids_project.response.school.AppIconPlusNotifyResponse;
import com.example.onekids_project.response.school.AppIconPlusResponse;
import com.example.onekids_project.response.school.ListAppIconPlusNotifyResponse;
import com.example.onekids_project.response.school.ListAppIconPlusResponse;
import com.example.onekids_project.response.system.IconPlusConfigResponse;
import com.example.onekids_project.service.servicecustom.AppIconPlusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class AppIconPlusServiceImpl implements AppIconPlusService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppIconPlusRepository appIconPlusRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public boolean createAppIconPlus(Long idSchool) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE);
        if (schoolOptional.isEmpty()) {
            return false;
        }
        Optional<AppIconPlus> appIconPlusOptional = appIconPlusRepository.findBySchoolId(idSchool);
        if (appIconPlusOptional.isPresent()) {
            return false;
        }
        School school = schoolOptional.get();
        AppIconPlus appIconPlus = new AppIconPlus();
        appIconPlus.setSchool(school);
        appIconPlusRepository.save(appIconPlus);
        return true;
    }

    @Override
    public IconPlusConfigResponse findIconByIdSchoolConfig(Long idSchool) {
        AppIconPlus appIconPlus = appIconPlusRepository.findBySchoolId(idSchool).orElseThrow(() -> new NotFoundException("not found icon teacher by id"));
        IconPlusConfigResponse iconPlusConfigResponse = modelMapper.map(appIconPlus, IconPlusConfigResponse.class);
        return iconPlusConfigResponse;
    }

    @Override
    public IconPlusConfigResponse updateIconSchool(Long id, IconPlusConfigRequest iconPlusConfigRequest) {
        AppIconPlus appIconPlus = appIconPlusRepository.findBySchoolId(id).orElseThrow(() -> new NotFoundException("not found icon plus by id"));
        modelMapper.map(iconPlusConfigRequest, appIconPlus);
        AppIconPlus appIconParentSaved = appIconPlusRepository.save(appIconPlus);
        IconPlusConfigResponse iconPlusConfigResponse = modelMapper.map(appIconParentSaved, IconPlusConfigResponse.class);
        return iconPlusConfigResponse;
    }

    @Override
    public ListAppIconPlusNotifyResponse getAppIconPlusNotify(Long idSchoolLogin) {
        AppIconPlus appIconPlus = appIconPlusRepository.findAppIconPlus(idSchoolLogin);
        if (appIconPlus == null) {
            return null;
        }

        ListAppIconPlusNotifyResponse listAppIconPlusNotifyResponse = new ListAppIconPlusNotifyResponse();

        if (appIconPlus.isMessage()) {
            AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
            appIconPlusNotifyResponse.setIconName(appIconPlus.getMessageName());
            appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
        }
        if (appIconPlus.isMedicine()) {
            AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
            appIconPlusNotifyResponse.setIconName(appIconPlus.getMedicineName());
            appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
        }
        if (appIconPlus.isAbsent()) {
            AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
            appIconPlusNotifyResponse.setIconName(appIconPlus.getAbsentName());
            appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
        }
        if (appIconPlus.isFeedback()) {
            AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
            appIconPlusNotifyResponse.setIconName(appIconPlus.getFeedbackName());
            appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
            listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
        }

        List<AppIconPlusNotifyResponse> listAppIconPlusNotifyResponses = listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList();
        if (CollectionUtils.isEmpty(listAppIconPlusNotifyResponses)) {
            return null;
        }
        if (listAppIconPlusNotifyResponses.size() <= 10) {
            listAppIconPlusNotifyResponse.setAppIconPlusNotifyResponseList1(listAppIconPlusNotifyResponses);
        } else {
            listAppIconPlusNotifyResponse.setAppIconPlusNotifyResponseList1(listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().subList(0, 10));
            listAppIconPlusNotifyResponse.setAppIconPlusNotifyResponseList2(listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList2().subList(10, listAppIconPlusNotifyResponses.size()));
        }

        return listAppIconPlusNotifyResponse;
    }
}
