package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.response.finance.order.KidsPackagePaymentDetailResponse;
import com.example.onekids_project.response.finance.order.OrderKidsHistoryDetailResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-03-04 11:06
 *
 * @author lavanviet
 */
public interface ExOrderHistoryKidsPackageService {

    List<OrderKidsHistoryDetailResponse> findOrderKidsHistoryDetail(UserPrincipal principal, Long idOrderHistory);

    List<KidsPackagePaymentDetailResponse> findKidsPackagePaymentDetail(UserPrincipal principal, Long idKidsPackage);
}
