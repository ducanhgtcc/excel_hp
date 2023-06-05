package com.example.onekids_project.master.service;

import com.example.onekids_project.master.request.admin.MaAdminCreateRequest;
import com.example.onekids_project.master.request.admin.MaAdminSearchRequest;
import com.example.onekids_project.master.request.admin.MaAdminUpdateRequest;
import com.example.onekids_project.master.response.admin.AdminForSchoolResponse;
import com.example.onekids_project.master.response.admin.MaAdminAccountResponse;
import com.example.onekids_project.master.response.admin.MaAdminResponse;
import com.example.onekids_project.master.response.admin.MaAdminSchoolResponse;
import com.example.onekids_project.request.base.IdObjectRequest;

import java.util.List;

public interface MaAdminService {
    /**
     * get all admin
     *
     * @return
     */
    List<MaAdminResponse> searchAllMaAdmin(MaAdminSearchRequest maAdminSearchRequest);

    /**
     * update admin
     *
     * @param maAdminUpdateRequest
     * @return
     */
    MaAdminResponse udpateAdmin(MaAdminUpdateRequest maAdminUpdateRequest);

    /**
     * create admin
     *
     * @param maAdminCreateRequest
     * @return
     */
    MaAdminResponse createAdmin(MaAdminCreateRequest maAdminCreateRequest);

    /**
     * delete admin
     *
     * @param id
     * @return
     */
    boolean deleteAdmin(Long id);

    /**
     * find account
     *
     * @return
     */
    List<MaAdminAccountResponse> findMaAdminAccount();

    /**
     * find school of all admin
     *
     * @param maAdminSearchRequest
     * @return
     */
    List<MaAdminSchoolResponse> searchAdminSchool(MaAdminSearchRequest maAdminSearchRequest);

    /**
     * find school for one admin
     *
     * @param idAdmin
     * @return
     */
    List<AdminForSchoolResponse> findSchoolOfAdmin(Long idAdmin);


    /**
     * update school for one admin
     *
     * @param idAdmin
     * @param idObjectRequestList
     * @return
     */
    boolean updateSchoolOfAdmin(Long idAdmin, List<IdObjectRequest> idObjectRequestList);

}
