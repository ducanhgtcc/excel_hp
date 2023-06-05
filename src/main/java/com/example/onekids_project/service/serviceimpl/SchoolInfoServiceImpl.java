package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.entity.school.SchoolInfo;
import com.example.onekids_project.repository.SchoolInfoRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.schoolconfig.UpdateBankInfoRequest;
import com.example.onekids_project.response.caskinternal.SchoolInfoBankResponses;
import com.example.onekids_project.response.school.SchoolInfoResponse;
import com.example.onekids_project.response.supperplus.SchoolInfoConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolInfoService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.util.SchoolUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * date 2021-03-05 15:25
 *
 * @author lavanviet
 */
@Service
public class SchoolInfoServiceImpl implements SchoolInfoService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public SchoolInfoResponse findSchoolInfo(UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found schoolInfo by id"));
        return modelMapper.map(schoolInfo, SchoolInfoResponse.class);
    }

    @Override
    public boolean updateBankInfo(UserPrincipal principal, UpdateBankInfoRequest request) {
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow();
        modelMapper.map(request, schoolInfo);
        schoolInfoRepository.save(schoolInfo);
        return true;
    }

    @Override
    public SchoolInfoBankResponses findDetailSchoolInfoBank(UserPrincipal principal) {
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow();
        return modelMapper.map(schoolInfo, SchoolInfoBankResponses.class);
    }

    @Override
    public SchoolInfoConfigResponse findSchoolInfoConfig() {
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(SchoolUtils.getIdSchool()).orElseThrow();
        return modelMapper.map(schoolInfo, SchoolInfoConfigResponse.class);
    }

    @Override
    public void updateSchoolInfoConfig(SchoolInfoConfigResponse request) {
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(SchoolUtils.getIdSchool()).orElseThrow();
        modelMapper.map(request, schoolInfo);
        schoolInfoRepository.save(schoolInfo);
    }
}
