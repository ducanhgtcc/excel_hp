package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.request.historynotifi.SearchHistoryNotifiTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.historynotifi.HistoryNotifiTeacherDetailResponse;
import com.example.onekids_project.mobile.teacher.response.historynotifi.ListHistoryNotifiTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.historynotifi.ListViewDetailUserNotifiTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface HistoryNotifiTeacherMobileService {

    ListHistoryNotifiTeacherResponse searchHistoryNotifi(UserPrincipal principal, SearchHistoryNotifiTeacherRequest searchHistoryNotifiTeacherRequest);

    boolean historyTeacherReovke(UserPrincipal principal, Long id);

    HistoryNotifiTeacherDetailResponse viewDetailNotifi(UserPrincipal principal, Long id);

    ListViewDetailUserNotifiTeacherResponse viewDetailNotifiUser(UserPrincipal principal, Long id);

    boolean revokeUserNotifi(UserPrincipal principal, Long id);
}
