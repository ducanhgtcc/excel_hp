package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.parent.response.menuclass.ListMenuFileParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuDateParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuImageWeekParentResponse;
import com.example.onekids_project.mobile.parent.response.menuclass.MenuWeekParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuDateParentService;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuImageFileParentService;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuOrdinalParentService;
import com.example.onekids_project.mobile.parent.service.servicecustom.ClassMenuWeekParentService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mob/parent/menu")
public class MenuClassMobileController {

    @Autowired
    private ClassMenuDateParentService classMenuDateParentService;

    @Autowired
    private ClassMenuWeekParentService classMenuWeekParentService;

    @Autowired
    private ClassMenuImageFileParentService classMenuImageFileParentService;

    @Autowired
    private ClassMenuOrdinalParentService classMenuOrdinalParentService;

    @RequestMapping(method = RequestMethod.GET, value = "/ordinal")
    public ResponseEntity ordinalMenuClass(@CurrentUser UserPrincipal principal, @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        RequestUtils.getFirstRequest(principal,date);
        CommonValidate.checkDataParent(principal);
        List<String> response = classMenuOrdinalParentService.classMenuOrdinalParent(principal, date);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/date")
    public ResponseEntity searchScheduleDate(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        List<MenuDateParentResponse> data = classMenuDateParentService.findDateMenu(principal, dateNotNullRequest.getDate());
        if (CollectionUtils.isEmpty(data)) {
            return NewDataResponse.setDataCustom(data,"Không có thực đơn ngày cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(data);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity searchMenuMonth(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        List<Integer> dataResponse = classMenuDateParentService.findClassMenuMonthList(principal, dateNotNullRequest.getDate());
        if (dataResponse == null) {
            return NewDataResponse.setDataCustom(dataResponse,"Không có thời tháng cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(dataResponse);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity searchMenuWeek(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        List<MenuWeekParentResponse> dataResponse = classMenuWeekParentService.findWeekMenu(principal, dateNotNullRequest.getDate());
        if (dataResponse == null) {
            return NewDataResponse.setDataCustom(dataResponse,"Không có bữa ăn tuần cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(dataResponse);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/image-week")
    public ResponseEntity searchImageMenuWeek(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        MenuImageWeekParentResponse dataResponse = classMenuImageFileParentService.findImageWeek(principal, dateNotNullRequest.getDate());
        if (dataResponse == null) {
            return NewDataResponse.setDataCustom(dataResponse,"Không có dữ liệu ảnh bữa ăn tuần cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(dataResponse);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/file-week")
    public ResponseEntity searchFileMenuWeek(@CurrentUser UserPrincipal principal, DateNotNullRequest dateNotNullRequest) {
        // không cần truyền vào dữ liệu ngày. Trường hợp ngày, sẽ trả về ngày bé hơn ngày truyền vào để phân trang
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        ListMenuFileParentResponse dataResponse = classMenuImageFileParentService.findFileAllWeek(principal, pageable, dateNotNullRequest.getDate());
        if (CollectionUtils.isEmpty(dataResponse.getDataList())) {
            return NewDataResponse.setDataCustom(dataResponse,"Không có file danh sách bữa ăn tuần cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(dataResponse);

    }

}