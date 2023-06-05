package com.example.onekids_project.service.servicecustom.finance.extend;

import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.response.finance.extend.PackageExtendUpdateResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-10-07 16:34
 *
 * @author lavanviet
 */
public interface FnPackageKidsExtendService {
    void createKidsExtendFromKid(Long idSchool, List<Long> idKidList, LocalDate date);

    void activeKidsExtendFromKid(Long idSchool, List<Long> idKidList, boolean active, LocalDate date);

    void deleteKidsExtendFromKid(Long idSchool, List<Long> idKidList, LocalDate date);

    PackageExtendUpdateResponse getKidsExtendById(Long idSchool, Long id);

    void updateKidsExtend(Long idSchool, PackageExtendUpdateRequest request);

    void createKidsExtendFromPackage(Long idSchool, List<Long> idKidsPackageList);

    void activeKidsExtendFromPackage(Long idSchool, List<Long> idKidsPackageList, boolean active);

    void deleteKidsExtendFromPackage(Long idSchool, List<Long> idKidsPackageList);

    void saveKidsExtend(List<FnKidsPackage> fnKidsPackageList, String generateType);
}
