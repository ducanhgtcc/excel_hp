package com.example.onekids_project.service.serviceimpl.config;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.school.ConfigAttendanceEmployeeSchool;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.school.ConfigAttendanceEmployeeSchoolRequest;
import com.example.onekids_project.request.school.ConfigTimeAttendanceEmployeeRequest;
import com.example.onekids_project.request.schoolconfig.ClassConfigRequest;
import com.example.onekids_project.request.schoolconfig.ClassConfigSearchRequest;
import com.example.onekids_project.response.school.ConfigAttendanceEmployeeSchoolResponse;
import com.example.onekids_project.response.school.ConfigTimeAttendanceEmployeeSchoolResponse;
import com.example.onekids_project.response.schoolconfig.ClassConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.config.SchoolConfigService;
import com.example.onekids_project.util.GenerateCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchoolConfigImpl implements SchoolConfigService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolConfigRepository schoolConfigRepository;

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;

    @Autowired
    private ConfigAttendanceEmployeeSchoolRepository configAttendanceEmployeeSchoolRepository;

    @Override
    public SchoolConfig createSchoolConfig(Long idSchool) {
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        SchoolConfig schoolConfig = new SchoolConfig();
        schoolConfig.setSchool(school);
        schoolConfig.setVerifyCode(GenerateCode.getLetterUpperCase());
        SchoolConfig schoolConfigSaved = schoolConfigRepository.save(schoolConfig);
        return schoolConfigSaved;
    }


    @Override
    public List<ClassConfigResponse> searchMaclassConfig(UserPrincipal principal, ClassConfigSearchRequest classConfigSearchRequest) {
        List<MaClass> maClassList = maClassRepository.searchMaClassConfig(principal.getIdSchoolLogin(), classConfigSearchRequest);
        return listMapper.mapList(maClassList, ClassConfigResponse.class);
    }

    @Transactional
    @Override
    public boolean updateConfigClassAbsent(UserPrincipal principal, List<ClassConfigRequest> request) {
        Long idSchool = principal.getIdSchoolLogin();
        request.forEach(x -> {
            MaClass maClass = maClassRepository.findByIdAndIdSchoolAndDelActiveTrue(x.getId(), idSchool).orElseThrow();
            modelMapper.map(x, maClass);
            maClassRepository.save(maClass);
        });
        return true;
    }

    @Override
    public ConfigAttendanceEmployeeSchoolResponse getConfigAttendanceEmployeeSchool(UserPrincipal principal) {
        ConfigAttendanceEmployeeSchool configAttendanceEmployeeSchool = configAttendanceEmployeeSchoolRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow();
        return modelMapper.map(configAttendanceEmployeeSchool, ConfigAttendanceEmployeeSchoolResponse.class);
    }

    @Override
    public boolean updateConfigAttendanceEmployeeSchool(UserPrincipal principal, ConfigAttendanceEmployeeSchoolRequest request) {
        ConfigAttendanceEmployeeSchool configAttendanceEmployeeSchool = configAttendanceEmployeeSchoolRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow();
        modelMapper.map(request, configAttendanceEmployeeSchool);
        configAttendanceEmployeeSchoolRepository.save(configAttendanceEmployeeSchool);
        return true;
    }

    @Override
    public ConfigTimeAttendanceEmployeeSchoolResponse getConfigTimeAttendanceEmployeeSchool(UserPrincipal principal) {
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();
        ConfigTimeAttendanceEmployeeSchoolResponse response = modelMapper.map(schoolConfig, ConfigTimeAttendanceEmployeeSchoolResponse.class);
        return response;
    }

    @Override
    public boolean updateConfigTimeAttendanceEmployeeSchool(UserPrincipal principal, ConfigTimeAttendanceEmployeeRequest request) {
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();
        modelMapper.map(request, schoolConfig);
        schoolConfigRepository.save(schoolConfig);
        return true;
    }

}
