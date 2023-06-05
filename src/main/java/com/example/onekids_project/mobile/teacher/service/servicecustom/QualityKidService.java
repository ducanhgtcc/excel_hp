package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.request.qualitykid.KidsHeightWeightTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsExtraQualityResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsHeightWeightTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListHeightWeightSampleTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListQualityKidTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface QualityKidService {

    ListQualityKidTeacherResponse findQualityKidOfClass(UserPrincipal principal);


    KidsExtraQualityResponse findKidsExtraQuality(UserPrincipal principal, Long id);

    boolean deleteHeightWeight(Long idHeight, Long idWeight);

    KidsHeightWeightTeacherResponse saveHeightWeightTeacher(UserPrincipal principal, KidsHeightWeightTeacherRequest kidsHeightWeightParentRequest);

    ListHeightWeightSampleTeacherResponse findKidSample(Long id);
}
