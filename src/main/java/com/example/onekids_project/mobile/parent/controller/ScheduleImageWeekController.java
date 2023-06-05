package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ListScheduleFileParentResponse;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleImageParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleImageFileParentService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/parent/schedule/image")
public class ScheduleImageWeekController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ScheduleImageFileParentService scheduleImageFileParentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findImageWeek(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        try {
            CommonValidate.checkDataParent(principal);
            ScheduleImageParentResponse scheduleImageParentResponse = scheduleImageFileParentService.findImageWeek(principal, dateNotNullRequest.getDate());
            if (scheduleImageParentResponse == null) {
                return NewDataResponse.setDataCustom(scheduleImageParentResponse,"Không tìm thấy dữ liệu trong DB");
            }
            return NewDataResponse.setDataSearch(scheduleImageParentResponse);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm hình ảnh thời khóa biểu tuần cho phụ huynh" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm ảnh thời khóa biểu tuần cho phụ huynh", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file")

    // không cần truyền vào dữ liệu ngày. Trường hợp ngày, sẽ trả về ngày bé hơn ngày truyền vào để phân trang
    public ResponseEntity findFileWeek(@CurrentUser UserPrincipal principal, DateNotNullRequest dateNotNullRequest) {
        try {
            CommonValidate.checkDataParent(principal);
            Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
            ListScheduleFileParentResponse data = scheduleImageFileParentService.findFileAllWeek(principal, pageable, dateNotNullRequest.getDate());
            if (CollectionUtils.isEmpty(data.getDataList())) {
                return NewDataResponse.setDataCustom(data,"Không tìm thấy dữ liệu trong DB");
            }
            return NewDataResponse.setDataSearch(data);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm file thời khóa biểu tuần cho phụ huynh" + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm file thời khóa biểu tuần cho phụ huynh", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}