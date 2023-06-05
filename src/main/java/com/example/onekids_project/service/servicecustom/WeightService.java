package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.kidsheightweight.CreateKidsHeightWeightRequest;
import com.example.onekids_project.response.kidsheightweight.KidsWeightResponse;
import com.example.onekids_project.response.kidsheightweight.ListWeightSampleResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

public interface WeightService {

    KidsWeightResponse createKidsWeight(Long idSchool, UserPrincipal principal, CreateKidsHeightWeightRequest createKidsHeightWeightRequest);

    boolean deleteKidsWeight(Long idSchool, Long id);

    ListWeightSampleResponse findWeightSample(Long idSchoolLogin, Pageable pageable, Long id);
}
