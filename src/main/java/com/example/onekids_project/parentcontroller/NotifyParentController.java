package com.example.onekids_project.parentcontroller;

import com.example.onekids_project.entity.school.NotifySchoolAttachFile;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.repository.NotifySchoolAttachFileRepository;
import com.example.onekids_project.repository.UrlFileAppSendRepository;
import com.example.onekids_project.request.notifyschool.SearchNotifySchoolRequest;
import com.example.onekids_project.request.parentweb.NotifyParentWebRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.notifyschool.ListNotifySchoolResponse;
import com.example.onekids_project.response.parentweb.ListNotifyParentResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.notifyschool.NotifySchoolService;
import com.example.onekids_project.service.servicecustom.parentweb.ParentNotifyService;
import com.example.onekids_project.util.RequestUtils;
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
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/parent")
public class NotifyParentController {

    @Autowired
    private ParentNotifyService parentNotifyService;
    @Autowired
    private NotifySchoolService notifySchoolService;
    @Autowired
    private NotifySchoolAttachFileRepository notifySchoolAttachFileRepository;
    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/notify")
    public ResponseEntity searchNotifyParent(@CurrentUser UserPrincipal principal, @Valid NotifyParentWebRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListNotifyParentResponse response = parentNotifyService.findNotifyWeb(request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/notify")
    public ResponseEntity viewNotifyParent(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequest(principal, idList);
        parentNotifyService.viewNotifyWeb(idList);
        return NewDataResponse.setDataUpdate(true);
    }

    @RequestMapping(value = "/notify/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFileNotify(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws IOException {
        RequestUtils.getFirstRequest(principal, id);
        HttpHeaders responseHeader = new HttpHeaders();
        UrlFileAppSend dataFile = urlFileAppSendRepository.findById(id).orElseThrow();
        String fileLocal = StringUtils.isNotBlank(dataFile.getUrlLocalFile()) ? dataFile.getUrlLocalFile() : dataFile.getUrlLocalPicture();
        File file = new File(fileLocal);
        byte[] data = FileUtils.readFileToByteArray(file);
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
        responseHeader.setContentLength(data.length);
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/news")
    public ResponseEntity searchNotifySchool(@CurrentUser UserPrincipal principal, @Valid SearchNotifySchoolRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        request.setActive(true);
        ListNotifySchoolResponse data = notifySchoolService.searchNotifySchool(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(value = "/news/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFileNews(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws IOException {
        RequestUtils.getFirstRequest(principal, id);
        HttpHeaders responseHeader = new HttpHeaders();
        NotifySchoolAttachFile dataFile = notifySchoolAttachFileRepository.findById(id).orElseThrow();
        File file = new File(dataFile.getUrlLocal());
        byte[] data = FileUtils.readFileToByteArray(file);
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
        responseHeader.setContentLength(data.length);
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
    }


}
