package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.common.PlusConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.AccountType;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.department.SearchEmployeeRequest;
import com.example.onekids_project.mobile.plus.response.department.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.DepartmentPlusService;
import com.example.onekids_project.repository.DepartmentRepository;
import com.example.onekids_project.repository.ExDepartmentEmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentPlusServiceImpl implements DepartmentPlusService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private ExDepartmentEmployeeRepository exDepartmentEmployeeRepository;

    @Override
    public ListDepartmentPlusResponse searchDepartment(UserPrincipal principal) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Department> departmentList = departmentRepository.findDepartment(idSchool);
        ListDepartmentPlusResponse listMessagePlusResponse = new ListDepartmentPlusResponse();
        List<DepartmentPlusResponse> departmentPlusResponseList = new ArrayList<>();
        departmentList.forEach(x -> {
            DepartmentPlusResponse model = new DepartmentPlusResponse();
            model.setId(x.getId());
            model.setDepartmentName(x.getDepartmentName());
            model.setNumberEmployees((int) x.getDepartmentEmployeeList().stream().filter(y -> y.getInfoEmployeeSchool().isDelActive() && y.getInfoEmployeeSchool().getEmployeeStatus().equals(EmployeeConstant.STATUS_WORKING)).count());
            departmentPlusResponseList.add(model);
        });
        List<InfoEmployeeSchool> infoEmployeeSchoolRetainList = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndDelActiveTrue(idSchool, AppConstant.EMPLOYEE_STATUS_RETAIN);
        List<InfoEmployeeSchool> infoEmployeeSchoolLeaveList = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndDelActiveTrue(idSchool, AppConstant.EMPLOYEE_STATUS_LEAVE);

        DepartmentPlusResponse model2 = new DepartmentPlusResponse();
        model2.setId(-1L);
        model2.setDepartmentName(PlusConstant.EMPLOYEE_STATUS_RETAIN);
        model2.setNumberEmployees(infoEmployeeSchoolRetainList.size());
        departmentPlusResponseList.add(model2);

        DepartmentPlusResponse model3 = new DepartmentPlusResponse();
        model3.setId(-2L);
        model3.setDepartmentName(PlusConstant.EMPLOYEE_STATUS_LEAVE);
        model3.setNumberEmployees(infoEmployeeSchoolLeaveList.size());
        departmentPlusResponseList.add(model3);
        listMessagePlusResponse.setDataList(departmentPlusResponseList);
        return listMessagePlusResponse;
    }

    @Override
    public ListEmployeePlusResponse searchEmployeePlus(UserPrincipal principal, SearchEmployeeRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<EmployeePlusResponse> employeePlusResponseList = new ArrayList<>();
        ListEmployeePlusResponse listEmployeePlusResponse = new ListEmployeePlusResponse();
        // nhân viên tạm nghỉ
        if (request.getId() == -1) {
            List<InfoEmployeeSchool> infoEmployeeSchoolRetainList = infoEmployeeSchoolRepository.findEmployeeRetain(idSchool, AppConstant.EMPLOYEE_STATUS_RETAIN);
            infoEmployeeSchoolRetainList = infoEmployeeSchoolRetainList.stream().filter(a -> a.getAppType().equals(AppConstant.TYPE_TEACHER)).collect(Collectors.toList());
            infoEmployeeSchoolRetainList.forEach(x -> {
                EmployeePlusResponse model = new EmployeePlusResponse();
                model.setId(x.getId());
                model.setPhone(x.getPhone());
                model.setEmployeeName(x.getFullName());
                model.setAvartar(AvatarUtils.getAvatarInfoEmployee(x));
                if (x.getExEmployeeClassList().size() > 0) {
                    model.setClassName(x.getExEmployeeClassList().get(0).getMaClass().getClassName());
                } else {
                    model.setClassName("");
                }
                model.setLoginStatus(this.setLoginStatus(x));
                employeePlusResponseList.add(model);
            });
        }
        // nhân viên nghỉ làm
        if (request.getId() == -2) {
            List<InfoEmployeeSchool> infoEmployeeSchoolRetainList = infoEmployeeSchoolRepository.findEmployeeLeave(idSchool, AppConstant.EMPLOYEE_STATUS_LEAVE);
            infoEmployeeSchoolRetainList = infoEmployeeSchoolRetainList.stream().filter(b -> b.getAppType().equals(AppConstant.TYPE_TEACHER)).collect(Collectors.toList());
            infoEmployeeSchoolRetainList.forEach(x -> {
                EmployeePlusResponse model = new EmployeePlusResponse();
                model.setId(x.getId());
                model.setPhone(x.getPhone());
                model.setEmployeeName(x.getFullName());
                model.setAvartar(AvatarUtils.getAvatarInfoEmployee(x));
                if (x.getExEmployeeClassList().size() > 0) {
                    model.setClassName(x.getExEmployeeClassList().get(0).getMaClass().getClassName());
                } else {
                    model.setClassName("");
                }
                model.setLoginStatus(this.setLoginStatus(x));
                employeePlusResponseList.add(model);
            });
        }
        // tất cả
        if (request.getId() == -3) {
            List<InfoEmployeeSchool> infoEmployeeSchoolAlllist = infoEmployeeSchoolRepository.findAllInSchool(idSchool);
            infoEmployeeSchoolAlllist = infoEmployeeSchoolAlllist.stream().filter(c -> c.getAppType().equals(AppConstant.TYPE_TEACHER)).collect(Collectors.toList());
            infoEmployeeSchoolAlllist.forEach(x -> {
                EmployeePlusResponse model = new EmployeePlusResponse();
                model.setId(x.getId());
                model.setPhone(x.getPhone());
                model.setEmployeeName(x.getFullName());
                model.setAvartar(AvatarUtils.getAvatarInfoEmployee(x));
                if (x.getExEmployeeClassList().size() > 0) {
                    model.setClassName(x.getExEmployeeClassList().get(0).getMaClass().getClassName());
                } else {
                    model.setClassName("");
                }
                model.setLoginStatus(this.setLoginStatus(x));
                employeePlusResponseList.add(model);
            });
        }
        if (request.getId() > 0) {
            List<ExDepartmentEmployee> exDepartmentEmployeeList = exDepartmentEmployeeRepository.findAllByDepartmentIdAndInfoEmployeeSchoolEmployeeStatusAndDelActiveTrue(request.getId(), EmployeeConstant.STATUS_WORKING);
            exDepartmentEmployeeList.forEach(x -> {
                EmployeePlusResponse model = new EmployeePlusResponse();
                model.setId(x.getInfoEmployeeSchool().getId());
                model.setPhone(x.getInfoEmployeeSchool().getPhone());
                model.setEmployeeName(x.getInfoEmployeeSchool().getFullName());
                model.setAvartar(AvatarUtils.getAvatarInfoEmployee(x.getInfoEmployeeSchool()));
                if (x.getInfoEmployeeSchool().getExEmployeeClassList().size() > 0) {
                    model.setClassName(x.getInfoEmployeeSchool().getExEmployeeClassList().get(0).getMaClass().getClassName());
                } else {
                    model.setClassName("");
                }
                model.setLoginStatus(this.setLoginStatus(x.getInfoEmployeeSchool()));
                employeePlusResponseList.add(model);
            });
        }
        listEmployeePlusResponse.setDataList(employeePlusResponseList);
        return listEmployeePlusResponse;
    }

    @Override
    public EmployeePlusDetailResponse findEmployeeDetail(UserPrincipal principal, Long id) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findById(id).orElseThrow();
        EmployeePlusDetailResponse model = new EmployeePlusDetailResponse();
        model.setFullName(infoEmployeeSchool.getFullName());
        model.setAvatar(AvatarUtils.getAvatarInfoEmployee(infoEmployeeSchool));
        model.setBirthDay(ConvertData.convertDateString(infoEmployeeSchool.getBirthday()));
        model.setStartDate(ConvertData.convertDateString(infoEmployeeSchool.getStartDate()));
        model.setContractDate(ConvertData.convertDateString(infoEmployeeSchool.getContractDate()));
        if (infoEmployeeSchool.getEmployeeStatus().equals(AppConstant.EMPLOYEE_STATUS_WORKING)) {
            model.setKey(AppConstant.EMPLOYEE_WORK);
        }
        if (infoEmployeeSchool.getEmployeeStatus().equals(AppConstant.EMPLOYEE_STATUS_RETAIN)) {
            model.setKey(AppConstant.EMPLOYEE_RETAIN);
        }
        if (infoEmployeeSchool.getEmployeeStatus().equals(AppConstant.EMPLOYEE_STATUS_LEAVE)) {
            model.setKey(AppConstant.EMPLOYEE_LEAVE);
        }
        model.setAddress(infoEmployeeSchool.getAddress());
        model.setPhone(infoEmployeeSchool.getPhone());
        model.setEmail(infoEmployeeSchool.getEmail());
        model.setGender(infoEmployeeSchool.getGender());
        model.setEducationLevel(infoEmployeeSchool.getEducationLevel());

        List<String> departmentNameList = infoEmployeeSchool.getDepartmentEmployeeList().stream().map(ExDepartmentEmployee::getDepartment).collect(Collectors.toList()).stream().map(Department::getDepartmentName).collect(Collectors.toList());
        model.setDepartmentEmployeeList(departmentNameList);
        List<String> classNameList = infoEmployeeSchool.getExEmployeeClassList().stream().map(ExEmployeeClass::getMaClass).collect(Collectors.toList()).stream().map(MaClass::getClassName).collect(Collectors.toList());
        model.setEmployeeClassResponseList(classNameList);
        List<String> accountTypeList = infoEmployeeSchool.getAccountTypeList().stream().map(AccountType::getName).collect(Collectors.toList());
        model.setAccountTypeList(accountTypeList);
        return model;
    }

    private String setLoginStatus(InfoEmployeeSchool infoEmployeeSchool) {
        String loginStatus = null;
        if (infoEmployeeSchool.getEmployee() != null) {
            MaUser maUser = infoEmployeeSchool.getEmployee().getMaUser();
            List<Device> deviceList = maUser.getDeviceList();
            if (CollectionUtils.isEmpty(deviceList)) {
                loginStatus = AppConstant.LOGIN_YET;
            } else {
                long count = maUser.getDeviceList().stream().filter(Device::isLogin).count();
                loginStatus = count > 0 ? AppConstant.LOGIN_YES : AppConstant.LOGIN_NO;
            }
            return loginStatus;
        } else {
            loginStatus = AppConstant.LOGIN_YET;
        }
        return loginStatus;
    }
}
