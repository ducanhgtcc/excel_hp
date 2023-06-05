package com.example.onekids_project.service.servicecustom.finance.extend;

import com.example.onekids_project.request.common.ActiveRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendCreateRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.response.finance.PackageBriefResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendUpdateResponse;

import java.util.List;

/**
 * date 2021-10-01 16:12
 *
 * @author lavanviet
 */
public interface FnPackageExtendService {
    List<PackageBriefResponse> getPackageAdd(Long idSchool);
    List<PackageExtendResponse> getPackageExtend(Long idSchool, String name);
    PackageExtendUpdateResponse getPackageExtendById(Long idSchool, Long id);
    void createPackageExtend(Long idSchool, PackageExtendCreateRequest request);
    void updatePackageExtend(Long idSchool, PackageExtendUpdateRequest request);
    void deletePackageExtendById(Long idSchool, Long id);
    void activePackageExtendById(Long idSchool, ActiveRequest request);
}
