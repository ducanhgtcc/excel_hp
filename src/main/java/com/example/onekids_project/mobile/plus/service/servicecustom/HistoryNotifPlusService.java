package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.*;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.*;
import com.example.onekids_project.security.model.UserPrincipal;

public interface HistoryNotifPlusService {


    ListHistoryNotifiPlusResponse searchHistoryNotifi(UserPrincipal principal, SearchHistoryNotifiPlusRequest request);

    boolean deleteNotifi(Long id);

    ListSmsPlusResponse searchSmsPlus(UserPrincipal principal, SearchSmsPlusRequest request);

    DetailHistoryNotifiAppResponse findDeTailNotifi(UserPrincipal principal, Long id);

    ListViewDetailUserNotifiPlusResponse viewDetailNotifiUserPlus(UserPrincipal principal, Long id,DetailUserRequest request);

    boolean approveMultiNotifi(UserPrincipal principal, ApproveNotifiRequest request);

    boolean revokeMultiNotifi(UserPrincipal principal, DeleteNotifiRequest request);

    DetailSmsResponse findDeTailSms(UserPrincipal principal, Long id, DetailSmsRequest request);

    boolean deletenotifi(UserPrincipal principal,DeleteNotifiRequest request);

    DetailSmsCustomResponse findDeTailSmsCustom(UserPrincipal principal, Long id);
}
