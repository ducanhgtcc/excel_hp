package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.request.finance.PackageSearchRequest;

import java.util.List;

public interface FnPackageRepositoryCustom {
    List<FnPackage> searchPackage(Long idSchool, PackageSearchRequest request);

    List<FnPackage> getPackageExcludePackageDefaultKid(Long idSchool, Long idKid);

    List<FnPackage> getPackageExcludePackageDefaultAndKidsPackage(Long idSchool, Long idKid, int month, int year);
    List<FnPackage> getPackageExcludePackageDefaultAndKidsPackageForAdd(List<Long> idList, Long idSchool, Long idKid, int month, int year);
}
