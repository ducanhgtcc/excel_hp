package com.example.onekids_project.controller.finance;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.cashbook.SearchCashBookHistoryRequest;
import com.example.onekids_project.request.cashinternal.*;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.response.cashbook.CashBookResponse;
import com.example.onekids_project.response.cashbook.CashbookMoneyResponse;
import com.example.onekids_project.response.cashbook.ListCashBookHistoryResponse;
import com.example.onekids_project.response.caskinternal.*;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.cashbook.CashBookHistoryService;
import com.example.onekids_project.service.servicecustom.cashbook.FnCashBookService;
import com.example.onekids_project.service.servicecustom.cashinternal.CashInternalService;
import com.example.onekids_project.service.servicecustom.cashinternal.FnBankService;
import com.example.onekids_project.service.servicecustom.cashinternal.PeopleTypeService;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/web/fn/cashinternal")
public class FnCashInternalController {

    @Autowired
    private CashInternalService cashInternalService;

    @Autowired
    private PeopleTypeService peopleTypeService;

    @Autowired
    private FnBankService fnBankService;

    @Autowired
    private FnCashBookService fnCashBookService;

    @Autowired
    private CashBookHistoryService cashBookHistoryService;

    //quản lý đối tượng

    /**
     * lấy danh sách đối tượng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/people/list")
    public ResponseEntity searchPeopleType(@CurrentUser UserPrincipal principal, @Valid SeacrhCashInternalRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListPeopleTypeReponseResponse response = peopleTypeService.searchPeopleType(principal, request);
        return NewDataResponse.setDataSearch(response);
    }


    /**
     * xem chi tiết
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/people/{id}")
    public ResponseEntity findDetailPeopleType(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        PeopleTypeResponse reponse = peopleTypeService.findDetailPeopleType(principal, id);
        return NewDataResponse.setDataSearch(reponse);
    }

    /**
     * Tạo đối tượng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/people")
    public ResponseEntity<Object> createPeopleType(@CurrentUser UserPrincipal principal, @RequestBody @Valid CreatePeopleTypeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = peopleTypeService.createPeopleType(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.CREATE_PEOPLETYPE);
    }

    /**
     * cập nhật đối tượng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/people")
    public ResponseEntity updatePeopleType(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdatePeopleTypeRequest request) {
        RequestUtils.getFirstRequestPlus(principal);
        boolean check = peopleTypeService.updatePeopleType(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * xóa theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/people/{id}")
    public ResponseEntity deletePeopleTypeById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal);
        boolean check = peopleTypeService.deletePeopleTypeById(principal, id);
        if (check == AppConstant.APP_FALSE) {
            return NewDataResponse.setDataCustom(check, "Xóa thất bại!");
        } else {
            return NewDataResponse.setDataCustom(check, MessageConstant.DELETE);
        }
    }
    //end quản lý đối tượng


    /**
     * lấy danh sách các đối tượng theo type cho việc tạo phiếu
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search-type")
    public ResponseEntity findPeopleTypeInternal(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        CashInternalCreateResponse response = peopleTypeService.searchPeopleInternal(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * lấy danh sách phiếu chi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/out/list")
    public ResponseEntity searchListCashOut(@CurrentUser UserPrincipal principal, @Valid SeacrhListpayRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListCashInternalinResponse response = cashInternalService.searchListCashOut(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tổng tiền chi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/out/total")
    public ResponseEntity getCashOutTotal(@CurrentUser UserPrincipal principal, @Valid SearchPayDateMonth request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        double response = cashInternalService.getCashOutTotal(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Tạo phiếu chi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/out/create")
    public ResponseEntity<Object> createCashInternalin(@CurrentUser UserPrincipal principal, @RequestBody @Valid CreateCashInternalRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = cashInternalService.createCashInternal(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.CREATE_CASHINTERNAL);
    }

    /**
     * duyệt phiếu chi
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/out/approved/{id}")
    public ResponseEntity approveCashOut(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @RequestParam boolean status) {
        RequestUtils.getFirstRequestExtend(principal, id, status);
        cashInternalService.approveCash(principal, id, status);
        return NewDataResponse.setDataApproved(status);
    }

    /**
     * thanh toán khoản chi
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/out/payment/{id}")
    public ResponseEntity PaymentOut(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean check = cashInternalService.paymentCash(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.PAYMENT_SUCCESS);
    }

    /**
     * duyệt phiếu chi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "out/approved/many")
    public ResponseEntity approvedManyOut(@CurrentUser UserPrincipal principal, @RequestBody StatusListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        cashInternalService.updateManyApproved(principal, request);
        return NewDataResponse.setDataApproved(request.getStatus());
    }


    /**
     * hủy phiếu chi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "out/cancel/many")
    public ResponseEntity cancelManyOut(@CurrentUser UserPrincipal principal, @RequestBody StatusListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        cashInternalService.cancelMany(principal, request);
        return NewDataResponse.setDataApproved(request.getStatus());
    }


    /**
     * lấy danh sách phiếu thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/in/list")
    public ResponseEntity searchListCashIn(@CurrentUser UserPrincipal principal, @Valid SeacrhListpayRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListCashInternalinResponse response = cashInternalService.searchListCashIn(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Tạo phiếu thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/in/create")
    public ResponseEntity<Object> createCashCollect(@CurrentUser UserPrincipal principal, @RequestBody @Valid CreateCashInternalRequest request) throws IOException, DocumentException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = cashInternalService.createCashCollect(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.CREATE_CASHCOLLECT);
    }

    /**
     * duyệt phiếu thu
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/in/approved/{id}")
    public ResponseEntity approveCashIn(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @RequestParam boolean status) {
        RequestUtils.getFirstRequestExtend(principal, id, status);
        cashInternalService.approveCash(principal, id, status);
        return NewDataResponse.setDataApproved(status);
    }


    /**
     * thanh toán khoản thu
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/in/payment/{id}")
    public ResponseEntity PaymentIn(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean check = cashInternalService.paymentCash(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.PAYMENT_SUCCESS);
    }

    /**
     * duyệt phiếu thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "in/approved/many")
    public ResponseEntity approvedManyIn(@CurrentUser UserPrincipal principal, @RequestBody StatusListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        cashInternalService.updateManyApproved(principal, request);
        return NewDataResponse.setDataApproved(request.getStatus());
    }

    /**
     * hủy phiếu thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "in/cancel/many")
    public ResponseEntity cancelManyIn(@CurrentUser UserPrincipal principal, @RequestBody StatusListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        cashInternalService.cancelMany(principal, request);
        return NewDataResponse.setDataApproved(request.getStatus());
    }

    /**
     * xem chi tiết
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findDetailCashInternal(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        CashInternalinResponse reponse = cashInternalService.findDeTailCashInternal(principal, id);
        return NewDataResponse.setDataSearch(reponse);
    }

    /**
     * cập nhật phiếu chi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update-cash/{id}")
    public ResponseEntity UpdateCashInternal(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateCashinternalInRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = cashInternalService.updateCashInternal(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/print/{id}")
    public ResponseEntity createPdf(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws IOException, DocumentException {
        RequestUtils.getFirstRequestPlus(principal, id);
        cashInternalService.printpdf(principal, id);
        return NewDataResponse.setDataCustom(true, "ok");
    }

    //start thông tin thanh toán

    /**
     * Danh sách tài khoản ngân hàng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/bank")
    public ResponseEntity findAllBankInfo(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<BankResponse> responseList = fnBankService.searchBankInfo(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Chi tiết
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/bank/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        BankResponse response = fnBankService.findByIdBankInfo(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Cập nhật thông tin tài khoản ngân hàng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/bank")
    public ResponseEntity updateBankInfo(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateBankInfoRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = fnBankService.updateBankInfo(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.UPDATE_BANK_INFO);
    }

    /**
     * Xóa thông tin tài khoản ngân hàng
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/bank/{id}")
    public ResponseEntity deleteBankInfo(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        boolean check = fnBankService.deleteBankInFo(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.DELETE_BANK_INFO);
    }

    /**
     * Thêm thông tin tài khoản ngân hàng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/bank")
    public ResponseEntity createBankInfo(@CurrentUser UserPrincipal principal, @RequestBody @Valid CreateBankInforRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = fnBankService.createBankInfo(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.CREATE_BANK_INFO);
    }

    /**
     * cập nhật chọn tài khoản chính
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/bank/checked")
    public ResponseEntity updateCheckedBank(@CurrentUser UserPrincipal principal, @RequestBody @Valid IdObjectRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean check = fnBankService.updateCheckedBank(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.BANK_CHECKED);
    }
    //end thông tin thanh toán

    //start cashbook

    /**
     * danh sách cashbook theo năm
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/cashbook")
    public ResponseEntity findCashBook(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<CashBookResponse> responseList = fnCashBookService.findCashBook(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm cashbookhistory
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/cashbook/history")
    public ResponseEntity findCashBookHistory(@CurrentUser UserPrincipal principal, @Valid SearchCashBookHistoryRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListCashBookHistoryResponse responseList = cashBookHistoryService.searchCashBookHistory(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm cashbookhistory now
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/cashbook/history-now")
    public ResponseEntity findCashBookHistoryNow(@CurrentUser UserPrincipal principal, @Valid SearchCashBookHistoryRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListCashBookHistoryResponse responseList = cashBookHistoryService.searchCashBookHistoryNow(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cashbook/history-year/{year}")
    public ResponseEntity findCashBookHistoryYear(@CurrentUser UserPrincipal principal, @PathVariable Integer year, @Valid SearchCashBookHistoryRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        CashbookMoneyResponse data = cashBookHistoryService.searchCashBookHistoryYear(principal, year, request);
        return NewDataResponse.setDataSearch(data);
    }
    //end cashbook

}
