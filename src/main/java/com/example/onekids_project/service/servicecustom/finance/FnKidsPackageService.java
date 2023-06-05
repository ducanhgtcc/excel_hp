package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.finance.*;
import com.example.onekids_project.request.finance.approved.KidsPackageInKidsSearchDetailRequest;
import com.example.onekids_project.request.finance.kidspackage.SaveNumberManyRequest;
import com.example.onekids_project.request.finance.kidspackage.TransferNumberManyRequest;
import com.example.onekids_project.request.finance.kidspackage.TransferNumberOneRequest;
import com.example.onekids_project.request.finance.kidspackage.UserNumberRequest;
import com.example.onekids_project.response.finance.*;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface FnKidsPackageService {
    ListKidsPackageInClassResponse searchKidsPackageInClass(UserPrincipal principal, KidsPackageInClassSearchRequest request);

    int generateKidsPackageOne(UserPrincipal principal, Long idKid, LocalDate request);

    boolean generateKidsPackageMany(UserPrincipal principal, GenerateKidsPackageRequest request);

    void generateKidsPackageAuto(School school, Kids kids, LocalDate date);

    boolean activeKidsPackageOne(UserPrincipal principal, KidsPackageActiveRequest request);

    boolean activeKidsPackageMany(UserPrincipal principal, List<KidsPackageActiveRequest> requestList);

    List<Package2Response> getPackageForKidsPackageAdd(UserPrincipal principal, Long idKid, LocalDate date);

    List<PackageInClassResponse> getPackageForKidsPackageAddMany(UserPrincipal principal, Long idClass);

    int createPackageForKidsPackageAddMany(UserPrincipal principal, AddPackageForKidsRequest request);

    boolean createKidsPackageFromSchool(UserPrincipal principal, Long idKid, KidsPackageCreateRequest request);

    FnKidsPackage createKidsPackageRootFromPackage(Kids kids, FnPackage fnPackage, int month, int year, double price, double paid);

    List<KidsPackageDetailResponse> getKidsPackageByIdKid(UserPrincipal principal, Long idKid, SearchKidsPackageDetailRequest request);

    KidsPackageUpdateResponse getKidsPackageById(UserPrincipal principal, Long id);

    boolean updateKidsPackage(UserPrincipal principal, KidsPackageUpdateRequest request);

    boolean deletePackageKidById(UserPrincipal principal, Long idKidsPackage);

    boolean deletePackageKidsMany(UserPrincipal principal, List<Long> idList);

    int deletePackageKidsManyKids(List<Long> idKidList, List<Long> idPackageList, LocalDate date);

    boolean addFromPackageDefault(UserPrincipal principal, Long idKid, Long idDefaultPackage);

    int addFromPackageDefaultMany(UserPrincipal principal, List<Long> idKidList, List<Long> idPackageDefaultList);

    boolean activeKidsPackageOne(UserPrincipal principal, PackageDefaultActiveRequest request);

    List<KidsForKidsPackageResponse> searchKidsPackageForKids(UserPrincipal principal, KidsPackageInKidsSearchRequest request);

    List<KidsPackageForApprovedResponse> searchKidsPackageForApproved(UserPrincipal principal, KidsPackageInKidsSearchRequest request);
    List<KidsPackageForApprovedResponse> searchKidsPackageForApprovedDetail(UserPrincipal principal, KidsPackageInKidsSearchDetailRequest request);

    boolean approvedKidsPackage(UserPrincipal principal, ApprovedRequest request);
    void approvedKidsPackageAdvance(UserPrincipal principal, Long id);
    void deleteKidsPackageAdvance(UserPrincipal principal, Long id);

    boolean lockedKidsPackage(UserPrincipal principal, LockedRequest request);

    boolean approvedKidsPackageMany(UserPrincipal principal, StatusKidsListRequest request);

    boolean lockedKidsPackageMany(UserPrincipal principal, StatusKidsListRequest request);

    boolean saveUsedNumber(UserPrincipal principal, UserNumberRequest request);

    boolean saveTransferNumberOne(UserPrincipal principal, TransferNumberOneRequest request);

    boolean saveTransferNumberMany(UserPrincipal principal, List<TransferNumberManyRequest> requestList);

    boolean saveUseNumberMany(UserPrincipal principal, List<SaveNumberManyRequest> requestList);
    int getCalculateNumber(Kids kids, FnKidsPackage fnKidsPackage);
}
