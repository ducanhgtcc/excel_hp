package com.example.onekids_project.controller.finance;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.request.common.ActiveRequest;
import com.example.onekids_project.request.finance.MonthRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendCreateRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.finance.PackageBriefResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendUpdateResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.extend.FnPackageDefaultExtendService;
import com.example.onekids_project.service.servicecustom.finance.extend.FnPackageExtendService;
import com.example.onekids_project.service.servicecustom.finance.extend.FnPackageKidsExtendService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-10-01 15:48
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/fn/fees/extend")
public class FnFeesExtendController {

    @Autowired
    private FnPackageExtendService fnPackageExtendService;
    @Autowired
    private FnPackageDefaultExtendService fnPackageDefaultExtendService;
    @Autowired
    private FnPackageKidsExtendService fnPackageKidsExtendService;

    /**
     * khoản đính kèm chung
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/package/add")
    public ResponseEntity getPackageForAdd(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<PackageBriefResponse> responseList = fnPackageExtendService.getPackageAdd(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/package/search")
    public ResponseEntity getPackageExtend(@CurrentUser UserPrincipal principal, @RequestParam String name) {
        RequestUtils.getFirstRequest(principal, name);
        List<PackageExtendResponse> responseList = fnPackageExtendService.getPackageExtend(principal.getIdSchoolLogin(), name);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/package/{id}")
    public ResponseEntity getPackageExtendById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        PackageExtendUpdateResponse response = fnPackageExtendService.getPackageExtendById(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/package")
    public ResponseEntity createPackageExtend(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageExtendCreateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        fnPackageExtendService.createPackageExtend(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/package")
    public ResponseEntity updatePackageExtend(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageExtendUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        fnPackageExtendService.updatePackageExtend(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/package/{id}")
    public ResponseEntity deletePackageExtendById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        fnPackageExtendService.deletePackageExtendById(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/package/active")
    public ResponseEntity activePackageExtendById(@CurrentUser UserPrincipal principal, @Valid @RequestBody ActiveRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        fnPackageExtendService.activePackageExtendById(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataActive(request.getActive());
    }

    /**
     * khoảng đính kèm mặc định
     */
    @RequestMapping(method = RequestMethod.POST, value = "/default/kid/manual")
    public ResponseEntity createDefaultExtendFromKid(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList) {
        RequestUtils.getFirstRequest(principal, idKidList);
        fnPackageDefaultExtendService.createDefaultExtendFromKid(principal.getIdSchoolLogin(), idKidList);
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/default/kid/active")
    public ResponseEntity activeDefaultExtendFromKid(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList, @RequestParam boolean active) {
        RequestUtils.getFirstRequestExtend(principal, idKidList, active);
        fnPackageDefaultExtendService.activeDefaultExtendFromKid(principal.getIdSchoolLogin(), idKidList, active);
        return NewDataResponse.setDataActive(active);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/default/kid")
    public ResponseEntity deleteDefaultExtendFromKid(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList) {
        RequestUtils.getFirstRequest(principal, idKidList);
        fnPackageDefaultExtendService.deleteDefaultExtendFromKid(principal.getIdSchoolLogin(), idKidList);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/default/{id}")
    public ResponseEntity getDefaultExtendById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        PackageExtendUpdateResponse response = fnPackageDefaultExtendService.getDefaultExtendById(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/default")
    public ResponseEntity updateDefaultExtend(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageExtendUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        fnPackageDefaultExtendService.updateDefaultExtend(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/default/package/manual")
    public ResponseEntity createDefaultExtendFromPackage(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idDefaultPackageList) {
        RequestUtils.getFirstRequest(principal, idDefaultPackageList);
        fnPackageDefaultExtendService.createDefaultExtendFromPackage(principal.getIdSchoolLogin(), idDefaultPackageList);
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/default/package/active")
    public ResponseEntity activeDefaultExtendFromPackage(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idDefaultPackageList, @RequestParam boolean active) {
        RequestUtils.getFirstRequestExtend(principal, idDefaultPackageList, active);
        fnPackageDefaultExtendService.activeDefaultExtendFromPackage(principal.getIdSchoolLogin(), idDefaultPackageList, active);
        return NewDataResponse.setDataActive(active);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/default/package")
    public ResponseEntity deleteDefaultExtendFromPackage(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idDefaultPackageList) {
        RequestUtils.getFirstRequest(principal, idDefaultPackageList);
        fnPackageDefaultExtendService.deleteDefaultExtendFromPackage(principal.getIdSchoolLogin(), idDefaultPackageList);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    /**
     * khoản đính kèm học sinh
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids/kid/manual")
    public ResponseEntity createKidsExtendFromKid(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList, @Valid DateNotNullRequest month) {
        RequestUtils.getFirstRequest(principal, idKidList);
        fnPackageKidsExtendService.createKidsExtendFromKid(principal.getIdSchoolLogin(), idKidList, month.getDate());
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/kids/kid/active")
    public ResponseEntity activeKidsExtendFromKid(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList, @RequestParam boolean active, @Valid DateNotNullRequest month) {
        RequestUtils.getFirstRequestExtend(principal, idKidList, active);
        fnPackageKidsExtendService.activeKidsExtendFromKid(principal.getIdSchoolLogin(), idKidList, active, month.getDate());
        return NewDataResponse.setDataActive(active);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/kids/kid")
    public ResponseEntity deleteKidsExtendFromKid(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList, @Valid DateNotNullRequest month) {
        RequestUtils.getFirstRequest(principal, idKidList);
        fnPackageKidsExtendService.deleteKidsExtendFromKid(principal.getIdSchoolLogin(), idKidList, month.getDate());
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kids/{id}")
    public ResponseEntity getKidsExtendById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        PackageExtendUpdateResponse response = fnPackageKidsExtendService.getKidsExtendById(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/kids")
    public ResponseEntity updateKidsExtend(@CurrentUser UserPrincipal principal, @Valid @RequestBody PackageExtendUpdateRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        fnPackageKidsExtendService.updateKidsExtend(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataUpdate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/kids/package/manual")
    public ResponseEntity createKidsExtendFromPackage(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidsPackageList) {
        RequestUtils.getFirstRequest(principal, idKidsPackageList);
        fnPackageKidsExtendService.createKidsExtendFromPackage(principal.getIdSchoolLogin(), idKidsPackageList);
        return NewDataResponse.setDataCreate(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/kids/package/active")
    public ResponseEntity activeKidsExtendFromPackage(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidsPackageList, @RequestParam boolean active) {
        RequestUtils.getFirstRequestExtend(principal, idKidsPackageList, active);
        fnPackageKidsExtendService.activeKidsExtendFromPackage(principal.getIdSchoolLogin(), idKidsPackageList, active);
        return NewDataResponse.setDataActive(active);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/kids/package")
    public ResponseEntity deleteKidsExtendFromPackage(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidsPackageList) {
        RequestUtils.getFirstRequest(principal, idKidsPackageList);
        fnPackageKidsExtendService.deleteKidsExtendFromPackage(principal.getIdSchoolLogin(), idKidsPackageList);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

}
