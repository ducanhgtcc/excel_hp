package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.request.finance.*;
import com.example.onekids_project.response.finance.*;
import com.example.onekids_project.response.finance.fnpackage.PackageRootRequest;
import com.example.onekids_project.response.finance.fnpackage.PackageRootResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface FnPackageService {
    List<PackageBriefResponse> searchPackage(UserPrincipal principal, PackageSearchRequest request);

    PackageResponse getById(UserPrincipal principal, Long id);

    boolean createPackage(UserPrincipal principal, PackageCreateRequest request);

    boolean updatePackage(UserPrincipal principal, PackageUpdateRequest request);

    void createPackageRoot(Long idSchool);

    List<PackageRootResponse> searchRootPackage();
    void updateRootPackage(PackageRootRequest request);

    List<KidsPackageForPackageResponse> detailPackage(UserPrincipal principal, Long idPackage, LocalDate date);

    boolean deletePackageById(UserPrincipal principal, Long id);

    boolean changeSortPackage(UserPrincipal principal, ChangeSortRequest request);

    List<MaClassPackageResponse> searchClassPackage(UserPrincipal principal, String className);

    List<PackageInClassResponse> getPackageInClass(UserPrincipal principal, Long idClass);

    boolean addPackageForClass(UserPrincipal principal, AddPackageClassRequest request);

}
