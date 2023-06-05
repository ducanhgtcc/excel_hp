package com.example.onekids_project.controller.finance;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.common.*;
import com.example.onekids_project.request.finance.*;
import com.example.onekids_project.request.finance.approved.KidsPackageInKidsSearchDetailRequest;
import com.example.onekids_project.request.finance.kidspackage.*;
import com.example.onekids_project.request.finance.order.*;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalMiniRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.request.finance.wallet.BankBriefResponse;
import com.example.onekids_project.request.finance.wallet.SearchWalletParentHistoryRequest;
import com.example.onekids_project.request.finance.wallet.WalletParentHistoryCreateRequest;
import com.example.onekids_project.request.finance.wallet.WalletParentStatisticalRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelDynamicResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.*;
import com.example.onekids_project.response.finance.fnpackage.PackageRootRequest;
import com.example.onekids_project.response.finance.fnpackage.PackageRootResponse;
import com.example.onekids_project.response.finance.kidspackage.KidsInfoDataResponse;
import com.example.onekids_project.response.finance.order.*;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalMiniResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.response.finance.wallet.ListWalletParentHistoryResponse;
import com.example.onekids_project.response.finance.wallet.ListWalletParentStatisticalResponse;
import com.example.onekids_project.response.finance.wallet.WalletParentResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.cashinternal.FnBankService;
import com.example.onekids_project.service.servicecustom.finance.*;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/web/fn/fees")
public class FnFeesController {
    @Autowired
    private FnPackageService fnPackageService;

    @Autowired
    private FnKidsPackageDefaultService fnKidsPackageDefaultService;

    @Autowired
    private FnKidsPackageService fnKidsPackageService;

    @Autowired
    private FnOrderKidsService fnOrderKidsService;

    @Autowired
    private WalletParentService walletParentService;

    @Autowired
    private WalletParentHistoryService walletParentHistoryService;

    @Autowired
    private FnBankService fnBankService;

    @Autowired
    private OrderKidsHistoryService orderKidsHistoryService;

    @Autowired
    private ExOrderHistoryKidsPackageService exOrderHistoryKidsPackageService;

    @Autowired
    private KidsService kidsService;

    @Autowired
    private FinanceStatisticalKidsService financeStatisticalKidsService;

    //start Danh sách khoản

