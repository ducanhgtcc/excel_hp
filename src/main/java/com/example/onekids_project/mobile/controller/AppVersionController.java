package com.example.onekids_project.mobile.controller;

import com.example.onekids_project.mobile.response.AppVersionResponse;
import com.example.onekids_project.mobile.service.servicecustom.AppVersionService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mob")
public class AppVersionController {

    private static final Logger logger = LoggerFactory.getLogger(AppVersionController.class);

    @Autowired
    private AppVersionService versionService;

    @RequestMapping(method = RequestMethod.GET, value = "/app-version")
    public ResponseEntity searchDate() {
        try {
            List<AppVersionResponse> appVersionResponseList = versionService.findAllAppVersion();
            if (CollectionUtils.isEmpty(appVersionResponseList)) {
                logger.warn("Không có app version");
                return DataResponse.getData("Không có app version", HttpStatus.ACCEPTED);
            }
            logger.info("Tìm kiếm app version thành công");
            return DataResponse.getData(appVersionResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi Tìm kiếm app version" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi Tìm kiếm app version", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
