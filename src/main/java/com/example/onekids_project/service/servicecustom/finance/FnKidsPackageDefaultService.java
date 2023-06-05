package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.finance.*;
import com.example.onekids_project.response.finance.*;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface FnKidsPackageDefaultService {
    void addPackageForKids(Long idClass, Long idPackage);

    void createPackageDefaultForKids(Kids kids);

    void deletePackageForKids(Long idClass, Long idPackage);

    boolean updateKidsPackageDefaultOne(UserPrincipal principal, KidsPackageDefaultRequest request);

    boolean updateKidsPackageDefaultMany(UserPrincipal principal, List<KidsPackageDefaultRequest> requestList);

    List<PackageResponse> getPackageForDefaultPackageAdd(UserPrincipal principal, Long idKid);

    boolean createPackageDefault(UserPrincipal principal, Long idKid, PackageDefaultCreateRequest request);

    List<PackageDefaultDetailResponse> getPackageDefaultKid(UserPrincipal principal, Long idKid);

    boolean activePackageDefaultKid(UserPrincipal principal, PackageDefaultActiveRequest requestList);

    boolean deletePackageDefaultKid(UserPrincipal principal, Long idPackageDefault);

    void updatePackageDefaultForChangeClass(Kids kids);

    List<PackageInClassResponse> searchKidsPackageDefaultInClass(UserPrincipal principal, Long idClass);

    ListKidsPackageDefaultResponse searchKidsPackageDefault(UserPrincipal principal, KidsPackageKidsSearchRequest request);

    PackageDefaultUpdateResponse getPackageDefaultById(UserPrincipal principal, Long id);

    boolean updatePackageDefault(UserPrincipal principal, PackageDefaultUpdateRequest request);


}
