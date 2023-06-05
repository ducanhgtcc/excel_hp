package com.example.onekids_project.service.servicecustom.finance.extend;

import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.response.finance.extend.PackageExtendUpdateResponse;

import java.util.List;

/**
 * date 2021-10-06 11:13
 *
 * @author lavanviet
 */
public interface FnPackageDefaultExtendService {
    void createDefaultExtendFromKid(Long idSchool, List<Long> idKidList);

    void activeDefaultExtendFromKid(Long idSchool, List<Long> idKidList, boolean active);

    void deleteDefaultExtendFromKid(Long idSchool, List<Long> idKidList);

    PackageExtendUpdateResponse getDefaultExtendById(Long idSchool, Long id);

    void updateDefaultExtend(Long idSchool, PackageExtendUpdateRequest request);

    void createDefaultExtendFromPackage(Long idSchool, List<Long> idDefaultPackageList);

    void activeDefaultExtendFromPackage(Long idSchool, List<Long> idDefaultPackageList, boolean active);

    void deleteDefaultExtendFromPackage(Long idSchool, List<Long> idDefaultPackageList);


}
