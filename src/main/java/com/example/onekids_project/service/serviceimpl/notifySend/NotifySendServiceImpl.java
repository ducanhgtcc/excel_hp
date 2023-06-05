package com.example.onekids_project.service.serviceimpl.notifySend;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.plus.response.notify.NotifyPlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.notify.NotifySendService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifySendServiceImpl implements NotifySendService {

    @Override
    public NotifyPlusResponse createNotifyEmployee(UserPrincipal principal, List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        return null;
    }

    @Override
    public NotifyPlusResponse createNotifyStudent(UserPrincipal principal, List<Kids> kidsList) {
        return null;
    }
}
