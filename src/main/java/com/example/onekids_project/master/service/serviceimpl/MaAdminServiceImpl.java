package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.usermaster.MaAdmin;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.admin.MaAdminCreateRequest;
import com.example.onekids_project.master.request.admin.MaAdminSearchRequest;
import com.example.onekids_project.master.request.admin.MaAdminUpdateRequest;
import com.example.onekids_project.master.response.admin.AdminForSchoolResponse;
import com.example.onekids_project.master.response.admin.MaAdminAccountResponse;
import com.example.onekids_project.master.response.admin.MaAdminResponse;
import com.example.onekids_project.master.response.admin.MaAdminSchoolResponse;
import com.example.onekids_project.master.service.MaAdminService;
import com.example.onekids_project.repository.MaAdminRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.security.payload.AccountCreateData;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.util.GenerateCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaAdminServiceImpl implements MaAdminService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private MaAdminRepository maAdminRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public List<MaAdminResponse> searchAllMaAdmin(MaAdminSearchRequest maAdminSearchRequest) {
        List<MaAdmin> maAdminList = maAdminRepository.searchMaAdmin(maAdminSearchRequest);
        return listMapper.mapList(maAdminList, MaAdminResponse.class);
    }

    @Transactional
    @Override
    public MaAdminResponse udpateAdmin(MaAdminUpdateRequest maAdminUpdateRequest) {
        MaAdmin maAdmin = maAdminRepository.findByIdAndDelActiveTrue(maAdminUpdateRequest.getId()).orElseThrow(() -> new NotFoundException("not found admin by id"));
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(maAdmin.getMaUser().getId()).orElseThrow(() -> new NotFoundException("not found mauser by id"));
        //set trước khi mapper của maAdmin
        if (!maUser.getPasswordShow().equals(maAdminUpdateRequest.getMaUser().getPasswordShow())) {
            maAdmin.getMaUser().setPasswordHash(passwordEncoder.encode(maAdminUpdateRequest.getMaUser().getPasswordShow()));
        }
        if (!maUser.getUsername().equals(maAdminUpdateRequest.getMaUser().getUsername())) {
            maUserService.checkExistUsernameAndAppType(maAdminUpdateRequest.getMaUser().getUsername(), AppTypeConstant.SYSTEM);
        }
        //nhân viên chuyển trạng thái thành nghỉ làm hoặc tạm nghỉ thì set kích hoạt tài khoản là false
        if (EmployeeConstant.STATUS_RETAIN.equals(maAdminUpdateRequest.getAdminStatus()) || EmployeeConstant.STATUS_LEAVE.equals(maAdminUpdateRequest.getAdminStatus())) {
            maAdmin.getMaUser().setActivated(AppConstant.APP_FALSE);
        }
        modelMapper.map(maAdminUpdateRequest, maAdmin);
        MaAdmin maAdminSaved = maAdminRepository.save(maAdmin);
        MaAdminResponse maAdminResponse = modelMapper.map(maAdminSaved, MaAdminResponse.class);
        return maAdminResponse;
    }

    @Transactional
    @Override
    public MaAdminResponse createAdmin(MaAdminCreateRequest maAdminCreateRequest) {
        AccountCreateData accountCreateData = new AccountCreateData();
        this.setPropertiesSignUp(accountCreateData, maAdminCreateRequest);
        MaUser maUser = maUserService.createAccountOther(accountCreateData);
        MaAdmin maAdmin = modelMapper.map(maAdminCreateRequest, MaAdmin.class);
        maAdmin.setMaUser(maUser);
        maAdmin.setCode(GenerateCode.codeAdmin());
        MaAdmin maAdminSaved = maAdminRepository.save(maAdmin);
        MaAdminResponse maAdminResponse = modelMapper.map(maAdminSaved, MaAdminResponse.class);
        return maAdminResponse;
    }

    @Transactional
    @Override
    public boolean deleteAdmin(Long id) {
        MaAdmin maAdmin = maAdminRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        if (!CollectionUtils.isEmpty(maAdmin.getSchoolList())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Còn tồn tại trường trong nhân viên");
        }
        maAdmin.getMaUser().setDelActive(AppConstant.APP_FALSE);
        maAdmin.setDelActive(AppConstant.APP_FALSE);
        maAdminRepository.save(maAdmin);
        return true;
    }

    @Override
    public List<MaAdminAccountResponse> findMaAdminAccount() {
        List<MaUser> maUserList = maUserRepository.findByAppTypeAndDelActiveTrueOrderByFullName(AppTypeConstant.SYSTEM);
        List<MaAdminAccountResponse> maAdminAccountResponseList = new ArrayList<>();
        maUserList.forEach(x -> {
            MaAdminAccountResponse response = new MaAdminAccountResponse();
            response.setId(x.getId());
            response.setFullName(x.getFullName());
            response.setPhone(x.getMaAdmin().getPhone());
            response.setUsername(x.getUsername());
            response.setPassword(x.getPasswordShow());
            response.setActivated(x.isActivated());
            maAdminAccountResponseList.add(response);
        });
        return maAdminAccountResponseList;
    }

    @Override
    public List<MaAdminSchoolResponse> searchAdminSchool(MaAdminSearchRequest maAdminSearchRequest) {
        maAdminSearchRequest.setStatus(EmployeeConstant.STATUS_WORKING);
        List<MaAdmin> maAdminList = maAdminRepository.searchMaAdmin(maAdminSearchRequest);
        List<MaAdminSchoolResponse> maAdminSchoolResponseList = listMapper.mapList(maAdminList, MaAdminSchoolResponse.class);
        return maAdminSchoolResponseList;
    }

    @Override
    public List<AdminForSchoolResponse> findSchoolOfAdmin(Long idAdmin) {
        maAdminRepository.findByIdAndDelActiveTrue(idAdmin).orElseThrow(() -> new NotFoundException("not found admin by id"));
        List<School> schoolList = schoolRepository.findAllByDelActiveTrue();
        List<Long> schoolForAdminList = schoolRepository.findByMaAdminList_Id(idAdmin).stream().map(x -> x.getId()).collect(Collectors.toList());
        List<AdminForSchoolResponse> cameraForClassResponseList = listMapper.mapList(schoolList, AdminForSchoolResponse.class);
        cameraForClassResponseList.forEach(y -> {
            schoolForAdminList.forEach(z -> {
                if (y.getId().equals(z)) {
                    y.setUsed(AppConstant.APP_TRUE);
                }
            });
        });
        return cameraForClassResponseList;
    }

    @Transactional
    @Override
    public boolean updateSchoolOfAdmin(Long idAdmin, List<IdObjectRequest> idObjectRequestList) {
        maAdminRepository.findByIdAndDelActiveTrue(idAdmin).orElseThrow(() -> new NotFoundException("not found admin by id"));
        maAdminRepository.deleteSchoolForAdmin(idAdmin);
        idObjectRequestList.forEach(x -> {
            maAdminRepository.insertSchoolForAdmin(idAdmin, x.getId());
        });
        return true;
    }

    private void setPropertiesSignUp(AccountCreateData accountCreateData, MaAdminCreateRequest maAdminCreateRequest) {
        accountCreateData.setFullName(maAdminCreateRequest.getFullName());
        accountCreateData.setPhone(maAdminCreateRequest.getPhone());
        accountCreateData.setAppType(AppTypeConstant.SYSTEM);
        accountCreateData.setUsername(maAdminCreateRequest.getUsername());
        accountCreateData.setPassword(GenerateCode.passwordAuto());
        accountCreateData.setGender(maAdminCreateRequest.getGender());
    }
}
