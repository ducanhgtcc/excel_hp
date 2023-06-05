package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.employee.EmployeeNotify;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.repository.NotifyPlusSchoolRepository;
import com.example.onekids_project.request.kids.AppIconNotifyPlusRequest;
import com.example.onekids_project.response.school.AppIconPlusNotifyResponse;
import com.example.onekids_project.response.school.ListAppIconPlusNotifyResponse;
import com.example.onekids_project.service.servicecustom.AppIconNotifyPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AppIconNotifyPlusServiceImpl implements AppIconNotifyPlusService {

    @Autowired
    private NotifyPlusSchoolRepository notifyPlusSchoolRepository;

    @Override
    public boolean createAppIconNotifyPlusAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconNotifyPlusRequest> appIconNotifyPlusRequests) {
        EmployeeNotify employeeNotify = new EmployeeNotify();
        appIconNotifyPlusRequests.forEach(x -> {
            if (x.getIconName().equalsIgnoreCase(AppIconName.FEEDBACK)) {
                employeeNotify.setFeedback(x.isStatus());
            } else if (x.getIconName().equalsIgnoreCase(AppIconName.MEDICINE)) {
                employeeNotify.setMedicine(x.isStatus());
            } else if (x.getIconName().equalsIgnoreCase(AppIconName.ABSENT)) {
                employeeNotify.setAbsent(x.isStatus());
            } else if (x.getIconName().equalsIgnoreCase(AppIconName.MESSAGE)) {
                employeeNotify.setMessage(x.isStatus());
            }
            employeeNotify.setSys(AppConstant.APP_TRUE);
        });
        employeeNotify.setInfoEmployeeSchool(infoEmployeeSchool);
        notifyPlusSchoolRepository.save(employeeNotify);
        return true;
    }

    @Override
    public ListAppIconPlusNotifyResponse findAppIconNotifyPlusAdd(Long idSchoolLogin, Long id) {
        Optional<EmployeeNotify> employeeNotifyFind = notifyPlusSchoolRepository.findAppIconNotifyPlus(id);
        ListAppIconPlusNotifyResponse listAppIconPlusNotifyResponse = new ListAppIconPlusNotifyResponse();
        if (employeeNotifyFind.isPresent()) {
            EmployeeNotify employeeNotify = employeeNotifyFind.get();
            if (employeeNotify.isMessage()) {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
                appIconPlusNotifyResponse.setIconName(AppIconName.MESSAGE);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            } else {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_FALSE);
                appIconPlusNotifyResponse.setIconName(AppIconName.MESSAGE);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            }
            if (employeeNotify.isMedicine()) {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
                appIconPlusNotifyResponse.setIconName(AppIconName.MEDICINE);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            } else {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_FALSE);
                appIconPlusNotifyResponse.setIconName(AppIconName.MEDICINE);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            }
            if (employeeNotify.isAbsent()) {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
                appIconPlusNotifyResponse.setIconName(AppIconName.ABSENT);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            } else {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_FALSE);
                appIconPlusNotifyResponse.setIconName(AppIconName.ABSENT);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            }
            if (employeeNotify.isFeedback()) {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
                appIconPlusNotifyResponse.setIconName(AppIconName.FEEDBACK);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            } else {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_FALSE);
                appIconPlusNotifyResponse.setIconName(AppIconName.FEEDBACK);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            }
            if (employeeNotify.isSys()) {
                AppIconPlusNotifyResponse appIconPlusNotifyResponse = new AppIconPlusNotifyResponse();
                appIconPlusNotifyResponse.setStatus(AppConstant.APP_TRUE);
                appIconPlusNotifyResponse.setIconName(AppIconName.ONE_KID);
                listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().add(appIconPlusNotifyResponse);
            }

            List<AppIconPlusNotifyResponse> listAppIconPlusNotifyResponses = listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList();
            if (CollectionUtils.isEmpty(listAppIconPlusNotifyResponses)) {
                return null;
            }
            if (listAppIconPlusNotifyResponses.size() <= 10) {
                listAppIconPlusNotifyResponse.setAppIconPlusNotifyResponseList1(listAppIconPlusNotifyResponses);
            } else {
                listAppIconPlusNotifyResponse.setAppIconPlusNotifyResponseList1(listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList().subList(0, 10));
                listAppIconPlusNotifyResponse.setAppIconPlusNotifyResponseList2(listAppIconPlusNotifyResponse.getAppIconPlusNotifyResponseList2().subList(10, listAppIconPlusNotifyResponses.size()));
            }
        }


        return listAppIconPlusNotifyResponse;
    }

    @Override
    public void updateAppIconNotifyPlusAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconNotifyPlusRequest> appIconNotifyPlusRequests) {
        Optional<EmployeeNotify> employeeNotifyOld = notifyPlusSchoolRepository.findAppIconNotifyPlus(infoEmployeeSchool.getId());
        if (employeeNotifyOld.isPresent()) {
            EmployeeNotify employeeNotify = employeeNotifyOld.get();
            appIconNotifyPlusRequests.forEach(x -> {
                if (x.getIconName().equalsIgnoreCase(AppIconName.FEEDBACK)) {
                    employeeNotify.setFeedback(x.isStatus());
                } else if (x.getIconName().equalsIgnoreCase(AppIconName.MEDICINE)) {
                    employeeNotify.setMedicine(x.isStatus());

                } else if (x.getIconName().equalsIgnoreCase(AppIconName.ABSENT)) {
                    employeeNotify.setAbsent(x.isStatus());

                } else if (x.getIconName().equalsIgnoreCase(AppIconName.MESSAGE)) {
                    employeeNotify.setMessage(x.isStatus());
                }
            });
            notifyPlusSchoolRepository.save(employeeNotify);
        }
    }
}
