package com.example.onekids_project.master.service;

import com.example.onekids_project.master.request.school.SchoolMasterCreateRequest;
import com.example.onekids_project.master.request.SchoolMasterSearchRequest;
import com.example.onekids_project.master.request.school.SchoolMasterUpdateRequest;
import com.example.onekids_project.master.response.school.ListSchoolMasterResponse;
import com.example.onekids_project.master.response.school.SchoolMasterForSchoolResponse;
import com.example.onekids_project.master.response.school.SchoolMasterResponse;
import com.example.onekids_project.master.response.UpdateAccountSchoolResponse;

import java.util.List;

public interface AccountSchoolService {
    /**
     * create school master
     * @param schoolMasterCreateRequest
     * @return
     */
    boolean createAccountSchool(SchoolMasterCreateRequest schoolMasterCreateRequest);

    /**
     * search account schoolmaster
     * @param schoolMasterSearchRequest
     * @return
     */
    ListSchoolMasterResponse getAllAccountSchool(SchoolMasterSearchRequest schoolMasterSearchRequest);

    /**
     * update schoolmaster
     * @param schoolMasterUpdateRequest
     * @return
     */
    SchoolMasterResponse updateAccountSchool(SchoolMasterUpdateRequest schoolMasterUpdateRequest);

    /**
     * delete school master
     * @param id
     * @return
     */
    boolean deleteSchoolMaster(Long id);

    /**
     * get account for school
     * @param idSchool
     * @return
     */
    List<SchoolMasterForSchoolResponse> getAccountSchoolByIdSchool(Long idSchool);
}
