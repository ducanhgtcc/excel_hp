package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.response.KidsReco.ListKidsRecoResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsRecoPlusService;
import com.example.onekids_project.mobile.teacher.request.identifykid.IdentityKidRequest;
import com.example.onekids_project.mobile.teacher.response.identitykid.IdentifyKid;
import com.example.onekids_project.mobile.teacher.response.identitykid.InfoIdentityKid;
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
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/mob/plus/reco")
public class KidsRecoController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KidsRecoPlusService kidsRecoPlusService;

    /**
     * Danh sách học sinh lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class")
    public ResponseEntity getUrl(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        ListKidsRecoResponse recoResponse = kidsRecoPlusService.searchClass(principal);
        return NewDataResponse.setDataSearch(recoResponse);
    }


    /**
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findDetailAppsend(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        List<IdentifyKid> listKids = kidsRecoPlusService.getKidsClass(principal, id);
        return NewDataResponse.setDataCustom(listKids, MessageConstant.FIND_KID_LIST);
    }

    /**
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid/{id}")
    public ResponseEntity getKidIdentity(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        List<InfoIdentityKid> infoIdentityKidList = kidsRecoPlusService.getKidIdentity(principal, id);
        return NewDataResponse.setDataCustom(infoIdentityKidList, MessageConstant.FIND_KID);
    }

    /**
     * @param principal
     * @param identityKidRequest
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity updateDelInsKidIdentity(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid IdentityKidRequest identityKidRequest) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, identityKidRequest);
        kidsRecoPlusService.updateDelInsKidIdentity(principal, identityKidRequest);
        return NewDataResponse.setMessage(MessageConstant.CREATE_IDENTIFY);
    }
}
