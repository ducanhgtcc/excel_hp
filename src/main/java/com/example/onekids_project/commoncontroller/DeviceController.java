package com.example.onekids_project.commoncontroller;

import com.example.onekids_project.master.request.device.DeviceWebRequest;
import com.example.onekids_project.mobile.service.servicecustom.DeviceCamService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DeviceService;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/web/device")
public class DeviceController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceCamService deviceCamService;

    /**
     * save device login
     *
     * @param principal
     * @param deviceWebRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/login")
    public ResponseEntity saveDeviceLogin(@CurrentUser UserPrincipal principal, @Valid @RequestBody DeviceWebRequest deviceWebRequest) {
        deviceService.saveDeviceLogin(principal.getId(), deviceWebRequest);
        return DataResponse.getData("Login device thành công", HttpStatus.OK);
    }

    /**
     * save device login
     * thực hiện bởi chính người dùng
     *
     * @param principal
     * @param deviceWebRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/logout")
    public ResponseEntity saveDeviceLogout(@CurrentUser UserPrincipal principal, @Valid @RequestBody DeviceWebRequest deviceWebRequest) {
        deviceService.saveDeviceLogout(principal.getId(), deviceWebRequest);
        return DataResponse.getData("Đăng xuất thiết bị thành công", HttpStatus.OK);
    }

    /**
     * đăng xuât thiết bị thực hiện từ giao diện quản trị viên
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/logout/admin/{id}")
    public ResponseEntity saveDeviceLogoutAdmin(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        CommonValidate.checkDataAdmin(principal);
        deviceService.saveDeviceLogoutAdmin(id);
        return NewDataResponse.setDataCustom(true, "Đăng xuất thành công");
    }

    /**
     * đăng xuât thiết bị thực hiện từ giao diện quản trị viên
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/logout/onecam/{id}")
    public ResponseEntity saveDeviceLogoutOneCamAdmin(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        CommonValidate.checkDataAdmin(principal);
        deviceCamService.forceLogoutCame(id);
        return NewDataResponse.setDataCustom(true, "Đăng xuất thiết bị thành công");
    }
}
