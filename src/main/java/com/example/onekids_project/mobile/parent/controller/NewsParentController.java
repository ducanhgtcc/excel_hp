package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.NewsParentService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mob/parent/news")
public class NewsParentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NewsParentService newsParentService;

    /**
     * find new for parents
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchEvaluateKids(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataParent(principal);
        try {
            List<NewsMobileResponse> dataList = newsParentService.findNews();
            return NewDataResponse.setDataSearch(dataList);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm tin tức cho phụ huynh" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm tin tức cho phụ huynh", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
