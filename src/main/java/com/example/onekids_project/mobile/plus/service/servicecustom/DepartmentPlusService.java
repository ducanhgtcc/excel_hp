package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.department.SearchEmployeeRequest;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.plus.response.department.EmployeePlusDetailResponse;
import com.example.onekids_project.mobile.plus.response.department.ListDepartmentPlusResponse;
import com.example.onekids_project.mobile.plus.response.department.ListEmployeePlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface DepartmentPlusService {

    ListDepartmentPlusResponse searchDepartment(UserPrincipal principal);

    ListEmployeePlusResponse searchEmployeePlus(UserPrincipal principal, SearchEmployeeRequest request);

    EmployeePlusDetailResponse findEmployeeDetail(UserPrincipal principal, Long id);
}
