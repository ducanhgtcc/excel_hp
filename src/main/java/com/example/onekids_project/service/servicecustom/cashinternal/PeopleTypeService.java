package com.example.onekids_project.service.servicecustom.cashinternal;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.cashinternal.CreatePeopleTypeRequest;
import com.example.onekids_project.request.cashinternal.SeacrhCashInternalRequest;
import com.example.onekids_project.request.cashinternal.UpdatePeopleTypeRequest;
import com.example.onekids_project.response.caskinternal.CashInternalCreateResponse;
import com.example.onekids_project.response.caskinternal.ListPeopleTypeReponseResponse;
import com.example.onekids_project.response.caskinternal.PeopleTypeResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface PeopleTypeService {

    boolean createPeopleTypeSchool(School school);

    /**
     * @param principal
     * @param request
     * @return
     */
    ListPeopleTypeReponseResponse searchPeopleType(UserPrincipal principal, SeacrhCashInternalRequest request);

    /**
     * @param principal
     * @return
     */
    CashInternalCreateResponse searchPeopleInternal(UserPrincipal principal);

    /**
     * @param principal
     * @param id
     * @return
     */
    PeopleTypeResponse findDetailPeopleType(UserPrincipal principal, Long id);

    /**
     * @param principal
     * @param request
     * @return
     */
    boolean createPeopleType(UserPrincipal principal, CreatePeopleTypeRequest request);

    /**
     * @param principal
     * @param request
     * @return
     */
    boolean updatePeopleType(UserPrincipal principal, UpdatePeopleTypeRequest request);

    /**
     * @param principal
     * @param id
     * @return
     */
    boolean deletePeopleTypeById(UserPrincipal principal, Long id);

}
