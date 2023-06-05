package com.example.onekids_project.service.servicecustom.notify;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.plus.response.notify.NotifyPlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface NotifySendService {

    NotifyPlusResponse createNotifyEmployee(UserPrincipal principal, List<InfoEmployeeSchool> infoEmployeeSchoolList);

    NotifyPlusResponse createNotifyStudent(UserPrincipal principal, List<Kids> kidsList);
}
