package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.notifihistory.*;
import com.example.onekids_project.request.smsNotify.VerifyCodeRequest;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ListAppSendNewResponse;
import com.example.onekids_project.response.notifihistory.ListSmsSendResponse;
import com.example.onekids_project.response.notifihistory.ReiceiverSmsSchedule;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;
import java.util.concurrent.ExecutionException;

//import org.springframework.data.domain.Pageable;

public interface SmsSendService {

    ListSmsSendResponse searchSmsSend(UserPrincipal principal, SearchSmsSendRequest searchSmsSendRequest);

    boolean deleteSmsSend(Long id);

    AppSendResponse updateSmsApp(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppRequest smsAppRequestEditRequest);

    AppSendResponse updateSmsAppforrevoke(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppRevokeRequest updateSmsAppRevokeRequest);

    boolean deleteApp(Long id);

    AppSendResponse updateSmsAppforrevokeun(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppRevokeRequest updateSmsAppRevokeRequest);

    SmsNotifiRequest updateApproved(Long id, List<SmsNotifiRequest> smsNotifiRespone);

    /*
     * tìm kiếm tin nhắn hẹn giờ trong DB
     * */
    boolean findSendSms();

    List<ReiceiverSmsSchedule> findByIdSmsss(Long idSchoolLogin, Long id);

    SmsNotifiRequest updateManyApproved(Long id, UserPrincipal principal, List<SmsNotifiRequest> smsNotifiRequests);

    boolean createVerifyAccountKid(UserPrincipal principal, VerifyCodeRequest verifyCodeRequest) throws ExecutionException, InterruptedException;

    void sendAutoSmsBirthday();

    ListAppSendNewResponse searchSmsAppSysNew(UserPrincipal principal, SearchSmsAppRequest request);

    ListAppSendNewResponse searchSmsAppTeacherNew(UserPrincipal principal, SearchSmsAppRequest request);

    boolean sendStudentSms(UserPrincipal principal, List<Kids> kidsList, String content) throws ExecutionException, InterruptedException;

    boolean sendEmployeeSms(UserPrincipal principal, List<InfoEmployeeSchool> infoEmployeeSchoolList, String content) throws ExecutionException, InterruptedException;
}
