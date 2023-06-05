package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.usermaster.MaAdmin;
import com.example.onekids_project.master.request.admin.MaAdminSearchRequest;

import java.util.List;

public interface MaAdminRepositoryCustom {
    /**
     * search maadmin
     * @param maAdminSearchRequest
     * @return
     */
    List<MaAdmin> searchMaAdmin(MaAdminSearchRequest maAdminSearchRequest);
}
