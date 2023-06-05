package com.example.onekids_project.mobile.service.serviceimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.appversion.AppVersion;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.response.AppVersionResponse;
import com.example.onekids_project.mobile.service.servicecustom.AppVersionService;
import com.example.onekids_project.repository.AppVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AppVersionImpl implements AppVersionService {

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Autowired
    private ListMapper listMapper;

    @Override
    public List<AppVersionResponse> findAllAppVersion() {
        Iterable<AppVersion> appVersionIterable = appVersionRepository.findAll();
        List<AppVersion> appVersionList = StreamSupport.stream(appVersionIterable.spliterator(), false).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(appVersionList)) {
            return null;
        }
        List<AppVersionResponse> appVersionResponseList = listMapper.mapList(appVersionList, AppVersionResponse.class);
        return appVersionResponseList;
    }

    @Override
    public boolean createAppVersion() {
        List<AppVersion> appVersionList = appVersionRepository.findByDelActiveTrue();
        if (!CollectionUtils.isEmpty(appVersionList)) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            AppVersion appVersion = new AppVersion();
            appVersion.setApiUrl("https://api.onekids.edu.vn/");
            appVersion.setVersion("1.1.0");
            appVersion.setIdCreated(0l);
            appVersion.setCreatedDate(LocalDateTime.now());
            if (i == 0) {
                appVersion.setAppType(AppTypeConstant.SCHOOL);
                appVersion.setAppOs("android");
            }
            if (i == 1) {
                appVersion.setAppType(AppTypeConstant.SCHOOL);
                appVersion.setAppOs("ios");
            }
            if (i == 2) {
                appVersion.setAppType(AppTypeConstant.TEACHER);
                appVersion.setAppOs("android");
            }
            if (i == 3) {
                appVersion.setAppType(AppTypeConstant.TEACHER);
                appVersion.setAppOs("ios");
            }
            if (i == 4) {
                appVersion.setAppType(AppTypeConstant.PARENT);
                appVersion.setAppOs("android");
            }
            if (i == 5) {
                appVersion.setAppType(AppTypeConstant.PARENT);
                appVersion.setAppOs("ios");
            }
            appVersionRepository.save(appVersion);

        }
        return true;
    }
}
