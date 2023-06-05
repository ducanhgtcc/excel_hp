package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.kidsQuality.CreateKidsHeightWeightPlusRequest;
import com.example.onekids_project.mobile.plus.request.kidsQuality.SearchKidsQualityPlusRequest;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.plus.response.kidsQuality.KidsHeightWeightPlusResponse;
import com.example.onekids_project.mobile.plus.response.kidsQuality.ListKidsQualityPlusResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsExtraQualityResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListHeightWeightSampleTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListQualityKidTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface KidsQualityPlusService {

    ListKidsQualityPlusResponse findQualityKid(UserPrincipal principal, SearchKidsQualityPlusRequest request);

    KidsExtraQualityResponse findKidsExtraQuality(UserPrincipal principal, Long id);

    boolean deleteHeightWeight(Long idHeight, Long idWeight);

    KidsHeightWeightPlusResponse saveHeightWeightPlus(UserPrincipal principal, CreateKidsHeightWeightPlusRequest request);

    ListHeightWeightSampleTeacherResponse findKidSamplePlus(Long id);
}
