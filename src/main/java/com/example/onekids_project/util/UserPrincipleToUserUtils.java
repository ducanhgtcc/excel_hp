package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-04-13 14:00
 *
 * @author lavanviet
 */
@Component
public class UserPrincipleToUserUtils {
    private static MaUserRepository maUserRepository;
    private static InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    public UserPrincipleToUserUtils(MaUserRepository maUserRepository, InfoEmployeeSchoolRepository infoEmployeeSchoolRepository) {
        this.maUserRepository = maUserRepository;
        this.infoEmployeeSchoolRepository = infoEmployeeSchoolRepository;
    }

    public static InfoEmployeeSchool getInfoEmployeeFromPrinciple(UserPrincipal principal) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(principal.getIdSchoolLogin()) && x.isDelActive() && x.isActivated()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_EMPLOYEE);
        } else if (infoEmployeeSchoolList.size() == 1) {
            return infoEmployeeSchoolList.get(0);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_MANY_INSCHOOL);
        }
    }

    public static InfoEmployeeSchool getInfoEmployeeFromPrinciple(MaUser maUser) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(SchoolUtils.getIdSchool()) && x.isDelActive() && x.isActivated()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_EMPLOYEE);
        } else if (infoEmployeeSchoolList.size() == 1) {
            return infoEmployeeSchoolList.get(0);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_MANY_INSCHOOL);
        }
    }

    public static InfoEmployeeSchool getInfoEmployeeFromMaUser(Long idUser, Long idSchool) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(idUser).orElseThrow();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(idSchool) && x.isDelActive() && x.isActivated()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_EMPLOYEE);
        } else if (infoEmployeeSchoolList.size() == 1) {
            return infoEmployeeSchoolList.get(0);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_MANY_INSCHOOL);
        }
    }

    public static List<InfoEmployeeSchool> getInfoEmployeeInSchool(Long idSchool) {
        return infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndActivatedTrueAndDelActiveTrue(idSchool, EmployeeConstant.STATUS_WORKING, AppTypeConstant.SCHOOL);
    }

    public static List<InfoEmployeeSchool> getInfoEmployeeInSchoolHasAccount(Long idSchool) {
        return infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndEmployeeIsNotNullAndActivatedTrueAndDelActiveTrue(idSchool, EmployeeConstant.STATUS_WORKING, AppTypeConstant.SCHOOL);
    }

    public static List<Long> getIdClassListTeacher(UserPrincipal principal) {
        InfoEmployeeSchool infoEmployeeSchool = getInfoEmployeeFromPrinciple(principal);
        List<MaClass> maClassList = infoEmployeeSchool.getExEmployeeClassList().stream().filter(BaseEntity::isDelActive).map(ExEmployeeClass::getMaClass).collect(Collectors.toList());
        return maClassList.stream().map(BaseEntity::getId).collect(Collectors.toList());
    }

    public static List<MaClass> getClassListTeacher(UserPrincipal principal) {
        InfoEmployeeSchool infoEmployeeSchool = getInfoEmployeeFromPrinciple(principal);
        return infoEmployeeSchool.getExEmployeeClassList().stream().filter(BaseEntity::isDelActive).map(ExEmployeeClass::getMaClass).collect(Collectors.toList());
    }

    /**
     * lấy tài khoản của nhân viên ở nhiều trường
     * chưa xóa
     * được kích hoạt
     * có tồn tại tài khoản
     *
     * @param maUser
     * @return
     */
    public static List<InfoEmployeeSchool> getInfoEmployeeInMaUser(MaUser maUser) {
        return maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(x -> x.isDelActive() && x.isActivated() && x.getEmployee() != null).collect(Collectors.toList());
    }
}
