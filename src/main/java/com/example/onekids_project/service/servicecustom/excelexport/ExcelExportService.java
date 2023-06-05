package com.example.onekids_project.service.servicecustom.excelexport;

import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.kids.ExcelGroupOutRequest;
import com.example.onekids_project.request.parentdiary.ExportMedicineRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-05-13 08:42
 *
 * @author lavanviet
 */
public interface ExcelExportService {
    List<ExcelResponse> getExportMessageParent(UserPrincipal principal, IdListRequest request);

    List<ExcelResponse> getExportMedicine(UserPrincipal principal, IdListRequest request);
    List<ExcelResponse> getExportMedicineDate(UserPrincipal principal, ExportMedicineRequest request);

    List<ExcelResponse> getExportAbsentLetter(UserPrincipal principal, IdListRequest request);

    List<ExcelResponse> getExportExcelSMS(Long idSchool, IdListRequest request);

    //Group out
    List<ExcelResponse> getExcelGroupOutKids(UserPrincipal principal, IdListRequest request);

    List<ExcelResponse> getExcelGroupOutKidsProviso(UserPrincipal principal, ExcelGroupOutRequest request);

    List<ExcelResponse> getExcelGroupOutEmployee(UserPrincipal principal, IdListRequest request);

    List<ExcelResponse> getExcelGroupOutEmployeeProviso(UserPrincipal principal, ExcelGroupOutRequest request);

}
