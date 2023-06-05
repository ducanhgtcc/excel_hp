package com.example.onekids_project.master.controller;

import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.master.request.CreateAppSendNotify;
import com.example.onekids_project.master.request.SearchAppSendRequest;
import com.example.onekids_project.master.request.UpdateAppSendNotify;
import com.example.onekids_project.master.response.notify.ListNotifyAdminResponse;
import com.example.onekids_project.repository.UrlFileAppSendRepository;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;

@RestController
@RequestMapping("/web/appsend-notify")
public class AppSendNotifyController {

    private static final Logger logger = LoggerFactory.getLogger(AppSendNotifyController.class);

    @Autowired
    private AppSendService appSendService;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @GetMapping(value = "/search")
    public ResponseEntity getAllAppSendNotify(@CurrentUser UserPrincipal principal, @Valid SearchAppSendRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListNotifyAdminResponse response = appSendService.findAllAppSendNotify(request);
        return NewDataResponse.setDataSearch(response);
    }

    @PostMapping
    public ResponseEntity createAppSendNotify(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute CreateAppSendNotify createAppSendNotify) {
        try {
            boolean checkCreate = appSendService.createAppSendNotify(principal, createAppSendNotify);
            logger.info("Tạo thông báo thành công");
            return DataResponse.getData(checkCreate, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception tạo thông báo cho  đại lý: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi tạo thông báo cho đại lý", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity updateAppSendNotify(@CurrentUser UserPrincipal userPrincipal, @Valid @ModelAttribute UpdateAppSendNotify updateAppSendNotify) {
        try {
            boolean checkUpdate = appSendService.updateAppSendNotify(userPrincipal, updateAppSendNotify);
            if (!checkUpdate) {
                logger.error("Lỗi cập nhật thông báo");
                return ErrorResponse.errorData("Không thể cập nhật thông báo", "Không thể cập nhật thông báo", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("Cập nhật thông báo thành công");
            return DataResponse.getData(checkUpdate, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception cập nhật thông báo: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi cập nhật thông báo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/multi")
    public ResponseEntity deleteAppSendNotify(@RequestBody Long[] id) {
        try {
            boolean checkDelete = appSendService.deleteAppSendNotify(id);
            if (!checkDelete) {
                logger.error("lỗi xóa đại lý");
                return ErrorResponse.errorData("Lỗi xóa đại lý", "Đại lý này không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("xóa đại lý thành công");
            return DataResponse.getData("Xóa đại lý thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception xóa đại lý theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi xóa đại lý", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/download/{idUrlAppSend}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlAppSend") Long idUrlAppSend) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        UrlFileAppSend urlFileAppSend = urlFileAppSendRepository.findById(idUrlAppSend).get();
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

}
