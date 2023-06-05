package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.usermaster.SchoolMaster;
import com.example.onekids_project.master.request.SchoolMasterSearchRequest;

import java.util.List;

public interface SchoolMasterRepositoryCustom {
    /**
     * search all school master
     * @param schoolMasterSearchRequest
     * @return
     */
    List<SchoolMaster> findAllSchoolMaster(SchoolMasterSearchRequest schoolMasterSearchRequest, List<Long> idSchoolList);

    long countSchoolMaster(SchoolMasterSearchRequest schoolMasterSearchRequest, List<Long> idSchoolList);
}
