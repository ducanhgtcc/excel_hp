package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserInforUtils {
    private static MaUserRepository maUserRepository;
    private static KidsRepository kidsRepository;
    private static InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    public UserInforUtils(MaUserRepository maUserRepository, KidsRepository kidsRepository, InfoEmployeeSchoolRepository infoEmployeeSchoolRepository) {
        UserInforUtils.maUserRepository = maUserRepository;
        UserInforUtils.kidsRepository = kidsRepository;
        UserInforUtils.infoEmployeeSchoolRepository = infoEmployeeSchoolRepository;
    }

    public static MaUserRepository getBeanMaUserRespository() {
        return maUserRepository;
    }

    /**
     * lấy thông tin maUser theo id
     *
     * @param id
     * @return
     */
    public static MaUser getMaUser(Long id) {
        return getBeanMaUserRespository().findById(id).orElseThrow(() -> new NotFoundException("not found maUser by id"));
    }

    public static Employee getEmployee(Long id) {
        return getBeanMaUserRespository().findById(id).orElseThrow(() -> new NotFoundException("not found maUser by id")).getEmployee();
    }

    /**
     * lấy họ tên theo id
     *
     * @param id
     * @return
     */
    public static String getFullName(Long id) {
        MaUser maUser = getBeanMaUserRespository().findById(id).orElseThrow(() -> new NotFoundException("not found maUser by id"));
        return maUser.getFullName();
    }

    public static List<Kids> getKidsInSchool() {
        return kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatus(SchoolUtils.getIdSchool(), KidsStatusConstant.STUDYING).stream().filter(Kids::isActivated).collect(Collectors.toList());
    }

    public static List<InfoEmployeeSchool> getTeacherInSchool() {
        return infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndActivatedTrueAndDelActiveTrue(SchoolUtils.getIdSchool(), EmployeeConstant.STATUS_WORKING, AppTypeConstant.TEACHER);
    }

    public static List<InfoEmployeeSchool> getPlusInSchool() {
        return infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndActivatedTrueAndDelActiveTrue(SchoolUtils.getIdSchool(), EmployeeConstant.STATUS_WORKING, AppTypeConstant.SCHOOL);
    }

    public static List<Kids> getKidsInSchoolHasAccount() {
        return getKids(SchoolUtils.getIdSchool());
    }

    public static List<InfoEmployeeSchool> getTeacherInSchoolHasAccount() {
        return getTeacher(SchoolUtils.getIdSchool());
    }

    public static List<InfoEmployeeSchool> getPlusInSchoolHasAccount() {
        return getPlus(SchoolUtils.getIdSchool());
    }

    public static List<Kids> getKidsInSchoolHasAccountForIdSchool(Long idSchool) {
        return getKids(idSchool);
    }

    public static List<InfoEmployeeSchool> getTeacherInSchoolHasAccountForIdSchool(Long idSchool) {
        return getTeacher(idSchool);
    }

    public static List<InfoEmployeeSchool> getPlusInSchoolHasAccountForIdSchool(Long idSchool) {
        return getPlus(idSchool);
    }

    private static List<Kids> getKids(Long idSchool) {
        List<Kids> list = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatusAndParentNotNull(idSchool, KidsStatusConstant.STUDYING);
        list = list.stream().filter(x -> x.isActivated() && x.getParent().getMaUser().isDelActive() && x.getParent().getMaUser().isActivated()).collect(Collectors.toList());
        return list;
    }

    private static List<InfoEmployeeSchool> getTeacher(Long idSchool) {
        List<InfoEmployeeSchool> list = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndEmployeeNotNullAndActivatedTrueAndDelActiveTrue(idSchool, EmployeeConstant.STATUS_WORKING, AppTypeConstant.TEACHER);
        list = list.stream().filter(x -> x.getEmployee().getMaUser().isDelActive() && x.getEmployee().getMaUser().isActivated()).collect(Collectors.toList());
        return list;
    }

    private static List<InfoEmployeeSchool> getPlus(Long idSchool) {
        List<InfoEmployeeSchool> list = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndEmployeeNotNullAndActivatedTrueAndDelActiveTrue(idSchool, EmployeeConstant.STATUS_WORKING, AppTypeConstant.SCHOOL);
        list = list.stream().filter(x -> x.getEmployee().getMaUser().isDelActive() && x.getEmployee().getMaUser().isActivated()).collect(Collectors.toList());
        return list;
    }

}
