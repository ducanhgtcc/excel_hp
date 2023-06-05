package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.AppSendDTO;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.master.request.CreateAppSendNotify;
import com.example.onekids_project.master.request.SearchAppSendRequest;
import com.example.onekids_project.master.request.UpdateAppSendNotify;
import com.example.onekids_project.master.response.notify.ListNotifyAdminResponse;
import com.example.onekids_project.mobile.teacher.response.message.MessageTeacheConfirmResponse;
import com.example.onekids_project.request.AppSend.*;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ListAppSendResponse;
import com.example.onekids_project.response.notifihistory.ReiceiversResponeHistoru;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//import org.springframework.data.domain.Pageable;

public interface AppSendService {

    Optional<AppSendDTO> findByIdNotifi(Long idSchoolLogin, Long idPrincipal, Long id);

    ListAppSendResponse searchNotifi(Long idSchoolLogin, UserPrincipal principal, SearchContentRequest request);

    ListAppSendResponse findAllNotif(Long idSchool, Pageable pageable, Long id, String sendType);

    AppSendResponse updateKidsBirthdayAppsend(Long idSchoolLogin, UserPrincipal principal, UpdateKidsBirthdayRequest kidsBirthdayEditRequest);

    boolean deleteAppsend(UserPrincipal principal, Long idSchoolLogin, Long id);

    boolean createAppSendNotify(UserPrincipal principal, CreateAppSendNotify createAppSendNotify) throws IOException, FirebaseMessagingException;

    boolean updateAppSendNotify(UserPrincipal principal, UpdateAppSendNotify updateAppSendNotify) throws IOException;

    ListNotifyAdminResponse findAllAppSendNotify(SearchAppSendRequest searchAppSendRequest);

    boolean deleteAppSendNotify(Long[] idAppSend);

    ReceiversRequest updateReadReceiver(Long id, List<ReceiversRequest> receiversRequests);

    boolean createAppsendParent(UserPrincipal principal, CreateSendParentBirthdayRequest createSendParentBirthdayRequest) throws FirebaseMessagingException;

    List<ReiceiversResponeHistoru> findByIdAppsend(UserPrincipal principal, Long idSchoolLogin, Long id);

    AppSendResponse createAppsendParentBirthday(UserPrincipal principal, CreateParentRealBirthdayRequest createParentRealBirthdayRequest) throws FirebaseMessagingException;

    AppSendResponse createAppsendTeacherBirthday(UserPrincipal principal, CreateParentRealBirthdayRequest createParentRealBirthdayRequest) throws FirebaseMessagingException;

    boolean updateRead(Long id, List<AppSendRequest> appSendRequests);

    Optional<AppSendDTO> findByIdAppsenda(Long idSchoolLogin, UserPrincipal principal, Long id);

    AppSendResponse updateHistoryAppSend(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppHistoryRequest updateSmsAppHistoryRequest);

    MessageTeacheConfirmResponse appovedHistoryAppsend(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    MessageTeacheConfirmResponse revokeAppSendhistory(UserPrincipal principal, Long id);

    MessageTeacheConfirmResponse unrevokeAppSendhistory(UserPrincipal principal, Long id);

    SmsAppRequest1 updateManyapproved(Long id, List<SmsAppRequest1> smsAppRequests);

    void saveToAppSendParent(UserPrincipal principal, Kids kids, String title, String content, String sendType);

    void saveToAppSendParentForAuto(Long idSchool, Kids kids, String title, String content, String sendType, String... picture);

    void saveToAppSendEmployee(UserPrincipal principal, InfoEmployeeSchool infoEmployeeSchool, String title, String content, String sendType);

    void saveToAppSendEmployeeForAuto(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, String title, String content, String sendType, String... picture);

}
