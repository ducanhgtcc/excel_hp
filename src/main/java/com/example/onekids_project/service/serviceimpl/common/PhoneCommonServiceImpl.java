package com.example.onekids_project.service.serviceimpl.common;

import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.response.phone.AccountLoginResponse;
import com.example.onekids_project.response.phone.PhoneResponse;
import com.example.onekids_project.service.servicecustom.common.PhoneCommonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhoneCommonServiceImpl implements PhoneCommonService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PhoneResponse> findPhoneParent(List<Kids> kidsList) {
        kidsList = kidsList.stream().filter(x -> (x.getParent() != null && x.isSmsReceive())).collect(Collectors.toList());
        List<Parent> parentList = kidsList.stream().map(Kids::getParent).collect(Collectors.toList());
        List<Parent> parentFilterList = List.copyOf(Set.copyOf(parentList));
        List<MaUser> maUserList = parentFilterList.stream().map(Parent::getMaUser).collect(Collectors.toList());
        List<PhoneResponse> dataList = listMapper.mapList(maUserList, PhoneResponse.class);
        dataList = dataList.stream().filter(x -> x.getPhone().length() == 10).collect(Collectors.toList());
        return dataList;
    }

    @Override
    public List<PhoneResponse> findPhoneEmpolyess(List<Employee> employeeList) {
        List<MaUser> maUserList = employeeList.stream().map(Employee::getMaUser).collect(Collectors.toList());
        maUserList = List.copyOf(Set.copyOf(maUserList));
        List<PhoneResponse> dataList = listMapper.mapList(maUserList, PhoneResponse.class);
        return dataList;
    }

    @Override
    public List<PhoneResponse> findPhoneWithInfo(List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        List<MaUser> maUserList = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee() != null).map(x -> x.getEmployee().getMaUser()).distinct().collect(Collectors.toList());
        return listMapper.mapList(maUserList, PhoneResponse.class);
    }


    @Override
    public List<AccountLoginResponse> findAccountParent(List<Kids> kidsList) {

        kidsList = kidsList.stream().filter(x -> (x.getParent() != null && x.isSmsReceive())).collect(Collectors.toList());
        List<Parent> parentList = kidsList.stream().map(Kids::getParent).collect(Collectors.toList());
        List<Parent> parentFilterList = List.copyOf(Set.copyOf(parentList));
        List<MaUser> maUserList = parentFilterList.stream().map(Parent::getMaUser).collect(Collectors.toList());
        List<AccountLoginResponse> dataList = listMapper.mapList(maUserList, AccountLoginResponse.class);
        return dataList;
    }

    @Override
    public AccountLoginResponse findAccountOneParent(Kids kids) {
        if (kids.getParent() != null && kids.isSmsReceive()) {
            AccountLoginResponse response = modelMapper.map(kids.getParent().getMaUser(), AccountLoginResponse.class);
            return response;
        }
        return new AccountLoginResponse();
    }

    @Override
    public List<AccountLoginResponse> findAccountEmployess(List<Employee> employeeList) {
        List<MaUser> maUserList = employeeList.stream().map(Employee::getMaUser).collect(Collectors.toList());
        maUserList = List.copyOf(Set.copyOf(maUserList));
        List<AccountLoginResponse> dataList = listMapper.mapList(maUserList, AccountLoginResponse.class);
        return dataList;
    }

    @Override
    public List<AccountLoginResponse> findAccountEmployessWithInfo(List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        List<MaUser> maUserList = infoEmployeeSchoolList.stream().map(x -> x.getEmployee().getMaUser()).collect(Collectors.toList());
        maUserList = List.copyOf(Set.copyOf(maUserList));
        return listMapper.mapList(maUserList, AccountLoginResponse.class);
    }
}
