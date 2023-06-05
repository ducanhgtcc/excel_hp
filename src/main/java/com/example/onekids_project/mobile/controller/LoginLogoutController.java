package com.example.onekids_project.mobile.controller;

import com.example.onekids_project.master.request.device.DeviceWebRequest;
import com.example.onekids_project.mobile.request.DeviceLoginMobileRequest;
import com.example.onekids_project.mobile.response.AppVersionResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DeviceService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob")
public class LoginLogoutController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(method = RequestMethod.PUT, value = "/login/device")
    public ResponseEntity udpateDeviceLogin(@CurrentUser UserPrincipal principal, @Valid @RequestBody DeviceLoginMobileRequest deviceLoginMobileRequest) {
        try {
            RequestUtils.getFirstRequestPlus(principal);
           boolean checkUpdate = deviceService.saveDeviceLoginMobile(principal.getId(), deviceLoginMobileRequest);
            logger.info("Lưu device thành công");
            return DataResponse.getData(checkUpdate, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi lưu device" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi lưu device", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * save device logout
     * @param principal
     * @param deviceWebRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/logout/device")
    public ResponseEntity saveDeviceLogout(@CurrentUser UserPrincipal principal, @Valid @RequestBody DeviceWebRequest deviceWebRequest) {
        try {
            RequestUtils.getFirstRequestPlus(principal);
            deviceService.saveDeviceLogout(principal.getId(), deviceWebRequest);
            logger.info("Logout device login thành công");
            return DataResponse.getData("Logout device login thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi lưu device login" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi lưu device login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
