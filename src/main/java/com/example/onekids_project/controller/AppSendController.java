package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.dto.AppSendDTO;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.mobile.teacher.response.message.MessageTeacheConfirmResponse;
import com.example.onekids_project.repository.UrlFileAppSendRepository;
import com.example.onekids_project.request.AppSend.*;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ListAppSendResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.notifihistory.ReiceiversResponeHistoru;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/appsend")
public class AppSendController {
    private static final Logger logger = LoggerFactory.getLogger(AppSendController.class);
    @Autowired
    private AppSendService appSendService;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
//        Long idSchoolLogin = principal.getIdSchoolLogin();
//        int pageNumber = ConvertData.getPageNumber(baseRequest.getPageNumber());
//        if (pageNumber == -1) {
//            logger.error("Số trang không hợp lệ");
//            return DataResponse.getData("Số trang không hợp lệ", HttpStatus.BAD_REQUEST);
//        }
//        String sendType = "sys";
//        Pageable pageable = PageRequest.of(pageNumber, AppConstant.MAX_PAGE_ITEM);
//        ListAppSendResponse listAppSendResponse = appSendService.findAllNotif(idSchoolLogin, pageable, principal.getId(), sendType);
//        if (listAppSendResponse == null) {
//            logger.warn("Không có ");
//            return DataResponse.getData("Không có ", HttpStatus.NOT_FOUND);
//        }
//        logger.info("Tìm kiếm tất cả thành công");
//        return DataResponse.getData(listAppSendResponse, HttpStatus.OK);
//    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<ReiceiversResponeHistoru> appSendDTOOptional = appSendService.findByIdAppsend(principal, idSchoolLogin, id);
        return NewDataResponse.setDataSearch(appSendDTOOptional);

    }

    @RequestMapping(value = "/appsendowload/{idUrlFileAppsend}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlFileAppsend") Long idUrlFileAppsend) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        UrlFileAppSend urlFileAppSend = urlFileAppSendRepository.findById(idUrlFileAppsend).get();
        File file = null;
        if (StringUtils.isNotBlank(urlFileAppSend.getUrlLocalFile())) {
            file = new File(urlFileAppSend.getUrlLocalFile());
        } else if (StringUtils.isNotBlank(urlFileAppSend.getUrlLocalPicture())) {
            file = new File(urlFileAppSend.getUrlLocalPicture());
        }

        byte[] data = FileUtils.readFileToByteArray(file);
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
        responseHeader.setContentLength(data.length);
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);

    }

    @RequestMapping(value = "/appsendowloadBirthday/{idUrlFileAppsend}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download3(HttpServletRequest request, @PathVariable("idUrlFileAppsend") Long idUrlFileAppsend) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        UrlFileAppSend urlFileAppSend = urlFileAppSendRepository.findById(idUrlFileAppsend).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(urlFileAppSend.getUrlLocalFile())) {
                file = new File(urlFileAppSend.getUrlLocalFile());
            } else if (StringUtils.isNotBlank(urlFileAppSend.getUrlLocalPicture())) {
                file = new File(urlFileAppSend.getUrlLocalPicture());
            }

            byte[] data = FileUtils.readFileToByteArray(file);
            // Set mimeType trả về
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // Thiết lập thông tin trả về
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<InputStreamResource>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/a/{id}")
    public ResponseEntity findByIda(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<AppSendDTO> appSendDTOOptional = appSendService.findByIdAppsenda(idSchoolLogin, principal, id);
        return NewDataResponse.setDataSearch(appSendDTOOptional);

    }

    /**
     * Danh sách thông báo hệ thống
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findContent(@CurrentUser UserPrincipal principal, SearchContentRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAppSendResponse listAppSendResponse = appSendService.searchNotifi(idSchoolLogin, principal, request);
        return NewDataResponse.setDataSearch(listAppSendResponse);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-approved-smsapp")
    public ResponseEntity updateManyApproved(@CurrentUser UserPrincipal principal, @RequestBody List<SmsAppRequest1> smsAppRequests, Long id) {
        RequestUtils.getFirstRequest(principal, id);
        SmsAppRequest1 smsAppRequest = appSendService.updateManyapproved(id, smsAppRequests);
        return NewDataResponse.setDataUpdate(smsAppRequest);
    }

    /**
     * duyệt đã đọc cho thông báo hệ thống
     *
     * @param principal
     * @param appSendRequests
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-read")
    public ResponseEntity updateRead(@CurrentUser UserPrincipal principal, @RequestBody List<AppSendRequest> appSendRequests, Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = appSendService.updateRead(id, appSendRequests);
        return NewDataResponse.setDataCustom(check, "Duyệt đã đọc thành công");
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateKidsBirthdayRequest kidsBirthdayEditRequest) {
        RequestUtils.getFirstRequest(principal, kidsBirthdayEditRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!kidsBirthdayEditRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + kidsBirthdayEditRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        AppSendResponse appSendResponse = appSendService.updateKidsBirthdayAppsend(principal.getIdSchoolLogin(), principal, kidsBirthdayEditRequest);
        return NewDataResponse.setDataUpdate(appSendResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppsend(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = appSendService.deleteAppsend(principal, idSchoolLogin, id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/approved/{id}")
    public ResponseEntity ApprovedHistoryappsend(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        MessageTeacheConfirmResponse messageTeacheConfirmResponse = appSendService.appovedHistoryAppsend(principal, id);
        return NewDataResponse.setDataCustom(messageTeacheConfirmResponse, MessageConstant.MESSAGE_CONFIRM);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity RevokeHistoryappsend(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        MessageTeacheConfirmResponse messageTeacheConfirmResponse = appSendService.revokeAppSendhistory(principal, id);
        return NewDataResponse.setDataCustom(messageTeacheConfirmResponse, MessageConstant.MESSAGE_CONFIRM);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/unrevoke/{id}")
    public ResponseEntity UnRevokeHistoryappsend(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        MessageTeacheConfirmResponse messageTeacheConfirmResponse = appSendService.unrevokeAppSendhistory(principal, id);
        return NewDataResponse.setDataCustom(messageTeacheConfirmResponse, MessageConstant.MESSAGE_CONFIRM);
    }

}
