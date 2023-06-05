package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.kidsheightweight.CreateKidsHeightWeightRequest;
import com.example.onekids_project.response.kidsheightweight.ListHeightSampleResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

public interface HeightService {

    boolean createKidsHeight(Long idSchool, UserPrincipal principal, CreateKidsHeightWeightRequest createKidsHeightWeightRequest);

    boolean deleteKidsHeight(Long idSchoolLogin, Long id);

    ListHeightSampleResponse findHeightSample(Long idSchoolLogin, Pageable pageable, Long id);
}
