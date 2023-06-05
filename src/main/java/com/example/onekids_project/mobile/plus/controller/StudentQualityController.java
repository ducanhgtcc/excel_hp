package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.kidsQuality.CreateKidsHeightWeightPlusRequest;
import com.example.onekids_project.mobile.plus.request.kidsQuality.SearchKidsQualityPlusRequest;
import com.example.onekids_project.mobile.plus.response.kidsQuality.KidsHeightWeightPlusResponse;
import com.example.onekids_project.mobile.plus.response.kidsQuality.ListKidsQualityPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsQualityPlusService;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsExtraQualityResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListHeightWeightSampleTeacherResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/mob/plus/kids-quality")
public class

StudentQualityController {

    @Autowired
    private KidsQualityPlusService kidsQualityPlusService;

    /**
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findQualityKidOfClass(@CurrentUser UserPrincipal principal, @Valid SearchKidsQualityPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListKidsQualityPlusResponse listKidsQualityPlusResponse = kidsQualityPlusService.findQualityKid(principal, request);
        return NewDataResponse.setDataSearch(listKidsQualityPlusResponse);
    }

    /**
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity findQualityKidExtral(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        KidsExtraQualityResponse kidsExtraQuality = kidsQualityPlusService.findKidsExtraQuality(principal, id);
        return NewDataResponse.setDataSearch(kidsExtraQuality);
    }

    /**
     * @param principal
     * @param idHeight
     * @param idWeight
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public ResponseEntity deleteHeightWeight(@CurrentUser UserPrincipal principal, @RequestParam(required = false) Long idHeight, @RequestParam(required = false) Long idWeight) {
        RequestUtils.getFirstRequestPlus(principal);
        boolean checkDelete = kidsQualityPlusService.deleteHeightWeight(idHeight, idWeight);
        return NewDataResponse.setDataDelete(MessageConstant.DELETE_HEIGHT_WEIGHT);
    }

    /**
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createHeightWeight(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid CreateKidsHeightWeightPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        KidsHeightWeightPlusResponse kidsHeightWeightPlusResponse = kidsQualityPlusService.saveHeightWeightPlus(principal, request);
        return NewDataResponse.setDataCustom(kidsHeightWeightPlusResponse, MessageConstant.CREATE_HEIGHT_WEIGHT);
    }

    /**
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sample/{id}")
    public ResponseEntity findsample(@CurrentUser UserPrincipal principal, @NotBlank Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        ListHeightWeightSampleTeacherResponse dataList = kidsQualityPlusService.findKidSamplePlus(id);
        return NewDataResponse.setDataSearch(dataList);
    }


}
