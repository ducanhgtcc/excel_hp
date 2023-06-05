package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.mobile.plus.response.salary.EmployeeSalaryPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.SalaryPlusService;
import com.example.onekids_project.repository.FnOrderEmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.EmployeeSalaryService;
import com.example.onekids_project.util.AvatarUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-06-08 14:38
 *
 * @author lavanviet
 */
@Service
public class SalaryPlusServiceImpl implements SalaryPlusService {
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;
    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Override
    public List<EmployeeSalaryPlusResponse> getSalaryEmployee(UserPrincipal principal, LocalDate date) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolIdAndAppTypeAndEmployeeStatusAndDelActiveTrue(principal.getIdSchoolLogin(), AppTypeConstant.TEACHER, EmployeeConstant.STATUS_WORKING);
        List<EmployeeSalaryPlusResponse> responseList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x -> {
            EmployeeSalaryPlusResponse model = new EmployeeSalaryPlusResponse();
            model.setId(x.getId());
            model.setFullName(x.getFullName());
            model.setAvatar(AvatarUtils.getAvatarInfoEmployee(x));
            Optional<FnOrderEmployee> fnOrderEmployeeOptional = fnOrderEmployeeRepository.findByInfoEmployeeSchoolIdAndMonthAndYear(x.getId(), date.getMonthValue(), date.getYear());
            if (fnOrderEmployeeOptional.isPresent()) {
                model.setIdOrder(fnOrderEmployeeOptional.get().isLocked() ? null : fnOrderEmployeeOptional.get().getId());
                model.setOrderShow(fnOrderEmployeeOptional.get().isView());
            }
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public void setOrderShow(UserPrincipal principal, List<StatusRequest> request) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        for (StatusRequest x : request) {
            StatusListRequest model = new StatusListRequest();
            model.setIdList(Collections.singletonList(x.getId()));
            model.setStatus(x.getStatus());
            employeeSalaryService.viewBill(principal, model);
        }
    }
}
