package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.request.finance.financegroup.*;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.financegroup.*;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-06-01 14:08
 *
 * @author lavanviet
 */
public interface FnPackageGroupService {
    List<PackageGroupResponse> searchPackageGroup(UserPrincipal principal, String name);

    PackageGroupResponse getPackageGroupById(UserPrincipal principal, Long id);

    boolean createPackageGroup(UserPrincipal principal, PackageGroupCreateRequest request);

    boolean updatePackageGroup(UserPrincipal principal, PackageGroupUpdateRequest request);

    boolean deletePackageGroupById(UserPrincipal principal, Long id);

    List<FnKidsPackageForGroupResponse> getPackageForAdd(UserPrincipal principal);

    boolean addPackageIntoGroup(UserPrincipal principal, Long idGroup, List<Long> idList);

    List<FnKidsPackageForGroupResponse> getPackageForRemove(UserPrincipal principal, Long idGroup);

    boolean removePackageInGroup(UserPrincipal principal, List<Long> idList);

    List<PackageGroupStatisticalResponse> getStatisticalFees(UserPrincipal principal, PackageGroupSearchRequest request);

    List<ExcelResponse> exportStatisticalFees(UserPrincipal principal, ExportRequest request);

    List<PackageGroupStatisticalResponse> getStatisticalSalary(UserPrincipal principal, PackageGroupSearchRequest request);

    List<ExcelResponse> exportStatisticalSalary(UserPrincipal principal, ExportRequest request);

    List<PackageGroupResponse> searchSalaryGroup(UserPrincipal principal, String name);

    PackageGroupResponse getSalaryGroupById(UserPrincipal principal, Long id);

    boolean createSalaryGroup(UserPrincipal principal, PackageGroupCreateRequest request);

    boolean updateSalaryGroup(UserPrincipal principal, PackageGroupUpdateRequest request);

    boolean deleteSalaryGroupById(UserPrincipal principal, Long id);

    boolean addSalarySampleIntoGroup(UserPrincipal principal, Long idGroup, List<Long> idList, String type);

    List<PackageGroupResponse> getSalaryGroup(UserPrincipal principal);

    CashBookMoneyResponse getCashBookStatistical(UserPrincipal principal, CashBookMoneyRequest request);
    InternalMoneyResponse getInternalStatistical(UserPrincipal principal, FinanceKidsStatisticalRequest request);
}
