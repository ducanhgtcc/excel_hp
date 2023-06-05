package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.finance.statistical.FinanceSearchKidsRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.exportimport.KidsPackageExport;
import com.example.onekids_project.response.finance.exportimport.KidsPackageInOutExport;
import com.example.onekids_project.response.finance.exportimport.KidsPackageOrderExport;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * date 2021-03-20 10:23
 *
 * @author Nguyễn Thành
 */
public interface ExportImportFinanceService {

    /**
     * thống kê danh sách học sinh có khoản thu hoặc chi chưa hoàn thành
     *
     * @param kidsList
     * @param year
     * @param startMonth
     * @param endMonth
     * @param category
     * @return
     */
    List<KidsPackageOrderExport> getKidsPackageInOrOut(List<Kids> kidsList, int year, int startMonth, int endMonth, String category);

    /**
     * thống kê thu chi các khoản theo hóa đơn
     *
     * @param kidsList
     * @param year
     * @param startMonth
     * @param endMonth
     * @return
     */
    List<KidsPackageInOutExport> getKidsPackageInAndOut(List<Kids> kidsList, int year, int startMonth, int endMonth);

    /**
     * thống kê thu chi các khoản theo hóa đơn hoàn thành
     *
     * @param kidsList
     * @param year
     * @param startMonth
     * @param endMonth
     * @return
     */
    List<KidsPackageInOutExport> getKidsPackageInAndOutTrue(List<Kids> kidsList, int year, int startMonth, int endMonth);

    List<KidsPackageInOutExport> getKidsPackageInAndOutTotal(List<Kids> kidsList, int year, int startMonth, int endMonth);

    /**
     * lấy các hóa đơn chưa hoàn thành của học sinh
     *
     * @param kidsList
     * @param year
     * @param startMonth
     * @param endMonth
     * @return
     */
    List<KidsPackageExport> getKidsPackageOrder(List<Kids> kidsList, int year, int startMonth, int endMonth);


    ByteArrayInputStream exportFinanceKid(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException;

    List<ExcelResponse> exportFinanceKidNew(UserPrincipal principal, FinanceSearchKidsRequest request);

    ByteArrayInputStream exportFinanceKidOut(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException;

    List<ExcelResponse> exportFinanceKidOutNew(UserPrincipal principal, FinanceSearchKidsRequest request);

    ByteArrayInputStream exportFinanceKidInOut(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException;

    List<ExcelResponse> exportFinanceKidInOutNew(UserPrincipal principal, FinanceSearchKidsRequest request);

    ByteArrayInputStream exportFinanceKidInAndOut(UserPrincipal principal, FinanceSearchKidsRequest request) throws IOException;

    List<ExcelResponse> exportFinanceKidInAndOutNew(UserPrincipal principal, FinanceSearchKidsRequest request);

    List<ExcelResponse> exportFinanceKidInAndOutTrueNew(UserPrincipal principal, FinanceSearchKidsRequest request);

    List<ExcelResponse> exportFinanceKidInAndOutTotalNew(UserPrincipal principal, FinanceSearchKidsRequest request);


}
