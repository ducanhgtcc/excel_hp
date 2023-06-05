package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.usermaster.SchoolMaster;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.SchoolMasterSearchRequest;
import com.example.onekids_project.master.request.school.SchoolMasterCreateRequest;
import com.example.onekids_project.master.request.school.SchoolMasterUpdateRequest;
import com.example.onekids_project.master.response.school.ListSchoolMasterResponse;
import com.example.onekids_project.master.response.school.SchoolMasterForSchoolResponse;
import com.example.onekids_project.master.response.school.SchoolMasterResponse;
import com.example.onekids_project.master.service.AccountSchoolService;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SchoolMasterRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.security.payload.AccountCreateData;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class AccountSchoolServiceImpl implements AccountSchoolService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SchoolMasterRepository schoolMasterRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private SchoolService schoolService;


    @Transactional
    @Override
    public boolean createAccountSchool(SchoolMasterCreateRequest schoolMasterCreateRequest) {
        AccountCreateData accountCreateData = new AccountCreateData();
        this.setPropertiesSignUp(accountCreateData, schoolMasterCreateRequest);
        MaUser maUser = maUserService.createAccountOther(accountCreateData);
        School school = schoolRepository.findByIdAndDelActiveTrue(schoolMasterCreateRequest.getIdSchool()).orElseThrow(() -> new NotFoundException("not found agent by id"));
        SchoolMaster schoolMaster = modelMapper.map(schoolMasterCreateRequest, SchoolMaster.class);
        schoolMaster.setMaUser(maUser);
        schoolMaster.setSchool(school);
        schoolMasterRepository.save(schoolMaster);
        return true;
    }

    @Override
    public ListSchoolMasterResponse getAllAccountSchool(SchoolMasterSearchRequest request) {
        ListSchoolMasterResponse response = new ListSchoolMasterResponse();
        List<Long> idSchoolList = ConvertData.getIdSchoolListInAgent(schoolService.findSchoolInAgent(request.getIdAgent()));
        List<SchoolMaster> schoolMasterList = schoolMasterRepository.findAllSchoolMaster(request, idSchoolList);
        long count = schoolMasterRepository.countSchoolMaster(request, idSchoolList);
        List<SchoolMasterResponse> dataList = listMapper.mapList(schoolMasterList, SchoolMasterResponse.class);
        response.setDataList(dataList);
        response.setTotal(count);
        return response;
    }

    @Override
    public List<SchoolMasterForSchoolResponse> getAccountSchoolByIdSchool(Long idSchool) {
        List<SchoolMaster> schoolMasterList = schoolMasterRepository.findBySchoolId(idSchool);
        List<SchoolMasterForSchoolResponse> schoolMasterForSchoolResponseList = listMapper.mapList(schoolMasterList, SchoolMasterForSchoolResponse.class);
        return schoolMasterForSchoolResponseList;
    }

    @Override
    public SchoolMasterResponse updateAccountSchool(SchoolMasterUpdateRequest schoolMasterUpdateRequest) {
        SchoolMaster schoolMaster = schoolMasterRepository.findByIdAndDelActiveTrue(schoolMasterUpdateRequest.getId()).orElseThrow(() -> new NotFoundException("not found schoolMaster by id"));
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(schoolMasterUpdateRequest.getMaUser().getId()).orElseThrow(() -> new NotFoundException("not found mauser by id"));
        if (!maUser.getPasswordShow().equals(schoolMasterUpdateRequest.getMaUser().getPasswordShow())) {
            schoolMaster.getMaUser().setPasswordHash(passwordEncoder.encode(schoolMasterUpdateRequest.getMaUser().getPasswordShow()));
        }
        /**
         * check username exist
         */
        if (!maUser.getUsername().equals(schoolMasterUpdateRequest.getMaUser().getUsername())) {
            maUserService.checkExistUsernameAndAppType(schoolMasterUpdateRequest.getMaUser().getUsername(), AppTypeConstant.SUPPER_SCHOOL);
        }
        modelMapper.map(schoolMasterUpdateRequest, schoolMaster);
        SchoolMaster schoolMasterSaved = schoolMasterRepository.save(schoolMaster);
        SchoolMasterResponse schoolMasterResponse = modelMapper.map(schoolMasterSaved, SchoolMasterResponse.class);
        return schoolMasterResponse;
    }

    @Override
    public boolean deleteSchoolMaster(Long id) {
        SchoolMaster schoolMaster = schoolMasterRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found agentmaster by id"));
        schoolMaster.getMaUser().setDelActive(AppConstant.APP_FALSE);
        schoolMaster.setDelActive(AppConstant.APP_FALSE);
        schoolMasterRepository.save(schoolMaster);
        return true;
    }

    /**
     * set properties
     *
     * @param accountCreateData
     * @param schoolMasterCreateRequest
     */
    private void setPropertiesSignUp(AccountCreateData accountCreateData, SchoolMasterCreateRequest schoolMasterCreateRequest) {
        accountCreateData.setFullName(schoolMasterCreateRequest.getFullName());
        accountCreateData.setPhone(schoolMasterCreateRequest.getPhone());
        accountCreateData.setAppType(AppTypeConstant.SUPPER_SCHOOL);
        accountCreateData.setUsername(schoolMasterCreateRequest.getUsername());
        accountCreateData.setPassword(schoolMasterCreateRequest.getPassword());
        accountCreateData.setGender(schoolMasterCreateRequest.getGender());
    }
}
