package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SchoolConfigRepository;
import com.example.onekids_project.request.commononekids.ChangePhoneSMSRequest;
import com.example.onekids_project.response.commononekids.PlusInSchoolResponse;
import com.example.onekids_project.response.commononekids.SchoolConfigCommonResponse;
import com.example.onekids_project.security.controller.AuthController;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.LoginRequest;
import com.example.onekids_project.service.servicecustom.CommonOnekidsService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.PermissionUtils;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.example.onekids_project.validate.RequestValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CommonOnekidsServiceImpl implements CommonOnekidsService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private AuthController authController;

    @Autowired
    private SchoolConfigRepository schoolConfigRepository;

    @Override
    public boolean changePhoneSmsUser(ChangePhoneSMSRequest request) {
        String appType = request.getAppType();
        if (appType.equals(AppTypeConstant.TEACHER) || appType.equals(AppTypeConstant.SCHOOL)) {
            this.updatePhoneEmployee(request);
        } else if (appType.equals(AppTypeConstant.PARENT)) {
            this.updatePhoneKids(request);
        }
        return true;
    }

    @Override
    public List<PlusInSchoolResponse> findInforEmployeeInEmployee(UserPrincipal principal) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = UserPrincipleToUserUtils.getInfoEmployeeInMaUser(maUser);
        List<PlusInSchoolResponse> responseList = listMapper.mapList(infoEmployeeSchoolList, PlusInSchoolResponse.class);
        responseList.forEach(x -> {
            if (x.getSchool().getId().equals(principal.getIdSchoolLogin())) {
                x.getSchool().setStatus(AppConstant.APP_TRUE);
            }
        });
        return responseList;
    }

    @Override
    public ResponseEntity updateChangeSchool(UserPrincipal principal, Long idShool) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        this.checkIdSchool(maUser, idShool);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAppType(maUser.getAppType());
        loginRequest.setUsername(ConvertData.getUsernameNoExtend(maUser.getUsername()));
        loginRequest.setPassword(maUser.getPasswordShow());
        maUser.getEmployee().setIdSchoolLogin(idShool);
        maUserRepository.save(maUser);
        ResponseEntity responseEntity = authController.authenticateUser(loginRequest);
        return responseEntity;
    }

    @Override
    public SchoolConfigCommonResponse getSchoolConfigCommon(UserPrincipal principal) {
        CommonValidate.checkPlusOrTeacher(principal);
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow();
        return modelMapper.map(schoolConfig, SchoolConfigCommonResponse.class);
    }

    @Override
    public String getAvatarUser(UserPrincipal principal) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        if (maUser.getAppType().equals(AppTypeConstant.SCHOOL) || maUser.getAppType().equals(AppTypeConstant.TEACHER)) {
            return maUser.getEmployee().getAvatar();
        } else if (maUser.getAppType().equals(AppTypeConstant.PARENT)) {
            return maUser.getParent().getAvatar();
        }
        return "";
    }

    @Override
    public Set<String> getApiUser(UserPrincipal principal) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        return PermissionUtils.getApiOfUser(maUser, principal.getIdSchoolLogin());
    }

    @Override
    public String getSchoolName(UserPrincipal principal) {
       return principal.getSchool().getSchoolName();
    }


    private void updatePhoneEmployee(ChangePhoneSMSRequest request) {
        RequestValidate.checkPhone(request.getNewPhone());
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        MaUser maUser = infoEmployeeSchool.getEmployee().getMaUser();
        maUser.setPhone(request.getNewPhone());
        maUserRepository.save(maUser);
    }

    private void updatePhoneKids(ChangePhoneSMSRequest request) {
        RequestValidate.checkPhone(request.getNewPhone());
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        MaUser maUser = kids.getParent().getMaUser();
        maUser.setPhone(request.getNewPhone());
        maUserRepository.save(maUser);
    }

    //check trường cần đổi có nằm trong danh sách trường của nhận viên đó ko
    private void checkIdSchool(MaUser maUser, Long idSchool) {

    }
}
