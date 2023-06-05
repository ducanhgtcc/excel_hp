package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.GetLinkPlusRequest;
import com.example.onekids_project.mobile.teacher.response.jsonattendance.ListUrlJsonResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.UrlJsonMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/mob/plus")
public class GetLinkPlusController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UrlJsonMobileService urlJsonMobileService;

    /**
     * Lấy link nhận dạng
     *
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getlink")
    public ResponseEntity getUrl(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid GetLinkPlusRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListUrlJsonResponse listUrlJsonResponse = urlJsonMobileService.searchUrlForPlus(principal, request);
        return NewDataResponse.setDataCustom(listUrlJsonResponse, MessageConstant.FIND_JSON_URL);
    }
}
