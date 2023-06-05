package com.example.onekids_project.mobile.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.master.response.ProfileMobileResponse;
import com.example.onekids_project.mobile.request.ProfileMobielRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/mob/user/profile")
public class ProfileMobileController {
    @Autowired
    private MaUserService maUserService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProfile(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        ProfileMobileResponse data = maUserService.findProfileMobile(principal);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_PROFILE);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateProfile(@CurrentUser UserPrincipal principal, @Valid @RequestBody ProfileMobielRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean data = maUserService.updateProfileMobile(principal, request);
        return NewDataResponse.setDataCustom(data, MessageConstant.UPDATE_PROFILE);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/avatar")
    public ResponseEntity saveAvatar(@CurrentUser UserPrincipal principal, @ModelAttribute MultipartFile multipartFile) throws IOException {
        RequestUtils.getFirstRequest(principal);
        boolean checkSave = maUserService.updateAvatarMobile(principal, multipartFile);
        return NewDataResponse.setDataCustom(checkSave, MessageConstant.UPDATE_AVATAR);
    }
}
