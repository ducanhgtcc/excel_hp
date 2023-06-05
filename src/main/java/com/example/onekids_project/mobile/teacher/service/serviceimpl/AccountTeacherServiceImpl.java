package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mobile.teacher.response.account.AccountTeacherResponse;
import com.example.onekids_project.master.response.ProfileMobileResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AccountTeacherService;
import com.example.onekids_project.repository.EmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.EmployeeUtil;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class AccountTeacherServiceImpl implements AccountTeacherService {
    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private SysInforRepository sysInforRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Override
    public AccountTeacherResponse findInforAccount(UserPrincipal principal) {
//        CommonValidate.checkDataNoClassTeacher(principal);
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
        AccountTeacherResponse model = new AccountTeacherResponse();
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        InfoEmployeeSchool infoEmployeeSchool = EmployeeUtil.convertEmployeeToInfoEmployeeSchool(principal.getIdSchoolLogin(), maUser.getEmployee());
        model.setTeacherName(infoEmployeeSchool.getFullName());
        model.setAvatar(AvatarUtils.getAvatarInfoEmployee(infoEmployeeSchool));
        model.setEmail(StringUtils.isNotBlank(infoEmployeeSchool.getEmail()) ? infoEmployeeSchool.getEmail() : "");
        model.setPhone(StringUtils.isNotBlank(infoEmployeeSchool.getPhone()) ? infoEmployeeSchool.getPhone() : "");
        List<String> departmentNameList = infoEmployeeSchool.getDepartmentEmployeeList().stream().map(ExDepartmentEmployee::getDepartment).collect(Collectors.toList()).stream().map(Department::getDepartmentName).collect(Collectors.toList());
        model.setDepartmentNameList(departmentNameList);
        List<String> classNameList = infoEmployeeSchool.getExEmployeeClassList().stream().map(ExEmployeeClass::getMaClass).collect(Collectors.toList()).stream().map(MaClass::getClassName).collect(Collectors.toList());
        model.setClassNameList(classNameList);
        model.setGuideLink(StringUtils.isNotBlank(sysInfor.getGuideTeacherLink()) ? sysInfor.getGuideTeacherLink() : "");
        return model;
    }

    @Override
    public boolean saveAvatar(UserPrincipal principal, MultipartFile multipartFile) throws IOException {
//        CommonValidate.checkDataNoClassTeacher(principal);
        if (multipartFile == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có ảnh");
        }
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in teacher"));
        InfoEmployeeSchool infoEmployeeSchool = EmployeeUtil.convertEmployeeToInfoEmployeeSchool(principal.getIdSchoolLogin(), maUser.getEmployee());
        if (StringUtils.isNotBlank(infoEmployeeSchool.getAvatar())) {
            HandleFileUtils.deletePictureInFolder(infoEmployeeSchool.getUrlLocalAvatar());
        }
        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(multipartFile, principal.getIdSchoolLogin(), UploadDownloadConstant.AVATAR);
        infoEmployeeSchool.setAvatar(handleFileResponse.getUrlWeb());
        infoEmployeeSchool.setUrlLocalAvatar(handleFileResponse.getUrlLocal());
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
        return true;
    }
}
