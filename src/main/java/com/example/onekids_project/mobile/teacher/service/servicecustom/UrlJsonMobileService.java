package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.GetLinkPlusRequest;
import com.example.onekids_project.mobile.teacher.request.getson.GetJsonTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.jsonattendance.ListUrlJsonResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;

public interface UrlJsonMobileService {

    ListUrlJsonResponse searchUrlJson1(UserPrincipal principal, GetJsonTeacherRequest getJsonTeacherRequest) throws IOException;

    ListUrlJsonResponse searchUrlForPlus(UserPrincipal principal, GetLinkPlusRequest request) throws IOException;
}
