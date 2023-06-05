package com.example.onekids_project.mobile.teacher.controller;


import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.qualitykid.KidsHeightWeightTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsExtraQualityResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsHeightWeightTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListHeightWeightSampleTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListQualityKidTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.QualityKidService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/mob/teacher/quality-kid")
public class QualityKidTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    QualityKidService qualityKidService;

    /**
     * tìm kiếm danh sách sức khỏe học sinh của một lớp
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findQualityKidOfClass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        ListQualityKidTeacherResponse listQualityKidTeacherResponse = qualityKidService.findQualityKidOfClass(principal);
        return NewDataResponse.setDataSearch(listQualityKidTeacherResponse);
    }

    /**
     * tìm kiếm mở rộng sức khỏe của một học sinh
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity findQualityKidExtral(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal,id);
        KidsExtraQualityResponse kidsExtraQuality = qualityKidService.findKidsExtraQuality(principal, id);
        return NewDataResponse.setDataSearch(kidsExtraQuality);
    }

    /**
     * xóa
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity deleteHeightWeight(@CurrentUser UserPrincipal principal, @RequestParam(required = false) Long idHeight, @RequestParam(required = false) Long idWeight) {
        RequestUtils.getFirstRequest(principal,idHeight);
        boolean checkDelete = qualityKidService.deleteHeightWeight(idHeight, idWeight);
        return NewDataResponse.setDataDelete(MessageConstant.DELETE_HEIGHT_WEIGHT);

    }

    /**
     * tạo mới
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createHeightWeight(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid KidsHeightWeightTeacherRequest kidsHeightWeightParentRequest) {
        RequestUtils.getFirstRequest(principal,kidsHeightWeightParentRequest);
        KidsHeightWeightTeacherResponse kidsHeightWeightTeacherResponse = qualityKidService.saveHeightWeightTeacher(principal, kidsHeightWeightParentRequest);
        return NewDataResponse.setDataCustom(kidsHeightWeightTeacherResponse, MessageConstant.CREATE_HEIGHT_WEIGHT);
    }

    /**
     * tìm kiếm cân nặng tiêu chuẩn
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sample{id}")
    public ResponseEntity createHeightWeight(@CurrentUser UserPrincipal principal, @NotBlank Long id) {
        RequestUtils.getFirstRequest(principal,id);
        ListHeightWeightSampleTeacherResponse dataList = qualityKidService.findKidSample(id);
        return NewDataResponse.setDataSearch(dataList);
    }


}
