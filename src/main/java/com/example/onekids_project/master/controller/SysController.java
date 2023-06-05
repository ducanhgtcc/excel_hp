package com.example.onekids_project.master.controller;

import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.system.SysConfigResponse;
import com.example.onekids_project.service.servicecustom.SysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("web/sys")
public class SysController {
    private static final Logger logger = LoggerFactory.getLogger(SysController.class);

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * tạo cấu hình hệ thống cho một trường
     *
     * @param idSchool
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create/{id}")
    public ResponseEntity createSysConfig(@PathVariable("id") Long idSchool) {
        try {
            SysConfigResponse sysConfigResponse = sysConfigService.createSysConfigForSchool(idSchool);
            if (sysConfigResponse == null) {
                logger.warn("Lỗi tạo cấu hình hệ thống cho một trường");
                return DataResponse.getData("Lỗi tạo cấu hình hệ thống cho một trường", HttpStatus.NOT_FOUND);
            }
            logger.info("Tạo cấu hình hệ thống cho một trường thành công");
            return DataResponse.getData(sysConfigResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Tạo cấu hình hệ thống cho một trường: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Tạo cấu hình hệ thống cho một trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
