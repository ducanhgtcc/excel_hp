package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.VaccinateParentRequest;
import com.example.onekids_project.mobile.parent.request.kids.KidsExtraInforRequest;
import com.example.onekids_project.mobile.parent.response.kids.KidsExtraInforResponse;
import com.example.onekids_project.mobile.parent.response.kids.VaccinateParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface VaccinateParentService {
    /**
     * get vaccinate
     *
     * @param principal
     * @return
     */
    List<VaccinateParentResponse> finVaccinateList(UserPrincipal principal);

    /**
     * save vaccinate
     * @param principal
     * @param vaccinateParentRequest
     * @return
     */
    VaccinateParentResponse saveVaccinate(UserPrincipal principal, VaccinateParentRequest vaccinateParentRequest);




}
