package com.example.onekids_project.util;

import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.enums.EmployeeStatusEnum;
import com.example.onekids_project.mobile.plus.response.home.SchoolPlusResponse;
import com.example.onekids_project.response.employee.EmployeeStatusResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeUtil {
    /**
     * lấy các trạng thái nhân viên
     *
     * @return
     */
    public static List<EmployeeStatusResponse> getEmployeeStatus() {
        List<EmployeeStatusEnum> employeeStatusEnumList = Arrays.asList(EmployeeStatusEnum.values());
        List<EmployeeStatusResponse> employeeStatusResponseList = new ArrayList<>();
        employeeStatusEnumList.forEach(e -> {
            EmployeeStatusResponse employeeStatusResponse = new EmployeeStatusResponse();
            employeeStatusResponse.setKey(e);
            employeeStatusResponse.setValue(e.getValue());
            employeeStatusResponseList.add(employeeStatusResponse);
        });
        return employeeStatusResponseList;
    }

    public static InfoEmployeeSchool convertEmployeeToInfoEmployeeSchool(Long idSchool, Employee employee) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(idSchool) && x.isDelActive() && x.isActivated()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_EMPLOYEE);
        }
        return infoEmployeeSchoolList.get(0);
    }

    public static InfoEmployeeSchool convertEmployeeToInfoEmployeeSchoolDeleteAble(Long idSchool, Employee employee) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(idSchool) && x.isDelActive()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(infoEmployeeSchoolList)) {
            return infoEmployeeSchoolList.get(0);
        }
        List<InfoEmployeeSchool> infoEmployeeSchoolDeleteList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(idSchool)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoEmployeeSchoolDeleteList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_EMPLOYEE);
        }
        return infoEmployeeSchoolDeleteList.get(0);
    }

    public static List<InfoEmployeeSchool> getInfoEmployeeList(Employee employee) {
        return employee.getInfoEmployeeSchoolList().stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
    }

    public static List<Long> getIdClassFromEmployee(Employee employee) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = getInfoEmployeeList(employee);
        List<MaClass> maClassList = new ArrayList<>();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
            List<MaClass> maClassLocalList = x.getExEmployeeClassList().stream().map(ExEmployeeClass::getMaClass).collect(Collectors.toList()).stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
            maClassList.addAll(maClassLocalList);
        }
        maClassList = maClassList.stream().filter(FilterDataUtils.distinctBy(MaClass::getId)).collect(Collectors.toList());
        return maClassList.stream().map(BaseEntity::getId).collect(Collectors.toList());
    }

    public static List<MaClass> getClassFromEmployee(Employee employee) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = getInfoEmployeeList(employee);
        List<MaClass> maClassList = new ArrayList<>();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
            List<MaClass> maClassLocalList = x.getExEmployeeClassList().stream().map(ExEmployeeClass::getMaClass).collect(Collectors.toList()).stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
            maClassList.addAll(maClassLocalList);
        }
        maClassList = maClassList.stream().filter(FilterDataUtils.distinctBy(MaClass::getId)).collect(Collectors.toList());
        return maClassList;
    }

    public static List<MaClass> getClassFromInfoEmployee(InfoEmployeeSchool infoEmployeeSchool) {
        List<MaClass> maClassLocalList = infoEmployeeSchool.getExEmployeeClassList().stream().map(ExEmployeeClass::getMaClass).collect(Collectors.toList()).stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        maClassLocalList = maClassLocalList.stream().filter(FilterDataUtils.distinctBy(MaClass::getId)).collect(Collectors.toList());
        return maClassLocalList;
    }


    public static MaClass getMaClassFromEmployee(Employee employee) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = getInfoEmployeeList(employee);
        List<MaClass> maClassList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x -> {
            List<MaClass> maClassLocalList = x.getExEmployeeClassList().stream().map(ExEmployeeClass::getMaClass).collect(Collectors.toList()).stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
            maClassList.addAll(maClassLocalList);
        });
        return CollectionUtils.isEmpty(maClassList) ? null : maClassList.get(0);
    }

    public static List<SchoolPlusResponse> getSchoolList(Employee employee) {
        List<SchoolPlusResponse> dataList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.isActivated() && x.isDelActive()).collect(Collectors.toList());
        infoEmployeeSchoolList.forEach(x -> {
            SchoolPlusResponse model = new SchoolPlusResponse();
            model.setId(x.getSchool().getId());
            model.setPlusName(x.getFullName());
            model.setSchoolName(x.getSchool().getSchoolName());
            model.setAvatar(AvatarUtils.getAvatarInfoEmployee(x));
            dataList.add(model);
        });
        return dataList;
    }
}
