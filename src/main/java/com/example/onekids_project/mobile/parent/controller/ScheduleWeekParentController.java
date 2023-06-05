package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleWeekParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleWeekParentService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/parent/schedule/week")
public class ScheduleWeekParentController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScheduleWeekParentService scheduleWeekParentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchScheduleDate(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        List<ScheduleWeekParentResponse> scheduleWeekParentResponse = scheduleWeekParentService.findScheduleWeek(principal, dateNotNullRequest.getDate());
//            String jsonScheduleWeekParent = new Gson().toJson(scheduleWeekParentResponse);
        if (CollectionUtils.isEmpty(scheduleWeekParentResponse)) {
            return NewDataResponse.setDataCustom(scheduleWeekParentResponse,"Không có thời khóa biểu tuần nào cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(scheduleWeekParentResponse);

    }
}
