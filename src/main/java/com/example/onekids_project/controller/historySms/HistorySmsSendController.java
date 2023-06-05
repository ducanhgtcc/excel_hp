package com.example.onekids_project.controller.historySms;

import com.example.onekids_project.mobile.teacher.response.historynotifi.HistorySmsSendNotifiResponse;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.notifihistory.ListHistorySmsSendResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.HistorySmsSendService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * lịch sử tin nhắn sms
 */
@RestController
@RequestMapping("/web/historysmssend")
public class HistorySmsSendController {

    @Autowired
    HistorySmsSendService historySmsSendService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchhistorySmsSend(@CurrentUser UserPrincipal principal, @Valid SearchHistorySmsSendNewtRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListHistorySmsSendResponse response = historySmsSendService.searchHistorySmsSend(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    // xem chi tiết
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMessageTeacherDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<HistorySmsSendNotifiResponse> responseList = historySmsSendService.findDetailHistory(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    // xem chi tiết
    @RequestMapping(method = RequestMethod.GET, value = "/fail/{id}")
    public ResponseEntity findMessageTeacherDetailFail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<HistorySmsSendNotifiResponse> responseList = historySmsSendService.findDetailHistoryFail(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    // xem chi tiết
    @RequestMapping(method = RequestMethod.GET, value = "/detailall/{id}")
    public ResponseEntity findMessageTeacherDetailAll(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<HistorySmsSendNotifiResponse> responseList = historySmsSendService.findDetailHistoryAll(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    // xem noi dung
    @RequestMapping(method = RequestMethod.GET, value = "/viewContent/{id}")
    public ResponseEntity viewcontent(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<HistorySmsSendNotifiResponse> responseList = historySmsSendService.viewContent(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    // xem noi dung
    @RequestMapping(method = RequestMethod.GET, value = "/export/excel")
    public ResponseEntity exportExcelSms(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        RequestUtils.getFirstRequest(principal, idList);
        List<ExcelResponse> responseList = historySmsSendService.exportExcelSms(idList);
        return NewDataResponse.setDataSearch(responseList);
    }

}
