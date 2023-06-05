package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.master.request.ActiveAgentRequest;
import com.example.onekids_project.master.request.SchoolSmsRequest;
import com.example.onekids_project.master.request.school.GroupTypeRequest;
import com.example.onekids_project.master.response.SmsSchoolResponse;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.school.CreateSchoolRequest;
import com.example.onekids_project.request.school.SearchSchoolRequest;
import com.example.onekids_project.request.school.UpdateSchoolRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.school.ListSchoolResponse;
import com.example.onekids_project.response.school.SchoolOtherResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.response.school.SchoolUniqueResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/web/school")
public class SchoolController {
    private static final Logger logger = LoggerFactory.getLogger(SchoolController.class);

    @Autowired
    private SchoolService schoolService;

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getSchoolBriefController() {
        List<SchoolOtherResponse> responseList = schoolService.getSchoolBriefService();
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "school-in-agent/{idAgent}")
    public ResponseEntity findSchoolInAgent(@CurrentUser UserPrincipal principal, @PathVariable Long idAgent) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), idAgent);
        List<SchoolOtherResponse> responseList = schoolService.findSchoolInAgent(idAgent);
        return NewDataResponse.setDataSearch(responseList);
    }


    /**
     * find unique school cho các thành phần khác
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/unique")
    public ResponseEntity findAllSchoolUnique() {
        try {
            List<SchoolUniqueResponse> schoolUniqueResponseList = schoolService.findSchoolUnique();
            if (CollectionUtils.isEmpty(schoolUniqueResponseList)) {
                logger.warn("Lỗi tìm kiếm tất cả các trường cho các thành phần khác");
                return DataResponse.getData("Lỗi tìm kiếm tất cả các trường cho các thành phần khác", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm tất cả các trường cho các thành phần khác thành công");
            return DataResponse.getData(schoolUniqueResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm tất cả các trường cho các thành phần khác" + e.toString());
            return ErrorResponse.errorData(e.getMessage(), "Exception Lỗi tìm kiếm tất cả các trường cho các thành phần khác", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * tìm kiếm tất cả các trường
     *
     * @param baseRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll(BaseRequest baseRequest) {
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

    /**
     * tìm kiếm trường theo id
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        SchoolResponse response = schoolService.findByIdSchool(id).orElseThrow();
        return NewDataResponse.setDataSearch(response);
    }


    /**
     * tìm kiếm trường theo tùy chọn
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/searchData")
    public ResponseEntity searchSchool(@CurrentUser UserPrincipal principal, @Valid SearchSchoolRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListSchoolResponse listSchoolResponse = schoolService.searchSchool(request);
        return NewDataResponse.setDataSearch(listSchoolResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchData/export")
    public ResponseEntity searchSchoolExport(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), idList);
        List<ExcelResponse> response = schoolService.searchSchoolExport(idList);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * tìm kiếm trường theo tùy chọn
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity search(@CurrentUser UserPrincipal principal, SearchSchoolRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListSchoolResponse listSchoolResponse = schoolService.searchSchool(request);
        return NewDataResponse.setDataSearch(listSchoolResponse);
    }

    /**
     * thêm mới trường
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute CreateSchoolRequest request) throws IOException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = schoolService.createSchool(request);
        return NewDataResponse.setDataCustom(check, MessageConstant.CREATE_SCHOOL);
    }

    /**
     * cập nhật trường
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/edit")
    public ResponseEntity updated(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute UpdateSchoolRequest request) throws IOException {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        SchoolResponse schoolResponse = schoolService.updateSchool(request);
        return NewDataResponse.setDataCustom(schoolResponse, MessageWebConstant.UPDATE_SCHOOL);
    }

//    @Deprecated
//    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
//    public ResponseEntity update(@PathVariable(name = "id") Long id, @Valid @ModelAttribute UpdateSchoolRequest updateSchoolRequest) {
//        try {
//            if (updateSchoolRequest.getId() != id) {
//                logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
//                return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSchoolRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
//            }
//
//            SchoolResponse schoolResponse = schoolService.updateSchool(updateSchoolRequest);
//            if (schoolResponse == null) {
//                logger.error("lỗi cập nhật trường với id=" + id);
//                return ErrorResponse.errorData("Không thể cập nhật trường với id=" + id, "Không thể cập nhật trường", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//            logger.info("cập nhật trường thành công");
//            return DataResponse.getData(schoolResponse, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Exception cập nhật trường: " + e.getMessage());
//            return ErrorResponse.errorData(e.getMessage(), "Lỗi cập nhật trường", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * xóa trường
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        try {
            boolean checkDelete = schoolService.deleteSchool(id);
            if (!checkDelete) {
                logger.error("lỗi xóa trường");
                return ErrorResponse.errorData("Lỗi xóa trường", "Trường này không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST);
            }
            logger.info("xóa trường thành công");
            return DataResponse.getData("Xóa trường thành công", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception xóa trường theo id: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi xóa trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/add-sms-school")
    public ResponseEntity addSmsSchool(@RequestBody SchoolSmsRequest schoolSmsRequest) {
        try {
            int checkCount = schoolService.saveSchoolSms(schoolSmsRequest);
            if (checkCount == schoolSmsRequest.getIdSchoolList().size()) {
                logger.error("Lỗi cấp SMS trường học");
                return ErrorResponse.errorData("Lỗi cấp SMS trường học", "Không thể cấp SMS cho trường học", HttpStatus.BAD_REQUEST);
            }
            logger.info("Cấp SMS đại lý thành công");
            return DataResponse.getData(checkCount, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception cấp SMS cho trường học : " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi Cấp SMS cho trường học", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lịch sử cấp SMS
     *
     * @return
     */
    @GetMapping("/add-sms-school/{idSchool}")
    public ResponseEntity getAllSchoolSms(@PathVariable("idSchool") long idSchool) {
        try {
            List<SmsSchoolResponse> schoolResponseList = schoolService.findSchoolSmsByIdSchool(idSchool);
            if (CollectionUtils.isEmpty(schoolResponseList)) {
                logger.error("Không có lịch sử cấp sms");
                return DataResponse.getData("Không có lịch sử cấp sms", HttpStatus.NOT_FOUND);
            }
            logger.info("Tìm kiếm lịch sử cấp sms thành công");
            return DataResponse.getData(schoolResponseList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi tìm kiếm lịch sử cấp sms: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi tìm kiếm lịch sử cấp sms", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Kích hoạt nhiều agent
     *
     * @return
     */
    @PutMapping("/active-multi-school")
    public ResponseEntity updateMultiActiveSchool(@RequestBody ActiveAgentRequest activeAgentRequest) {
        try {
            boolean checkMultiActive = schoolService.updateMultiActiveSchool(activeAgentRequest.getIds(), activeAgentRequest.isActiveOrUnActive());
            if (!checkMultiActive) {
                logger.error("Không thể kích hoạt nhà trường");
                return DataResponse.getData("Không thể kích hoạt nhà trường", HttpStatus.NOT_FOUND);
            }
            logger.info("Kích hoạt nhà trường thành công");
            return DataResponse.getData(checkMultiActive, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Lỗi kích hoạt nhà trường " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Lỗi kích hoạt nhà trường", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/group-type")
    public ResponseEntity updateGroupTypeController(@Valid @RequestBody GroupTypeRequest request) {
        schoolService.updateGroupTypeService(request);
        return NewDataResponse.setDataUpdate(true);
    }


}
