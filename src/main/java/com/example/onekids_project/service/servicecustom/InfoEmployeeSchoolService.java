package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.importexport.model.EmployeeModel;
import com.example.onekids_project.master.request.employee.EmployeeSearchAdminRequest;
import com.example.onekids_project.master.response.employee.ListEmployeeAdminResponse;
import com.example.onekids_project.request.employee.*;
import com.example.onekids_project.response.employee.ListEmployeeNewResponse;
import com.example.onekids_project.response.employee.ListEmployeePlusNewResponse;
import com.example.onekids_project.response.employee.ListInfoEmployeeSchoolResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InfoEmployeeSchoolService {

    ListEmployeeNewResponse searchEmployee(UserPrincipal principal, EmployeeSearchNew search);

    ListEmployeePlusNewResponse searchEmployeePlus(UserPrincipal principal, EmployeePlusSearchNew search);

    ListEmployeeAdminResponse searchEmployeeAdmin(EmployeeSearchAdminRequest search);

    boolean updateActiveOne(UserPrincipal principal, EmployeeUpdateActiveOneRequest request);

    boolean updateActiveSMSOne(UserPrincipal principal, EmployeeUpdateActiveSMSOneRequest request);

    boolean updateActiveSMSSendOne(UserPrincipal principal, EmployeeUpdateActiveSMSSendOneRequest request);

    boolean updateActiveMany(UserPrincipal principal, EmployeeUpdateActiveManyRequest request);

    boolean updateActiveSMSMany(UserPrincipal principal, EmployeeUpdateActiveManyRequest request);

    /**
     * Tìm kiếm  tất cả các InfoEmployee (có thể lọc theo từng field)
     *
     * @param searchEmployeeRequest
     * @param pageable
     * @return
     */
    ListInfoEmployeeSchoolResponse findAllInfoEmployeeSchool(SearchInfoEmployeeRequest searchEmployeeRequest, Pageable pageable, Long idSchool);

    /**
     * Tìm kiếm  tất cả các InfoEmployee (có thể lọc theo từng field)
     *
     * @param searchExportEmployeeRequest
     * @return
     */

    ListInfoEmployeeSchoolResponse findAllInfoEmployee(Long idSchool, SearchExportEmployeeRequest searchExportEmployeeRequest);


    /**
     * Chuyển đổi response InfoEmployee  thành đối tượng hiển thị lên excel
     *
     * @param listInfoEmployeeSchoolResponse
     * @return
     */

    List<EmployeeModel> getFileAllEmployeeByDepartment(ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse, String nameSchool);
    /**
     * Chuyển đổi response InfoEmployee  thành đối tượng hiển thị lên excel NEW
     *
     * @param listInfoEmployeeSchoolResponse
     * @return
     */
    List<ExcelNewResponse> getFileAllEmployeeByDepartmentNew(ListInfoEmployeeSchoolResponse listInfoEmployeeSchoolResponse, UserPrincipal principal);

    //    ListInfoEmployeeSchoolResponse findAllInfoEmployeeSchoolById(Long idSchool,SearchExportEmployeeRequest searchExportEmployeeRequest);

}
