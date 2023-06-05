package com.example.onekids_project.controller;

import com.example.onekids_project.master.request.CreateAppSendNotify;
import com.example.onekids_project.master.request.UpdateAppSendNotify;
import com.example.onekids_project.master.response.ReceiversResponse;
import com.example.onekids_project.master.service.ReceiversService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/receivers-notify")
public class ReceiversController {

    private static final Logger logger = LoggerFactory.getLogger(ReceiversController.class);

    @Autowired
    private ReceiversService receiversService;

    @GetMapping
    public ResponseEntity getAllReceiversNotify(@RequestParam("idAppSend") Long idAppSend) {
        List<ReceiversResponse> receiversResponseList = receiversService.findAllReceivers(idAppSend);
        return DataResponse.getData(receiversResponseList, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity createAppSendNotify(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute CreateAppSendNotify createAppSendNotify) {
        try {
            /*boolean checkCreate = appSendService.createAppSendNotify(principal, createAppSendNotify);
            logger.info("Tạo thông báo thành công");
            return DataResponse.getData(checkCreate, HttpStatus.OK);*/
            return null;
        } catch (Exception e) {
            logger.error("Exception tạo thông báo cho  đại lý: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi tạo thông báo cho đại lý", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity updateAppSendNotify(@CurrentUser UserPrincipal userPrincipal, @Valid @ModelAttribute UpdateAppSendNotify updateAppSendNotify) {
        try {
            /*boolean checkUpdate = appSendService.updateAppSendNotify(userPrincipal,updateAppSendNotify);
            if (!checkUpdate) {
                logger.error("Lỗi cập nhật thông báo");
                return ErrorResponse.errorData("Không thể cập nhật thông báo", "Không thể cập nhật thông báo", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("Cập nhật thông báo thành công");
            return DataResponse.getData(checkUpdate, HttpStatus.OK);*/
            return null;
        } catch (Exception e) {
            logger.error("Exception cập nhật thông báo: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi cập nhật thông báo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        try {
            boolean checkDelete = receiversService.deleteById(id);
            if (!checkDelete) {
                logger.error("Lỗi xóa thông báo người nhận");
                return ErrorResponse.errorData("Lỗi xóa thông báo người nhận", "Thông báo không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("Xóa thông báo người nhận thành công");
            return DataResponse.getData("Xóa thông báo người nhận thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception xóa thông báo theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi xóa thông báo người nhận", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("multi")
    public ResponseEntity deleteMulti(@RequestBody List<Long> idList) {
        try {
            boolean checkDelete = receiversService.deleteByMultiId(idList);
            if (!checkDelete) {
                logger.error("Lỗi xóa thông báo người nhận");
                return ErrorResponse.errorData("Lỗi xóa thông báo người nhận", "Thông báo không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("Xóa thông báo người nhận thành công");
            return DataResponse.getData("Xóa thông báo người nhận thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception xóa thông báo theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi xóa thông báo người nhận", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity updateRevoke(@PathVariable(name = "id") Long id) {
        try {
            boolean checkDelete = receiversService.revokeReceiversNotify(id);
            if (!checkDelete) {
                logger.error("Lỗi cập nhật thông báo người nhận");
                return ErrorResponse.errorData("Lỗi xóa thông báo người nhận", "Thông báo không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("Cập nhật thông báo người nhận thành công");
            return DataResponse.getData("Cập nhật thông báo người nhận thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Cập nhật thông báo theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi Cập nhật thông báo người nhận", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/approved/{id}")
    public ResponseEntity updateApproved(@PathVariable(name = "id") Long id) {
        try {
            boolean checkDelete = receiversService.approvedReceiversNotify(id);
            if (!checkDelete) {
                logger.error("Lỗi duyệt tin nhắn");
                return ErrorResponse.errorData("Lỗi duyệt tin nhắn", "Thông báo không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("Duyệt tin nhắn thành công");
            return DataResponse.getData("Duyệt tin nhắn thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Cập nhật thông báo theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi Cập nhật thông báo người nhận", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateMultiRevoke(@RequestBody List<Long> idList) {
        try {
            boolean checkDelete = receiversService.revokeMultiReceiversNotify(idList);
            if (!checkDelete) {
                logger.error("Lỗi cập nhật thông báo người nhận");
                return ErrorResponse.errorData("Lỗi xóa thông báo người nhận", "Thông báo không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("Cập nhật thông báo người nhận thành công");
            return DataResponse.getData("Cập nhật thông báo người nhận thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Cập nhật thông báo theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi Cập nhật thông báo người nhận", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-multi-revoke")
    public ResponseEntity updateMultiRevokeShow(@RequestBody List<Long> idList) {
        try {
            boolean checkDelete = receiversService.revokeMultiReceiversNotifyShow(idList);
            if (!checkDelete) {
                logger.error("Lỗi cập nhật thông báo người nhận");
                return ErrorResponse.errorData("Lỗi xóa thông báo người nhận", "Thông báo không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("Cập nhật thông báo người nhận thành công");
            return DataResponse.getData("Cập nhật thông báo người nhận thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Cập nhật thông báo theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi Cập nhật thông báo người nhận", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
