package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.studentgroup.CreateKidsGroupRequest;
import com.example.onekids_project.request.studentgroup.TransferKidsGroupRequest;
import com.example.onekids_project.request.studentgroup.UpdateKidsGroupRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.kids.KidsInGroupResponse;
import com.example.onekids_project.response.studentgroup.CreateUpdateKidsGroupResponse;
import com.example.onekids_project.response.studentgroup.KidsGroupResponse;
import com.example.onekids_project.response.studentgroup.ListKidsGroupResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.KidsGroupService;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.example.onekids_project.validate.RequestValidate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/student-group")
public class KidsGroupController {

    private static final Logger logger = LoggerFactory.getLogger(KidsGroupController.class);

    @Autowired
    private KidsGroupService kidsGroupService;
    @Autowired
    private KidsService kidsService;

    /**
     * tìm kiếm tất cả nhóm học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, PageNumberWebRequest request) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        ListKidsGroupResponse listKidsGroupResponse = kidsGroupService.findAllKidsGroup(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(listKidsGroupResponse);
    }

    /**
     * tìm kiếm nhóm học sinh theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        Optional<KidsGroupResponse> kidsGroupResponse = kidsGroupService.findByIdKidsGroup(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(kidsGroupResponse);
    }

    /**
     * tạo nhóm học sinh
     *
     * @param principal
     * @param createKidsGroupRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateKidsGroupRequest createKidsGroupRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        CreateUpdateKidsGroupResponse createUpdateKidsGroupResponse = kidsGroupService.createKidsGroup(principal.getIdSchoolLogin(), createKidsGroupRequest);
        return NewDataResponse.setDataCreate(createUpdateKidsGroupResponse);
    }

    /**
     * cập nhật nhóm học sinh
     *
     * @param id
     * @param principal
     * @param updateKidsGroupRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateKidsGroupRequest updateKidsGroupRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        CreateUpdateKidsGroupResponse createUpdateKidsGroupResponse = kidsGroupService.updateKidsGroup(principal.getIdSchoolLogin(), updateKidsGroupRequest);
        return NewDataResponse.setDataUpdate(createUpdateKidsGroupResponse);
    }

    /**
     * chuyển đối nhóm học sinh
     *
     * @param id
     * @param principal
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean check = kidsGroupService.deleteKidsGroup(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataDelete(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transfer-group")
    public ResponseEntity updateTransferGroup(@CurrentUser UserPrincipal principal, @Valid @RequestBody TransferKidsGroupRequest transferKidsGroupRequest) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean check = kidsGroupService.insertTransferKidsGroup(principal.getIdSchoolLogin(), transferKidsGroupRequest);
        return NewDataResponse.setDataUpdate(check);
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/searchkidsgroup")
//    public ResponseEntity searchKidsGroup(@CurrentUser UserPrincipal principal, @Valid SearchStudentGroupRequest request) {
//        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
//        ListKidsStudentGroupResponse listKidsStudentGroupResponse = kidsGroupService.searchKids(principal, request);
//        return NewDataResponse.setDataSearch(listKidsStudentGroupResponse);
//    }

    /**
     * thêm học sinh vào nhóm dialog
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/add")
    public ResponseEntity searchAllKidForGroup(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkDataPlus(principal);
        List<KidsInGroupResponse> responseList = kidsService.findAllKidForGroup(principal);
        return NewDataResponse.setDataSearch(responseList);
    }
}