    /**
     * danh sách các khoản thu của trường
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/package/search")
    public ResponseEntity searchPackage(@CurrentUser UserPrincipal principal, @Valid PackageSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<PackageBriefResponse> responseList = fnPackageService.searchPackage(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm khoản thu theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/package/{id}")
    public ResponseEntity getPackageById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        PackageResponse response = fnPackageService.getById(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tạo khoản thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/package")
    public ResponseEntity createPackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnPackageService.createPackage(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * cập nhật khoản thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/package")
    public ResponseEntity updatePackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnPackageService.updatePackage(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * chi tiết khoản thu
     *
     * @param principal
     * @param idPackage
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/package/detail/{idPackage}")
    public ResponseEntity detailPackage(@CurrentUser UserPrincipal principal, @PathVariable Long idPackage, DateNotNullRequest date) {
        RequestUtils.getFirstRequest(principal, idPackage);
        List<KidsPackageForPackageResponse> responseList = fnPackageService.detailPackage(principal, idPackage, date.getDate());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * xóa khoản thu
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/package/{id}")
    public ResponseEntity deletePackage(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = fnPackageService.deletePackageById(principal, id);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * sắp xếp các khoản thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/package/change-sort")
    public ResponseEntity changeSortPackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody ChangeSortRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnPackageService.changeSortPackage(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SORT_ROW);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/package/root/search")
    public ResponseEntity searchRootPackage(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<PackageRootResponse> responseList = fnPackageService.searchRootPackage();
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/package/root")
    public ResponseEntity updatePackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageRootRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        fnPackageService.updateRootPackage(request);
        return NewDataResponse.setDataUpdate(true);
    }

    /**
     * tìm kiếm các khoản thu
     *
     * @param principal
     * @param className
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/package/class/search")
    public ResponseEntity searchClassPackage(@CurrentUser UserPrincipal principal, @RequestParam(required = false) String className) {
        RequestUtils.getFirstRequest(principal, className);
        List<MaClassPackageResponse> responseList = fnPackageService.searchClassPackage(principal, className);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lấy các khoản thu để thêm cho một lớp
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/package/class/package-in-class/{id}")
    public ResponseEntity getPackageInClass(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<PackageInClassResponse> responseList = fnPackageService.getPackageInClass(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thêm các khoản thu cho lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/package/class/add-in-class")
    public ResponseEntity addPackageInClass(@CurrentUser UserPrincipal principal, @Valid @RequestBody AddPackageClassRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnPackageService.addPackageForClass(principal, request);
        return NewDataResponse.setDataSave(check);
    }

    //end Danh sách khoản

    //start Đăng ký khoản

    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/default/class/{idClass}")
    public ResponseEntity searchKidsPackageDefaultInClass(@CurrentUser UserPrincipal principal, @PathVariable Long idClass) {
        RequestUtils.getFirstRequest(principal, idClass);
        List<PackageInClassResponse> responseList = fnKidsPackageDefaultService.searchKidsPackageDefaultInClass(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * danh sách các khoản thu mặc định
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/default/search")
    public ResponseEntity searchKidsPackageDefault(@CurrentUser UserPrincipal principal, @Valid KidsPackageKidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListKidsPackageDefaultResponse response = fnKidsPackageDefaultService.searchKidsPackageDefault(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * đăng ký/hủy đăng ký các khoản thu cho nhiều học sinh trong một lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/default/one")
    public ResponseEntity saveKidsPackageDefaultOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody KidsPackageDefaultRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageDefaultService.updateKidsPackageDefaultOne(principal, request);
        return NewDataResponse.setDataSave(check);
    }

    /**
     * đăng ký/hủy đăng ký các khoản thu cho nhiều học sinh trong một lớp
     *
     * @param principal
     * @param requestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/default/many")
    public ResponseEntity saveKidsPackageDefaultMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<KidsPackageDefaultRequest> requestList) {
        RequestUtils.getFirstRequest(principal, requestList);
        boolean check = fnKidsPackageDefaultService.updateKidsPackageDefaultMany(principal, requestList);
        return NewDataResponse.setDataSave(check);
    }

    /**
     * lấy danh sách các khoản thu cho dialog tạo khoản thu mặc định
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/default/add/{idKid}")
    public ResponseEntity searchKidsPackageDefault(@CurrentUser UserPrincipal principal, @PathVariable Long idKid) {
        RequestUtils.getFirstRequest(principal, idKid);
        List<PackageResponse> responseList = fnKidsPackageDefaultService.getPackageForDefaultPackageAdd(principal, idKid);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tạo khoản thu mặc định
     *
     * @param principal
     * @param idKid
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids-package/default/add/{idKid}")
    public ResponseEntity createPackageDefault(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, @Valid @RequestBody PackageDefaultCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageDefaultService.createPackageDefault(principal, idKid, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * lấy danh sách các khoản thu mặc định của 1 học sinh
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/default/kid/{idKid}")
    public ResponseEntity getPackageDefaultKid(@CurrentUser UserPrincipal principal, @PathVariable Long idKid) {
        RequestUtils.getFirstRequest(principal, idKid);
        List<PackageDefaultDetailResponse> responseList = fnKidsPackageDefaultService.getPackageDefaultKid(principal, idKid);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * đăng ký/hủy đăng ký một khoản thu mặc định cho một học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/default/kid/active")
    public ResponseEntity getPackageDefaultKid(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageDefaultActiveRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageDefaultService.activePackageDefaultKid(principal, request);
        String message = request.isActive() ? MessageWebConstant.SUCCESS_ACTIVE : MessageWebConstant.SUCCESS_UNACTIVE;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * xóa khoản thu mặc định
     *
     * @param principal
     * @param idPackageDefault
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/kids-package/default/kid/{idPackageDefault}")
    public ResponseEntity deletePackageDefaultKid(@CurrentUser UserPrincipal principal, @PathVariable Long idPackageDefault) {
        RequestUtils.getFirstRequest(principal, idPackageDefault);
        boolean check = fnKidsPackageDefaultService.deletePackageDefaultKid(principal, idPackageDefault);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * lấy khoản thu mặc định theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/default/{id}")
    public ResponseEntity getPackageDefaultById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        PackageDefaultUpdateResponse response = fnKidsPackageDefaultService.getPackageDefaultById(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * cập nhật khoản thu mặc định cho học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/default")
    public ResponseEntity updatePackageDefault(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageDefaultUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageDefaultService.updatePackageDefault(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * tìm kiếm danh sách các khoản thu đã đăng ký theo lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/search")
    public ResponseEntity searchKidsPackageController(@CurrentUser UserPrincipal principal, @Valid KidsPackageInClassSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListKidsPackageInClassResponse response = fnKidsPackageService.searchKidsPackageInClass(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * khởi tạo khoản thu cho một học sinh
     *
     * @param principal
     * @param idKid
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids-package/generate/{idKid}")
    public ResponseEntity generateKidsPackageOne(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, @Valid DateNotNullRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idKid, request);
        int number = fnKidsPackageService.generateKidsPackageOne(principal, idKid, request.getDate());
        return NewDataResponse.setDataCustom(number, FinanceUltils.getMessageGenerate(number));
    }

    /**
     * khởi tạo khoản thu cho nhiều học sinh theo lớp
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids-package/generate")
    public ResponseEntity generateKidsPackageManyController(@CurrentUser UserPrincipal principal, @Valid @RequestBody GenerateKidsPackageRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.generateKidsPackageMany(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.GENERATE_KIDS_PACKAGE);
    }

    /**
     * kích hoạt/hủy kích hoạt nhiều khoản thu cho một học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/active/one")
    public ResponseEntity activeKidsPackageOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody KidsPackageActiveRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.activeKidsPackageOne(principal, request);
        return NewDataResponse.setDataSave(check);
    }


    /**
     * kích hoạt/hủy kích hoạt nhiều khoản thu cho nhiều học sinh trong một lớp
     *
     * @param principal
     * @param requestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/active/many")
    public ResponseEntity activeKidsPackageMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<KidsPackageActiveRequest> requestList) {
        RequestUtils.getFirstRequest(principal, requestList);
        boolean check = fnKidsPackageService.activeKidsPackageMany(principal, requestList);
        return NewDataResponse.setDataSave(check);
    }

    /**
     * lấy các khoản thu để thêm cho học sinh (từ các khoản thu ko thuộc mặc định và ko có trong đăng ký)
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/add/{idKid}")
    public ResponseEntity getPackageForAdd(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, @Valid DateNotNullRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idKid, request);
        List<Package2Response> responseList = fnKidsPackageService.getPackageForKidsPackageAdd(principal, idKid, request.getDate());
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thêm 1 khoản thu cho 1 học sinh(từ các khoản thu ko thuộc mặc định và ko có trong đăng ký)
     *
     * @param principal
     * @param idKid
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids-package/add/{idKid}")
    public ResponseEntity createKidsPackageAdd(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, @Valid @RequestBody KidsPackageCreateRequest request) {
        RequestUtils.getFirstRequestExtend(principal, idKid, request);
        boolean check = fnKidsPackageService.createKidsPackageFromSchool(principal, idKid, request);
        return NewDataResponse.setDataCreate(check);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/add/many/{idClass}")
    public ResponseEntity getPackageForAddMany(@CurrentUser UserPrincipal principal, @PathVariable Long idClass) {
        RequestUtils.getFirstRequest(principal, idClass);
        List<PackageInClassResponse> responseList = fnKidsPackageService.getPackageForKidsPackageAddMany(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tạo theo hàm khi thêm 1 khoản: chỉ tạo những khoản chưa tồn tại và chưa có trong mặc định
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids-package/add/many")
    public ResponseEntity createPackageForAddMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody AddPackageForKidsRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        int number = fnKidsPackageService.createPackageForKidsPackageAddMany(principal, request);
        return NewDataResponse.setDataCustom(number, FinanceUltils.getMessageGenerate(number));
    }

    /**
     * lấy danh sách chi tiết các khoản thu của một học sinh
     *
     * @param principal
     * @param idKid
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/{idKid}")
    public ResponseEntity getPackageKid(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, @Valid SearchKidsPackageDetailRequest request) {
        RequestUtils.getFirstRequest(principal, idKid, request);
        List<KidsPackageDetailResponse> responseList = fnKidsPackageService.getKidsPackageByIdKid(principal, idKid, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lấy khoản thu học sinh theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/update/{id}")
    public ResponseEntity getPackageKidById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        KidsPackageUpdateResponse response = fnKidsPackageService.getKidsPackageById(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * cập nhật khoản thu cho học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/update")
    public ResponseEntity updatePackageKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody KidsPackageUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.updateKidsPackage(principal, request);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * xóa khoản thu học sinh
     *
     * @param principal
     * @param idKidsPackage
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/kids-package/{idKidsPackage}")
    public ResponseEntity deletePackageKidsKidById(@CurrentUser UserPrincipal principal, @PathVariable Long idKidsPackage) {
        RequestUtils.getFirstRequest(principal, idKidsPackage);
        boolean check = fnKidsPackageService.deletePackageKidById(principal, idKidsPackage);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * xóa nhieu khoản thu học sinh
     *
     * @param principal
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/kids-package/many")
    public ResponseEntity deletePackageKidsKidById(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequest(principal, idList);
        boolean check = fnKidsPackageService.deletePackageKidsMany(principal, idList);
        return NewDataResponse.setDataDelete(check);
    }

    /**
     * xóa nhieu khoản thu của học sinh cho nhiều học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/kids-package/many-kids")
    public ResponseEntity deletePackageKidsKidById(@CurrentUser UserPrincipal principal, @Valid KidPackageDeleteManyRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        int count = fnKidsPackageService.deletePackageKidsManyKids(request.getIdKidList(), request.getIdPackageList(), request.getDate());
        return NewDataResponse.setDataDelete(count);
    }

    /**
     * thêm khoản thu cho học sinh từ mặc định
     *
     * @param principal
     * @param idKid
     * @param idPackageDefault
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids-package/add/default-package/{idKid}")
    public ResponseEntity addFromPackageDefault(@CurrentUser UserPrincipal principal, @PathVariable Long idKid, IdObjectRequest idPackageDefault) {
        RequestUtils.getFirstRequest(principal, idKid, idPackageDefault);
        boolean check = fnKidsPackageService.addFromPackageDefault(principal, idKid, idPackageDefault.getId());
        return NewDataResponse.setDataCreate(check);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/kids-package/add/default-package/many")
    public ResponseEntity addFromPackageDefaultMany(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList, @RequestParam List<Long> idPackageDefaultList) {
        RequestUtils.getFirstRequest(principal, idKidList, idPackageDefaultList);
        int count = fnKidsPackageService.addFromPackageDefaultMany(principal, idKidList, idPackageDefaultList);
        String message = count > 0 ? "Bổ sung thành công " + count + " khoản cho học sinh" : "Không có khoản nào được bổ sung";
        return NewDataResponse.setDataCustom(count, message);
    }

    /**
     * kích hoạt/hủy kích hoạt 1 khoản thu của học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-package/kid/active")
    public ResponseEntity activeKidsPackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageDefaultActiveRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.activeKidsPackageOne(principal, request);
        String message = request.isActive() ? MessageWebConstant.SUCCESS_ACTIVE : MessageWebConstant.SUCCESS_UNACTIVE;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * danh sách các khoản thu của các học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-package/kids/search")
    public ResponseEntity searchKidsPackageForKids(@CurrentUser UserPrincipal principal, @Valid KidsPackageInKidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<KidsForKidsPackageResponse> responseList = fnKidsPackageService.searchKidsPackageForKids(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }
    //end Đăng ký khoản

    //start Duyệt học phí

    /**
     * tìm kiếm danh sách các khoản thu của các học sinh ở tab duyệt khoản thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/approved/kids-package/search")
    public ResponseEntity searchKidsPackageForApproved(@CurrentUser UserPrincipal principal, @Valid KidsPackageInKidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<KidsPackageForApprovedResponse> responseList = fnKidsPackageService.searchKidsPackageForApproved(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * danh sách khoản thu phần duyệt cho một học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/approved/kids-package/search/detail")
    public ResponseEntity searchKidsPackageForApprovedDetail(@CurrentUser UserPrincipal principal, @Valid KidsPackageInKidsSearchDetailRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<KidsPackageForApprovedResponse> responseList = fnKidsPackageService.searchKidsPackageForApprovedDetail(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * duyệt/bỏ duyệt một khoản thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/kids-package/approved/one")
    public ResponseEntity approvedKidsPackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody ApprovedRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.approvedKidsPackage(principal, request);
        String message = request.isApproved() ? MessageWebConstant.SUCCESS_APPROVED : MessageWebConstant.SUCCESS_UNAPPROVED;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * bỏ duyệt trong bất kỳ TH nào
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/kids-package/approved/one/advance/{id}")
    public ResponseEntity approvedKidsPackageAdvance(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        fnKidsPackageService.approvedKidsPackageAdvance(principal, id);
        return NewDataResponse.setDataCustom(true, MessageWebConstant.SUCCESS_UNAPPROVED);
    }

    /**
     * xóa khoản trong bất kỳ TH nào
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/approved/kids-package/approved/one/advance/{id}")
    public ResponseEntity deleteKidsPackageAdvance(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        fnKidsPackageService.deleteKidsPackageAdvance(principal, id);
        return NewDataResponse.setDataCustom(true, MessageWebConstant.SUCCESS_DELETE);
    }

    /**
     * khóa/bỏ khóa một khoản thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/kids-package/locked/one")
    public ResponseEntity lockedKidsPackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody LockedRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.lockedKidsPackage(principal, request);
        String message = request.isLocked() ? MessageWebConstant.SUCCESS_LOCKED : MessageWebConstant.SUCCESS_UNLOCKED;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * duyệt/bỏ duyệt các khoản thu của các học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/kids-package/approved/many")
    public ResponseEntity approvedKidsPackageMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusKidsListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.approvedKidsPackageMany(principal, request);
        String message = request.isStatus() ? MessageWebConstant.SUCCESS_APPROVED : MessageWebConstant.SUCCESS_UNAPPROVED;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * khóa/bỏ khóa các khoản thu của các học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/kids-package/locked/many")
    public ResponseEntity lockedKidsPackageMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusKidsListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.lockedKidsPackageMany(principal, request);
        String message = request.isStatus() ? MessageWebConstant.SUCCESS_LOCKED : MessageWebConstant.SUCCESS_UNLOCKED;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * Cập nhật số lượng tính toán sang số lượng sử dụng cho một khoản thu
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/use-number/transfer/one")
    public ResponseEntity transferCalculateNumberOne(@CurrentUser UserPrincipal principal, @Valid @RequestBody TransferNumberOneRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.saveTransferNumberOne(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.NUMBER_TRANSFER);
    }

    /**
     * Cập nhật số lượng tính toán sang số lượng sử dụng cho nhiều học sinh
     *
     * @param principal
     * @param requestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/use-number/transfer/many")
    public ResponseEntity transferCalculateNumberMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<TransferNumberManyRequest> requestList) {
        RequestUtils.getFirstRequest(principal, requestList);
        boolean check = fnKidsPackageService.saveTransferNumberMany(principal, requestList);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.NUMBER_TRANSFER);
    }

    /**
     * lưu số lượng sử dụng thực tế
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/use-number/save/one")
    public ResponseEntity saveUsedNumberKidsPackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody UserNumberRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnKidsPackageService.saveUsedNumber(principal, request);
        return NewDataResponse.setDataSave(check);
    }

    /**
     * lưu số sử dụng cho nhiều học sinh
     *
     * @param principal
     * @param requestList
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/approved/use-number/save/many")
    public ResponseEntity saveUseNumberMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<SaveNumberManyRequest> requestList) {
        RequestUtils.getFirstRequest(principal, requestList);
        boolean check = fnKidsPackageService.saveUseNumberMany(principal, requestList);
        return NewDataResponse.setDataSave(check);
    }
    //end Duyệt học phí

    // start thu học phí

    /**
     * tìm kiếm danh sách các hóa đơn cho các học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/search")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal, @Valid KidsPackageInKidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<OrderKidsResponse> responseList = fnOrderKidsService.searchOrderKids(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }


    /**
     * tìm kiếm các hóa đơn 1 học sinh theo năm
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/month/search")
    public ResponseEntity searchOrderKidsMonth(@CurrentUser UserPrincipal principal, @Valid SearchOrderKidsDetailRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<OrderKidsCustom1> responseList = fnOrderKidsService.searchOrderKidsMonth(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm các hóa đơn một học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/kids/search")
    public ResponseEntity searchOrderForKids(@CurrentUser UserPrincipal principal, @Valid SearchOrderKidsAllRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListOrderKidsResponse response = fnOrderKidsService.searchOrderKidsForKids(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * khởi tạo hóa đơn khi click
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/order/generate")
    public ResponseEntity createOrderKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody GenerateOrderKidsRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnOrderKidsService.generateOrderKids(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.GENERATE_ORDER_KIDS);
    }

    /**
     * gửi thông báo học phí
     *
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/notify")
    public ResponseEntity sendNotify(@CurrentUser UserPrincipal principal, @Valid @RequestBody GenerateOrderKidsRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnOrderKidsService.sendNotifyOrderKids(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SEND_NOTIFY);
    }

    /**
     * hiện thị/bỏ hiện thị một hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/view/one")
    public ResponseEntity setViewOrder(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnOrderKidsService.setViewOrder(principal, request);
        String message = request.getStatus() ? MessageWebConstant.VIEW_ACTIVE : MessageWebConstant.VIEW_INACTIVE;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * khóa/bỏ khóa một hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/locked/one")
    public ResponseEntity setLockedOrder(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnOrderKidsService.setLockedOrder(principal, request);
        return NewDataResponse.setDataLocked(check);
    }

    /**
     * hiện thị/bỏ hiện thi nhiều hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/view/many")
    public ResponseEntity setViewOrderMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusListRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnOrderKidsService.setViewOrderMany(principal, request);
        String message = request.getStatus() ? MessageWebConstant.VIEW_ACTIVE : MessageWebConstant.VIEW_INACTIVE;
        return NewDataResponse.setDataCustom(check, message);
    }

    /*
    thông báo giống thao tác click hiện thị
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/notify/app")
    public ResponseEntity notifyOrderByApp(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusListRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        fnOrderKidsService.sendViewOrderManyNoSMS(principal, request);
        return NewDataResponse.setSendApp();
    }

    /**
     * khóa/bỏ khóa nhiều hóa đơn
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/locked/many")
    public ResponseEntity setLockedOrderMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = fnOrderKidsService.setLockedOrderMany(principal, request);
        return NewDataResponse.setDataLocked(check);
    }

    /**
     * tìm kiếm các khoản thu cho mục thanh toán
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/payment/{idOrder}")
    public ResponseEntity searchOrderKids(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @Valid OrderRequest request) {
        RequestUtils.getFirstRequest(principal, idOrder, request);
        ListKidsPackageForPaymentResponse response = fnOrderKidsService.searchKidsPackagePayment(principal, idOrder, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * thanh toán
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/payment/{idOrder}")
    public ResponseEntity orderKidsPayment(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @Valid @RequestBody OrderKidsPaymentRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, idOrder, request);
        boolean check = fnOrderKidsService.orderKidsPayment(principal, idOrder, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.PAYMENT_SUCCESS);
    }

    /**
     * in hóa đơn
     *
     * @param principal
     * @param request
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/print")
    public ResponseEntity orderPrint(@CurrentUser UserPrincipal principal, @Valid OrderRequest request, @Valid IdListRequest idList) throws IOException {
        RequestUtils.getFirstRequestExtend(principal, request, idList);
        OrderPrintResponse response = fnOrderKidsService.getPrintOrder(principal, request, idList);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/order/print/many")
    public ResponseEntity orderPrintMany(@CurrentUser UserPrincipal principal, @Valid @RequestBody List<OrderManyRequest> request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        List<OrderPrintResponse> response = fnOrderKidsService.getPrintOrderMany(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * in hóa đơn thu
     *
     * @param principal
     * @param request
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/print/out")
    public ResponseEntity orderPrintOut(@CurrentUser UserPrincipal principal, @Valid OrderRequest request, @Valid IdListRequest idList) throws IOException {
        RequestUtils.getFirstRequestExtend(principal, request, idList);
        OrderPrintResponse response = fnOrderKidsService.getPrintOrderOut(principal, request, idList);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * in hóa đơn chi
     *
     * @param principal
     * @param request
     * @param idList
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/print/in")
    public ResponseEntity orderPrintIn(@CurrentUser UserPrincipal principal, @Valid OrderRequest request, @Valid IdListRequest idList) throws IOException {
        RequestUtils.getFirstRequestExtend(principal, request, idList);
        OrderPrintResponse response = fnOrderKidsService.getPrintOrderIn(principal, request, idList);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tìm kiếm chi tiết một hóa đơn
     *
     * @param principal
     * @param idOrder
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/detail/{idOrder}")
    public ResponseEntity findOrderKidsDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder) {
        RequestUtils.getFirstRequest(principal, idOrder);
        ListOrderKidsDetailResponse response = fnOrderKidsService.findKidsPackagePaymentDetail(principal, idOrder);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * lưu mô tả hóa đơn
     *
     * @param principal
     * @param idOrder
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/description/{idOrder}")
    public ResponseEntity saveOrderKidsDescription(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder, @RequestBody DescriptionRequest request) {
        RequestUtils.getFirstRequest(principal, idOrder, request);
        boolean check = fnOrderKidsService.saveOrderKidsDescription(principal, idOrder, request);
        return NewDataResponse.setDataSave(check);
    }

    /**
     * tìm kiếm lịch sử các lần thanh toán của một khoản thu
     *
     * @param principal
     * @param idKidsPackage
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/history/kids-package/{idKidsPackage}")
    public ResponseEntity findKidsPackagePaymentDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idKidsPackage) {
        RequestUtils.getFirstRequest(principal, idKidsPackage);
        List<KidsPackagePaymentDetailResponse> responseList = exOrderHistoryKidsPackageService.findKidsPackagePaymentDetail(principal, idKidsPackage);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * lịch sửa thanh toán hóa đơn
     *
     * @param principal
     * @param idOrder
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/history/{idOrder}")
    public ResponseEntity findOrderKidsHistory(@CurrentUser UserPrincipal principal, @PathVariable Long idOrder) {
        RequestUtils.getFirstRequest(principal, idOrder);
        List<OrderKidsHistoryResponse> responseList = orderKidsHistoryService.findOrderKidsHistory(principal, idOrder);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * chi tiết các khoản mỗi lần thanh toán
     *
     * @param principal
     * @param idOrderHistory
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/history/detail/{idOrderHistory}")
    public ResponseEntity findOrderKidsHistoryDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idOrderHistory) {
        RequestUtils.getFirstRequest(principal, idOrderHistory);
        List<OrderKidsHistoryDetailResponse> responseList = exOrderHistoryKidsPackageService.findOrderKidsHistoryDetail(principal, idOrderHistory);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * thống kê chung toàn bộ học sinh trong trường
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/statistical/common")
    public ResponseEntity statisticalFinanceKids(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        FinanceKidsStatisticalResponse response = financeStatisticalKidsService.statisticalFinanceKids(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order/statistical/mini")
    public ResponseEntity statisticalFinanceKidsMini(@CurrentUser UserPrincipal principal, @Valid FinanceKidsStatisticalMiniRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        FinanceKidsStatisticalMiniResponse response = financeStatisticalKidsService.statisticalFinanceKidsMini(principal, request);
        return NewDataResponse.setDataSearch(response);
    }
    //end thu học phí

    //start quản lý ví

    /**
     * tìm kiếm danh sách ví
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet/search")
    public ResponseEntity searchWalletParent(@CurrentUser UserPrincipal principal, @Valid SearchKidsCommonRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<WalletParentResponse> responseList = walletParentService.searchWalletParent(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm danh sách ví chưa xác nhận
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet/search-unconfirm")
    public ResponseEntity searchWalletParentUnConfirm(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<WalletParentResponse> responseList = walletParentService.searchWalletParentUnConfirm(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * nạp rút tiền vào ví
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/wallet/history/create")
    public ResponseEntity createWalletParentHistory(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute WalletParentHistoryCreateRequest request) throws IOException, FirebaseMessagingException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = walletParentHistoryService.createWalletParentHistory(principal, request);
        String message = request.getCategory().equals(FinanceConstant.CATEGORY_IN) ? MessageWebConstant.WALLET_IN : MessageWebConstant.WALLET_OUT;
        return NewDataResponse.setDataCustom(check, message);
    }

    /**
     * xác nhận phụ huynh nạp tiền vào ví
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/wallet/history/confirm/{id}")
    public ResponseEntity confirmWalletParentHistory(@CurrentUser UserPrincipal principal, @PathVariable Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = walletParentHistoryService.confirmWalletParentHistory(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SUCCESS_CONFIRM);
    }

    /**
     * xóa lịch sử thanh toán nhà trường rút mà chưa được phụ huynh xác nhận
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/wallet/history/{id}")
    public ResponseEntity deleteWalletParentHistory(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean check = walletParentHistoryService.deleteWalletParentHistory(principal, id);
        return NewDataResponse.setDataDelete(check);
    }


    /**
     * tìm kiếm lịch sửa ví
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet-history/search")
    public ResponseEntity searchWalletParentHistory(@CurrentUser UserPrincipal principal, @Valid SearchWalletParentHistoryRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListWalletParentHistoryResponse response = walletParentHistoryService.searchWalletParentHistory(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tìm kiếm lịch sửa ví chưa xác nhận
     *
     * @param principal
     * @param idWalletParent
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet-history/search-false")
    public ResponseEntity searchWalletParentHistoryFalse(@CurrentUser UserPrincipal principal, @Valid @NotNull Long idWalletParent) {
        RequestUtils.getFirstRequest(principal);
        ListWalletParentHistoryResponse response = walletParentHistoryService.searchWalletParentHistoryFalse(principal, idWalletParent);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * thống kê ví của trường
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet/statistical/search")
    public ResponseEntity searchWalletParent(@CurrentUser UserPrincipal principal, @Valid WalletParentStatisticalRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListWalletParentStatisticalResponse response = walletParentService.searchWalletParentStatistical(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * lấy thông tin ngân hàng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/bank/info")
    public ResponseEntity getBank(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<BankBriefResponse> responseList = fnBankService.getBankBrief(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * tìm kiếm học sinh theo tên
     *
     * @param principal
     * @param fullName
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids/search")
    public ResponseEntity findKidsByName(@CurrentUser UserPrincipal principal, @RequestParam String fullName) {
        RequestUtils.getFirstRequest(principal, fullName);
        List<KidsInfoDataResponse> responseList = kidsService.findKidsByName(principal, fullName);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Xuất excel ds học sinh ví
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "wallet/excel")
    public ResponseEntity excelKidsWallet(@CurrentUser UserPrincipal principal, @Valid IdListRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<WalletParentResponse> walletParentResponseList = walletParentService.searchWalletParentExcel(request);
        List<ExcelResponse> responseList = walletParentService.exportWalletParentExcel(principal, walletParentResponseList);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Xuất excel ds học sinh có điều kiện
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "wallet/excel-proviso")
    public ResponseEntity excelKidsWalletProviso(@CurrentUser UserPrincipal principal, @Valid SearchKidsCommonExcelRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        List<WalletParentResponse> walletParentResponseList = walletParentService.searchWalletParentExcelProviso(request);
        List<ExcelResponse> responseList = walletParentService.exportWalletParentExcel(principal, walletParentResponseList);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "order/excel")
    public ResponseEntity excelExportOrderController(@CurrentUser UserPrincipal principal, @Valid OrderExcelRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ExcelDynamicResponse data = fnOrderKidsService.excelExportOrderService(request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "order/excel/now")
    public ResponseEntity excelExportOrderNowController(@CurrentUser UserPrincipal principal, @Valid OrderExcelRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ExcelDynamicResponse data = fnOrderKidsService.excelExportNowOrderService(request);
        return NewDataResponse.setDataSearch(data);
    }

}
