package com.example.onekids_project.service.servicecustom.employeesaraly;

import com.example.onekids_project.request.common.DescriptionRequest;
import com.example.onekids_project.response.employeesalary.ListOrderSalaryDetailResponse;
import com.example.onekids_project.security.model.UserPrincipal;

/**
 * date 2021-03-04 2:20 CH
 *
 * @author ADMIN
 */
public interface OrderSalaryService {

    ListOrderSalaryDetailResponse findSalaryPackagePaymentDetail(UserPrincipal principal, Long idOrder);

    boolean saveOrderSalaryDescription(UserPrincipal principal, Long idOrder, DescriptionRequest request);
}
