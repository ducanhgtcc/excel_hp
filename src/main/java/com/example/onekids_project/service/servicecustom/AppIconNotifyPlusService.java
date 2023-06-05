package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.request.kids.AppIconNotifyPlusRequest;
import com.example.onekids_project.response.school.ListAppIconPlusNotifyResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppIconNotifyPlusService {
    boolean createAppIconNotifyPlusAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconNotifyPlusRequest> appIconNotifyPlusRequests);

    ListAppIconPlusNotifyResponse findAppIconNotifyPlusAdd(Long idSchoolLogin, Long id);


    void updateAppIconNotifyPlusAdd(Long idSchool, InfoEmployeeSchool newInfoEmployeeSchool, List<AppIconNotifyPlusRequest> notifyAppIconPlusRequestList);
}
