package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.entity.appversion.AppVersion;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AppVersionRepository;
import com.example.onekids_project.request.system.AppVersionConfigRequest;
import com.example.onekids_project.response.system.AppVersionConfigResponse;
import com.example.onekids_project.service.servicecustom.AppVersionConfigService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class AppVersionConfigServiceImpl implements AppVersionConfigService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppVersionRepository appVersionRepository;

    @Override
    public List<AppVersionConfigResponse> findAllAppVersion() {
        List<AppVersion> appVersionList = appVersionRepository.findByDelActiveTrue();
        if (CollectionUtils.isEmpty(appVersionList)) {
            return null;
        }
        List<AppVersionConfigResponse> appVersionConfigResponseList = listMapper.mapList(appVersionList, AppVersionConfigResponse.class);
        return appVersionConfigResponseList;
    }

    @Override
    public AppVersionConfigResponse updateAppVersion(AppVersionConfigRequest appVersionConfigRequest) {
        AppVersion appVersion = appVersionRepository.findByIdAndDelActiveTrue(appVersionConfigRequest.getId()).orElseThrow(() -> new NotFoundException("not found app version by id"));
        modelMapper.map(appVersionConfigRequest, appVersion);
        AppVersion appVersionSaved = appVersionRepository.save(appVersion);
        AppVersionConfigResponse appVersionConfigResponse = modelMapper.map(appVersionSaved, AppVersionConfigResponse.class);
        return appVersionConfigResponse;
    }
}
