package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.birthdaymanagement.SearchWishSampleRequest;
import com.example.onekids_project.response.birthdaymanagement.ListWishesSampleResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.ParentBirthdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/web/wish")
public class WishesSampleController {
    private static final Logger logger = LoggerFactory.getLogger(WishesSampleController.class);
    @Autowired
    private ParentBirthdayService parentBirthdayService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findContent(@CurrentUser UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest) {
        ListWishesSampleResponse listWishesSampleResponse = parentBirthdayService.searchBirthdayKid1(principal, searchWishSampleRequest);
        return NewDataResponse.setDataSearch(listWishesSampleResponse);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/searchforparent")
    public ResponseEntity findContent1(@CurrentUser UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest) {
        ListWishesSampleResponse listWishesSampleResponse = parentBirthdayService.searchBirthdayParent(principal,searchWishSampleRequest);
        return NewDataResponse.setDataSearch(listWishesSampleResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchforteacher")
    public ResponseEntity findContent2(@CurrentUser UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest) {
        ListWishesSampleResponse listWishesSampleResponse = parentBirthdayService.searchBirthdayTeacher(principal, searchWishSampleRequest);
        return NewDataResponse.setDataSearch(listWishesSampleResponse);
    }
}
