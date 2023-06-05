package com.example.onekids_project.service.servicecustom;

//import org.springframework.data.domain.Pageable;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mobile.teacher.response.historynotifi.SmsCustomResponse;
import com.example.onekids_project.request.notifihistory.SearchSmsSendCustomRequest;
import com.example.onekids_project.request.smsNotify.SmsCustomRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.notifihistory.ListSmsCustomResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SmsSendCustomService {

    ListSmsCustomResponse searchSmsCustom(UserPrincipal principal, SearchSmsSendCustomRequest request);

    List<SmsCustomResponse> findDetailHistory(UserPrincipal principal, Long id);

    List<SmsCustomResponse> findDetailHistoryFail(UserPrincipal principal, Long id);

    List<SmsCustomResponse> findDetailHistoryAll(UserPrincipal principal, Long id);

    boolean sendAccountStudentSms(UserPrincipal principal, List<Kids> kidsList) throws ExecutionException, InterruptedException;

    boolean sendAccountOneStudentSms(UserPrincipal principal, Kids kids) throws ExecutionException, InterruptedException;

    boolean sendAccountEmployeeSms(UserPrincipal principal, List<InfoEmployeeSchool> infoEmployeeSchoolList) throws ExecutionException, InterruptedException;

    void sendSmsCustom(UserPrincipal principal, SmsCustomRequest request) throws ExecutionException, InterruptedException;
    void sendFirebaseCustom(UserPrincipal principal, SmsCustomRequest request) throws ExecutionException, InterruptedException, FirebaseMessagingException;
}
