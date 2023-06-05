package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.master.service.CommonMasterService;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lavanviet
 */
@Service
public class CommonMasterServiceImpl implements CommonMasterService {
    @Autowired
    private MaUserService maUserService;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Override
    public void updatePasswordManyKids(List<Long> idList, String newPassword) {
        List<Kids> kidsList = kidsRepository.findByIdInAndParentIsNotNull(idList);
        List<MaUser> maUserList = kidsList.stream().map(x -> x.getParent().getMaUser()).distinct().collect(Collectors.toList());
        maUserService.updatePasswordMany(maUserList, newPassword);
    }

    @Override
    public void updatePasswordManyEmployee(List<Long> idList, String newPassword) {
        List<InfoEmployeeSchool> list = infoEmployeeSchoolRepository.findByIdInAndEmployeeIsNotNull(idList);
        List<MaUser> maUserList = list.stream().map(x -> x.getEmployee().getMaUser()).distinct().collect(Collectors.toList());
        maUserService.updatePasswordMany(maUserList, newPassword);
    }
}
