package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.kidsQuality.CreateKidsHeightWeightPlusRequest;
import com.example.onekids_project.mobile.plus.request.kidsQuality.SearchKidsQualityPlusRequest;
import com.example.onekids_project.mobile.plus.request.video.SearchVideoPlusRequest;
import com.example.onekids_project.mobile.plus.response.kidsQuality.KidsHeightWeightPlusResponse;
import com.example.onekids_project.mobile.plus.response.kidsQuality.ListKidsQualityPlusResponse;
import com.example.onekids_project.mobile.plus.response.video.*;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsExtraQualityResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListHeightWeightSampleTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface VideoPlusService {


    ListVideoPlusResponse findVideoPlus(UserPrincipal principal, SearchVideoPlusRequest request);

    ListCameraPlusResponse searchCameraPlus(UserPrincipal principal);

    ListCameraClassDetailResponse findDeTailCameraClass(UserPrincipal principal, Long id);

    ListCameraClassDetailResponse findAllCamera(UserPrincipal principal);
}
