package com.example.onekids_project.service.servicecustom.finance;

import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.common.DescriptionRequest;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.finance.GenerateOrderKidsRequest;
import com.example.onekids_project.request.finance.KidsPackageInKidsSearchRequest;
import com.example.onekids_project.request.finance.order.*;
import com.example.onekids_project.response.excel.ExcelDynamicResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.finance.order.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-02-23 15:23
 *
 * @author lavanviet
 */
public interface FnOrderKidsService {
    List<OrderKidsResponse> searchOrderKids(UserPrincipal principal, KidsPackageInKidsSearchRequest request);

    List<OrderKidsCustom1> searchOrderKidsMonth(UserPrincipal principal, SearchOrderKidsDetailRequest request);

    ListOrderKidsResponse searchOrderKidsForKids(UserPrincipal principal, SearchOrderKidsAllRequest request);

    boolean generateOrderKids(UserPrincipal principal, GenerateOrderKidsRequest request);

    void generateOrderKidsAuto(List<Kids> kidsList);

    boolean sendNotifyOrderKids(UserPrincipal principal, GenerateOrderKidsRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;

    boolean setViewOrder(UserPrincipal principal, StatusRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;

    boolean setLockedOrder(UserPrincipal principal, StatusRequest request);

    boolean setViewOrderMany(UserPrincipal principal, StatusListRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;
    void sendViewOrderManyNoSMS(UserPrincipal principal, StatusListRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;

    boolean setLockedOrderMany(UserPrincipal principal, StatusListRequest request);

    ListKidsPackageForPaymentResponse searchKidsPackagePayment(UserPrincipal principal, Long idOrder, OrderRequest request);

    OrderPrintResponse getPrintOrder(UserPrincipal principal, OrderRequest request, IdListRequest idList) throws IOException;

    List<OrderPrintResponse> getPrintOrderMany(UserPrincipal principal, List<OrderManyRequest> request) throws IOException;

    OrderPrintResponse getPrintOrderOut(UserPrincipal principal, OrderRequest request, IdListRequest idList) throws IOException;

    OrderPrintResponse getPrintOrderIn(UserPrincipal principal, OrderRequest request, IdListRequest idList) throws IOException;

    ListOrderKidsDetailResponse findKidsPackagePaymentDetail(UserPrincipal principal, Long idOrder);

    boolean saveOrderKidsDescription(UserPrincipal principal, Long idOrder, DescriptionRequest request);

    /**
     * các thông tin được được lưu nhiều bảng: date, name, money, category: ở 3 table fncashbookhistory(ko có name), walllethistory, orderkidshistory
     * 1, create fn_cash_book_history để money mặc định là 0: ok(sẽ cập nhật lại ở dưới mục 6)
     * 2, create order_kids_history: ok
     * 3, create moneyInput into walletParentHisotry và update moneyInut for walletParent(if 1 true) if moneyInput>0: ok
     * 4, create ex_order_history_kids_package và update paid trong kid_package=>lấy được tổng số tiền đã thanh toán:
     * 5, create wallet_parent_history and update fn_wallet_parent
     * 6, update money trong fn_cash_book_history update cash_book
     * 7, update lastModifyDate in fnOrderKids
     *
     * @param principal
     * @param request
     * @return
     */
    boolean orderKidsPayment(UserPrincipal principal, Long idOrder, OrderKidsPaymentRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException;

    ExcelDynamicResponse excelExportOrderService(OrderExcelRequest request);
    ExcelDynamicResponse excelExportNowOrderService(OrderExcelRequest request);

}
