package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.master.request.SearchHistorySmsRequest;
import com.example.onekids_project.master.response.HistorySmsResponse;
import com.example.onekids_project.master.response.HistorySmsResponseByStatus;
import com.example.onekids_project.master.service.HistorySmsService;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.school.ListSchoolResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.util.ConvertData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/history-sms")
public class HistorySmsController {

    private static final Logger logger = LoggerFactory.getLogger(HistorySmsController.class);

    @Autowired
    private HistorySmsService historySmsService;
    @Autowired
    private SchoolService schoolService;


    /**
     * tìm kiếm trường
     *
     * @param baseRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "school/search")
    public ResponseEntity searchSchoolSMS(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), baseRequest);
        try {
            int pageNumber = ConvertData.getPageNumber(baseRequest.getPageNumber());
            if (pageNumber == -1) {
                logger.error(AppConstant.INVALID_PAGE_NUMBER);
                return DataResponse.getData(AppConstant.INVALID_PAGE_NUMBER, HttpStatus.BAD_REQUEST);
            }
            Pageable pageable = PageRequest.of(pageNumber, AppConstant.MAX_PAGE_ITEM);

            ListSchoolResponse listSchoolResponse = schoolService.findAllSchool(pageable);
            if (listSchoolResponse == null) {
                logger.warn("Không có trường nào");
                return DataResponse.getData("Không có trường nào", HttpStatus.NOT_FOUND);
            }

            //set an agent for a school
            listSchoolResponse.getSchoolList().forEach(school -> {
                school.setAgent(school.getAgent());
            });
            logger.info("Tìm kiếm tất cả các trường thành công");
            return DataResponse.getData(listSchoolResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception tìm kiếm tất cả các trường: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi hệ thống tìm kiếm tất cả các trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity getAllHistorySms(@Valid SearchHistorySmsRequest searchHistorySmsRequest) {
        List<HistorySmsResponse> historySmsResponseList = historySmsService.findAllHistorySms(searchHistorySmsRequest);
        return NewDataResponse.setDataSearch(historySmsResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAllHistorySmsByStatus(@PathVariable("id") Long id, @RequestParam(name = "typeSend") String typeSend) {
        try {

            List<HistorySmsResponseByStatus> historySmsResponseByStatusList = historySmsService.findByHistorySmsById(id, typeSend);
            if (historySmsResponseByStatusList == null) {
                logger.error("Không có lịch sử tin nhắn nào");
                return DataResponse.getData("Không có lịch sử tin nhắn nào theo tùy chọn", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm lịch sử tin nhắn theo tùy chọn thành công");
            return DataResponse.getData(historySmsResponseByStatusList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm lịch sử tin nhắn theo tùy chọn: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi tìm kiếm lịch sử tin nhắn theo tùy chọn", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
