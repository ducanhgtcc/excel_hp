package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.entity.system.SysConfig;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.SchoolConfigRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.repository.SysConfigRepository;
import com.example.onekids_project.request.schoolconfig.SchoolConfigRequest;
import com.example.onekids_project.request.system.SystemConfigSchoolTotalRequest;
import com.example.onekids_project.response.system.SysConfigResponse;
import com.example.onekids_project.response.system.SystemConfigSchoolTotalResponse;
import com.example.onekids_project.service.servicecustom.SysConfigService;
import com.example.onekids_project.service.servicecustom.config.SupperPlusConfigService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
public class SysConfigServiceImpl implements SysConfigService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Autowired
    private SchoolConfigRepository schoolConfigRepository;

    @Autowired
    private SupperPlusConfigService supperPlusConfigService;

    @Override
    public SysConfigResponse createSysConfigForSchool(Long idSchool) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        SysConfig sysConfig = new SysConfig();
        sysConfig.setSchool(schoolOptional.get());
        SysConfig sysConfigSaved = sysConfigRepository.save(sysConfig);
        SysConfigResponse sysConfigResponse = modelMapper.map(sysConfigSaved, SysConfigResponse.class);
        return sysConfigResponse;
    }

    @Override
    public SystemConfigSchoolTotalResponse findSystemConfigSchool(Long idSchool) {
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolId(idSchool).orElseThrow(() -> new NotFoundException("not found school config by id"));
        SysConfig sysConfig = sysConfigRepository.findBySchoolId(idSchool).orElseThrow(() -> new NotFoundException("not found sysconfig by idschool"));

        SystemConfigSchoolTotalResponse response = new SystemConfigSchoolTotalResponse();
        SchoolConfigRequest schoolConfigRequest = modelMapper.map(schoolConfig, SchoolConfigRequest.class);
        SysConfigResponse sysConfigResponse = modelMapper.map(sysConfig, SysConfigResponse.class);
        response.setSchoolConfigInSysResponse(schoolConfigRequest);
        response.setSysConfigResponse(sysConfigResponse);
        response.setIdSchool(idSchool);
        return response;
    }

    @Transactional
    @Override
    public boolean updateSystemConfigSchool(SystemConfigSchoolTotalRequest systemConfigSchoolTotalRequest) {
        Long idSchool = systemConfigSchoolTotalRequest.getIdSchool();
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolId(idSchool).orElseThrow(() -> new NotFoundException("not found school config by id=" + systemConfigSchoolTotalRequest.getIdSchool()));
        SysConfig sysConfig = sysConfigRepository.findBySchoolId(systemConfigSchoolTotalRequest.getIdSchool()).orElseThrow(() -> new NotFoundException("not found sysconfig by idschool"));
        modelMapper.map(systemConfigSchoolTotalRequest.getSchoolConfigInSysResponse(), schoolConfig);
        modelMapper.map(systemConfigSchoolTotalRequest.getSysConfigResponse(), sysConfig);
        schoolConfigRepository.save(schoolConfig);
        sysConfigRepository.save(sysConfig);
        supperPlusConfigService.saveChangeAttendanceConfig(idSchool, systemConfigSchoolTotalRequest.getSchoolConfigInSysResponse());
        return true;
    }
}
