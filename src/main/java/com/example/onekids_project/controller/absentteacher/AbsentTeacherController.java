package com.example.onekids_project.controller.absentteacher;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.employee.AbsentTeacherAttackFile;
import com.example.onekids_project.repository.AbsentTeacherAttachFileRepository;
import com.example.onekids_project.request.absentteacher.AbsentTeacherRequest;
import com.example.onekids_project.request.absentteacher.SearchAbsentTeacherRequest;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.response.absentteacher.AbsentTeacherDetailResponse;
import com.example.onekids_project.response.absentteacher.ListAbsentTeacherResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.absentteacher.AbsentTeacherService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.List;

/**
 * date 2021-05-21 2:26 PM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping(value = "/web/absent-teacher")
public class AbsentTeacherController {

    @Autowired
    private AbsentTeacherService absentTeacherService;

    @Autowired
    private AbsentTeacherAttachFileRepository absentTeacherAttachFileRepository;

    /**
     * Tìm kiếm all absent teacher
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findMessage(@CurrentUser UserPrincipal principal, SearchAbsentTeacherRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ListAbsentTeacherResponse response = absentTeacherService.searchAbsentTeacher(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Tìm kiếm chi tiết absent teacher
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findByIdAbsentTeacher(@CurrentUser UserPrincipal principal, @PathVariable Long id){
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        AbsentTeacherDetailResponse response = absentTeacherService.findByIdAbsentTeacher(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Đọc all absent teacher đã gửi
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/read/many")
    public ResponseEntity updateRead(@CurrentUser UserPrincipal principal, @RequestBody List<Long> request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean check = absentTeacherService.updateRead(request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.READ_SUCCESS);
    }

    /**
     * Xác nhận absent teacher theo id
     * @param id
     * @param principal
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity confirmTeacherReply(@PathVariable Long id, @CurrentUser UserPrincipal principal) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        boolean check = absentTeacherService.confirmReply(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SUCCESS_CONFIRM);
    }

    /**
     * Xác nhận all absent teacher
     * @param request
     * @param principal
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/many")
    public ResponseEntity updateConfirm(@RequestBody List<Long> request, @CurrentUser UserPrincipal principal) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        boolean check = absentTeacherService.updateConfirmMany(request, principal);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.SUCCESS_CONFIRM);
    }

    /**
     * Nhà trường phản hồi
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/save/reply")
    public ResponseEntity saveReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean response = absentTeacherService.updateAbsentTeacher(principal, request);
        return NewDataResponse.setDataCustom(response, MessageWebConstant.SUCCESS_SAVE_REPLY);
    }

    /**
     * Thu hồi, hủy thu hồi
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/plus")
    public ResponseEntity revokePlus(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean response = absentTeacherService.revokePlus(principal, request);
        String message = request.getStatus() ? MessageWebConstant.SUCCESS_REVOKE : MessageWebConstant.SUCCESS_UNREVOKE;
        return NewDataResponse.setDataCustom(response, message);
    }

    /**
     * Download file trong detail absent teacher
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/absent-download/{id}", method = RequestMethod.GET)
    public ResponseEntity downloadFileAbsentTeacher(@PathVariable Long id) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        AbsentTeacherAttackFile absentTeacherAttackFile = absentTeacherAttachFileRepository.findByIdAndDelActiveTrue(id).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(absentTeacherAttackFile.getUrlLocal())) {
                file = new File(absentTeacherAttackFile.getUrlLocal());
            } else if (StringUtils.isNotBlank(absentTeacherAttackFile.getUrlLocal())) {
                file = new File(absentTeacherAttackFile.getUrlLocal());
            }
            byte[] data = FileUtils.readFileToByteArray(file);
            // Set mimeType trả về
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // Thiết lập thông tin trả về
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.fillInStackTrace(), responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
