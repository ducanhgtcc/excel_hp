package com.example.onekids_project.service.servicecustom.common;

import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.response.phone.AccountLoginResponse;
import com.example.onekids_project.response.phone.PhoneResponse;

import java.util.List;

public interface PhoneCommonService {
    List<PhoneResponse> findPhoneParent(List<Kids> kidsList);

    List<PhoneResponse> findPhoneEmpolyess(List<Employee> employeeList);

    List<PhoneResponse> findPhoneWithInfo(List<InfoEmployeeSchool> infoEmployeeSchoolList);

    List<AccountLoginResponse> findAccountParent(List<Kids> kidsList);

    AccountLoginResponse findAccountOneParent(Kids kids);

    List<AccountLoginResponse> findAccountEmployess(List<Employee> employeeList);

    List<AccountLoginResponse> findAccountEmployessWithInfo(List<InfoEmployeeSchool> infoEmployeeSchoolList);


}
