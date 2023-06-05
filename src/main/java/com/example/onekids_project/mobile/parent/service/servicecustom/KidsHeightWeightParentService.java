package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.kids.KidsHeightWeightParentRequest;
import com.example.onekids_project.mobile.parent.response.kids.KidsHeightWeightParentResponse;
import com.example.onekids_project.mobile.parent.response.kids.ListHeightWeightSampleParentResponse;
import com.example.onekids_project.mobile.parent.response.kids.ListKidsHeightWeightParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface KidsHeightWeightParentService {
    ListHeightWeightSampleParentResponse findHeightWeightSampleParent(Long idKid);

    KidsHeightWeightParentResponse saveHeightWeightParent(UserPrincipal principal, KidsHeightWeightParentRequest kidsHeightWeightParentRequest);

    ListKidsHeightWeightParentResponse findHeightWeight(UserPrincipal principal, LocalDate localDate, Pageable pageable);

    boolean deleteHeightWeight(Long idHeight, Long idWeight);
}
