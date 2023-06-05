package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.importexport.model.AttendanceKidsModel;
import com.example.onekids_project.importexport.service.AttendanceKidsExcelService;
import com.example.onekids_project.request.attendancekids.*;
import com.example.onekids_project.request.finance.order.OrderExcelRequest;
import com.example.onekids_project.response.attendancekids.*;
import com.example.onekids_project.response.classes.DayOffClassResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelDynamicResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.schoolconfig.SchoolConfigAttendanceResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsService;
import com.example.onekids_project.service.servicecustom.classes.DayOffClassService;
import com.example.onekids_project.service.servicecustom.config.SupperPlusConfigService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/web/attendance-kids")
public class AttendanceKidsController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceKidsController.class);

    @Autowired
    private AttendanceKidsService attendanceKidsService;

    @Autowired
    private AttendanceKidsExcelService attendanceKidsExcelService;
    @Autowired
    private DayOffClassService dayOffClassService;
    @Autowired
    private SupperPlusConfigService supperPlusConfigService;

    @RequestMapping(method = RequestMethod.GET, value = "/check-absent")
    public ResponseEntity checkAbsentInClass(@CurrentUser UserPrincipal principal, @Valid AbsentCheckSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        List<Long> longList = attendanceKidsService.checkAbsentHas(principal, request);
        return NewDataResponse.setDataSearch(longList);
    }
    /**
     * tìm kiếm cấu hình nhà trường của người đang đang nhập cho attendance
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/config/pickup-kid")
    public ResponseEntity getSchoolConfigByIdByAttendance(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkPlusOrTeacher(principal);
        SchoolConfigAttendanceResponse sysConfigResponse = supperPlusConfigService.findSchoolConfigByIdSchoolByAttendance(principal);
        return NewDataResponse.setDataSearch(sysConfigResponse);
    }

    /**
     * tìm kiếm thông tin điểm danh cho các học sinh trong một ngày
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchDetailDate(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListAttendanceKidsDetailDateResponse data = attendanceKidsService.searchAllAttendanceKidsDetailDate(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * tìm kiếm các học sinh cho điểm danh đến trong một ngày
     *
     * @param principal
     * @param attendanceKidsSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search/arrive")
    public ResponseEntity searchArriveDate(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        RequestUtils.getFirstRequest(principal, attendanceKidsSearchRequest);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAttendanceArriveKidsDateResponse data = attendanceKidsService.searchAttendanceArriveKidsDate(principal, idSchoolLogin, attendanceKidsSearchRequest);
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * cập nhật điểm danh cho một học sinh theo id
     *
     * @param principal
     * @param id
     * @param attendanceArriveKidsDateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/arrive/{id}")
    public ResponseEntity saveArriveOneKidDate(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id, @Valid @RequestBody AttendanceArriveKidsDateRequest attendanceArriveKidsDateRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, attendanceArriveKidsDateRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        if (!attendanceArriveKidsDateRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + attendanceArriveKidsDateRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        AttendanceArriveKidsDateResponse attendanceArriveKidsDateResponse = attendanceKidsService.saveAttendanceArriveOneKidsDate(principal.getIdSchoolLogin(), principal, attendanceArriveKidsDateRequest);
        return NewDataResponse.setDataCreate(attendanceArriveKidsDateResponse);

    }

    /**
     * cập nhật nội dung điểm danh
     *
     * @param principal
     * @param attendanceArriveUpdateContentRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/arrive-content/{id}")
    public ResponseEntity updateContentArrive(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id, @Valid @ModelAttribute AttendanceArriveUpdateContentRequest attendanceArriveUpdateContentRequest) throws IOException {
        RequestUtils.getFirstRequest(principal, attendanceArriveUpdateContentRequest);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = attendanceKidsService.updateContentArrive(idSchoolLogin, principal, id, attendanceArriveUpdateContentRequest);
        return NewDataResponse.setDataCreate(checkUpdate);
    }

    /**
     * cập nhật điểm danh cho nhiều học sinh
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/arrive/many")
    public ResponseEntity saveArriveManyKidDate(@CurrentUser UserPrincipal principal, @RequestBody List<AttendanceArriveKidsDateRequest> attendanceArriveKidsDateRequestList) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, attendanceArriveKidsDateRequestList);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = attendanceKidsService.saveAttendanceArriveManyKidsDate(idSchoolLogin, principal, attendanceArriveKidsDateRequestList);
        return NewDataResponse.setDataCreate(checkUpdate);

    }

    /**
     * xem chi tiết điểm danh đến của một học sinh trong tháng
     *
     * @param principal
     * @param idKid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/arrive/month/{idKid}")
    public ResponseEntity getArriveByIdKidOfMonth(@CurrentUser UserPrincipal principal, @PathVariable(name = "idKid") Long idKid, @RequestParam(name = "month") Integer month, @RequestParam(name = "year") Integer year) {
        RequestUtils.getFirstRequest(principal, idKid);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<AttendanceArriveKidsDateResponse> attendanceArriveKidsDateResponseList = attendanceKidsService.findAttendanceArriveKidsDetailOfMonth(idSchoolLogin, idKid, month, year);
        return NewDataResponse.setDataSearch(attendanceArriveKidsDateResponseList);

    }

    /**
     * tìm kiếm điểm danh về cho các học sinh trong một ngày
     *
     * @param principal
     * @param attendanceKidsSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search/leave")
    public ResponseEntity searchLeaveDate(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        RequestUtils.getFirstRequest(principal, attendanceKidsSearchRequest);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<AttendanceLeaveKidsDateResponse> attendanceLeaveKidsDateResponseList = attendanceKidsService.searchAttendanceLeaveKidsDate(idSchoolLogin, attendanceKidsSearchRequest);
        return NewDataResponse.setDataSearch(attendanceLeaveKidsDateResponseList);

    }

    /**
     * cập nhật điểm danh về cho một học sinh trong một ngày
     *
     * @param principal
     * @param id
     * @param attendanceLeaveKidsDateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/leave/{id}")
    public ResponseEntity saveLeaveOneKidDate(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id, @Valid @RequestBody AttendanceLeaveKidsDateRequest attendanceLeaveKidsDateRequest) throws FirebaseMessagingException {
        if (!attendanceLeaveKidsDateRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + attendanceLeaveKidsDateRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        AttendanceLeaveKidsDateResponse attendanceLeaveKidsDateResponse = attendanceKidsService.saveAttendanceLeaveOneKidsDate(principal.getIdSchoolLogin(), principal, attendanceLeaveKidsDateRequest);
        return NewDataResponse.setDataUpdate(attendanceLeaveKidsDateResponse);
    }

    /**
     * cập nhật nội dung điểm danh ve
     *
     * @param principal
     * @param attendanceLeaveUpdateContentRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/leave-content/{id}")
    public ResponseEntity updateContentLeave(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id, @Valid @ModelAttribute AttendanceLeaveUpdateContentRequest attendanceLeaveUpdateContentRequest) throws IOException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = attendanceKidsService.updateContentLeave(idSchoolLogin, principal, id, attendanceLeaveUpdateContentRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * cập nhật điểm danh về cho nhiều học sinh trong một ngày
     *
     * @param principal
     * @param attendanceLeaveKidsDateRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/leave/many")
    public ResponseEntity saveLeaveManyKidDate(@CurrentUser UserPrincipal principal, @RequestBody List<AttendanceLeaveKidsDateRequest> attendanceLeaveKidsDateRequestList) throws FirebaseMessagingException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = attendanceKidsService.saveAttendanceLeaveManyKidsDate(idSchoolLogin, principal, attendanceLeaveKidsDateRequestList);
        return NewDataResponse.setDataCreate(checkUpdate);
    }

    /**
     * chi tiết điểm danh về của một học sinh trong một tháng
     *
     * @param principal
     * @param idKid
     * @param month
     * @param year
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/leave/month/{idKid}")
    public ResponseEntity getLeaveByIdKidOfMonth(@CurrentUser UserPrincipal principal, @PathVariable(name = "idKid") Long idKid, @RequestParam(name = "month") Integer month, @RequestParam(name = "year") Integer year) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<AttendanceLeaveKidsDateResponse> attendanceLeaveKidsDateResponseList = attendanceKidsService.findAttendanceLeaveKidsDetailOfMonth(idSchoolLogin, idKid, month, year);
        return NewDataResponse.setDataSearch(attendanceLeaveKidsDateResponseList);
    }

    /**
     * tìm kiếm điểm danh ăn cho các học sinh trong một ngày
     *
     * @param principal
     * @param attendanceKidsSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search/eat")
    public ResponseEntity searchEatDate(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAttendanceEatKidsDateResponse attendanceLeaveKidsDateResponseList = attendanceKidsService.searchAttendanceEatKidsDate(idSchoolLogin, attendanceKidsSearchRequest);
        return NewDataResponse.setDataSearch(attendanceLeaveKidsDateResponseList);
    }

    /**
     * cập nhật điểm danh ăn cho một học sinh trong một ngày
     *
     * @param principal
     * @param id
     * @param attendanceEatKidsDateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/eat/{id}")
    public ResponseEntity saveEatOneKidDate(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id, @Valid @RequestBody AttendanceEatKidsDateRequest attendanceEatKidsDateRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        if (!attendanceEatKidsDateRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + attendanceEatKidsDateRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        AttendanceEatKidsDateResponse attendanceEatKidsDateResponse = attendanceKidsService.saveAttendanceEatOneKidsDate(idSchoolLogin, principal, attendanceEatKidsDateRequest);
        return NewDataResponse.setDataUpdate(attendanceEatKidsDateResponse);
    }

    /**
     * cập nhật điểm danh ăn cho nhiều học sinh trong một ngày
     *
     * @param principal
     * @param attendanceEatKidsDateRequestList
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/eat/many")
    public ResponseEntity saveEatManyKidDate(@CurrentUser UserPrincipal principal, @RequestBody List<AttendanceEatKidsDateRequest> attendanceEatKidsDateRequestList) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = attendanceKidsService.saveAttendanceEatManyKidsDate(idSchoolLogin, principal, attendanceEatKidsDateRequestList);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * chi tiết điểm danh ăn của một học sinh trong một tháng
     *
     * @param principal
     * @param idKid
     * @param month
     * @param year
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/eat/month/{idKid}")
    public ResponseEntity getEatByIdKidOfMonth(@CurrentUser UserPrincipal principal, @PathVariable(name = "idKid") Long idKid, @RequestParam(name = "month") Integer month, @RequestParam(name = "year") Integer year) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<AttendanceEatKidsDateResponse> attendanceEatKidsDateResponseList = attendanceKidsService.findAttendanceEatKidsDetailOfMonth(idSchoolLogin, idKid, month, year);
        return NewDataResponse.setDataSearch(attendanceEatKidsDateResponseList);
    }

    /**
     * xuất file excel điểm danh học sinh của cả 1 lớp theo ngày
     *
     * @param attendanceKidsSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid")
    public ResponseEntity getAllAttendance(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        if (idSchoolLogin == null || idSchoolLogin <= 0) {
            logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
            return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
        }
        if (attendanceKidsSearchRequest.getIdClass() != null) {
            ListAttendanceKidsDetailDateResponse listKids = attendanceKidsService.searchAllAttendanceKidsDetailDate(principal, attendanceKidsSearchRequest);
            List<AttendanceKidsModel> list = attendanceKidsService.getFileAttendanceKids(listKids);
            if (list.size() == 0) {
                logger.warn("lỗi tìm kiếm học sinh");
                return ErrorResponse.errorData("Không thể tìm kiếm học sinh", "Không thể tìm kiếm học sinh", HttpStatus.NOT_FOUND);
            }
            ByteArrayInputStream in = null;

            try {
                in = attendanceKidsExcelService.attendanceToExcel(list, idSchoolLogin, attendanceKidsSearchRequest.getIdClass(), attendanceKidsSearchRequest.getDate());

            } catch (IOException e) {
                logger.error("Lỗi xuất file điểm danh ngày");
            }
            logger.info("Tìm kiếm học sinh thành công");
            return ResponseEntity.ok().body(new InputStreamResource(in)); // chưa trả được DataRespone Custom
        } else {
            logger.info("Thất bại");
            return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * xuất file excel điểm danh học sinh của cả 1 lớp theo ngày NEW
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid-new")
    public ResponseEntity getAllAttendanceNew(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest request) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAttendanceKidsDetailDateResponse listKids = attendanceKidsService.searchAllAttendanceKidsDetailDate(principal, request);
        List<ExcelNewResponse> list = attendanceKidsService.getFileAttendanceKidsNew(listKids, idSchoolLogin, request.getIdClass(), request.getDate());
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * xuất file excel điểm danh của một học sinh theo tháng của 1 lớp
     *
     * @param principal
     * @param attendanceKidsSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid-month")
    public ResponseEntity getAllAttendanceKidMonth(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (idSchoolLogin == null || idSchoolLogin <= 0) {
                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
            }
            if (attendanceKidsSearchRequest.getIdClass() != null) {
                List<AttendanceKidsDetailDateResponse> listKids = attendanceKidsService.findAttendanceKidsClassOfMonth(idSchoolLogin, attendanceKidsSearchRequest);
                if (CollectionUtils.isEmpty(listKids)) {
                    logger.error("lỗi tìm kiếm học sinh");
                    return ErrorResponse.errorData("Không thể tìm kiếm học sinh", "Không thể tìm kiếm học sinh", HttpStatus.NOT_FOUND);
                }
                List<ListIdKidDTO> listIdKidDTOS = attendanceKidsService.listIdAttendanceKidsDetailOfMonth(idSchoolLogin, attendanceKidsSearchRequest);
                Map<Long, List<AttendanceKidsModel>> map = attendanceKidsService.detachedListAttendanceKidsClassOfMonth(listKids, listIdKidDTOS);
                ByteArrayInputStream in = null;
                try {
                    in = attendanceKidsExcelService.attendanceToExcelMonth(map, idSchoolLogin, attendanceKidsSearchRequest.getIdClass(), attendanceKidsSearchRequest.getDate());
                } catch (IOException e) {
                    logger.error("Lỗi xuất file điểm danh tháng");
                }
                logger.info("Tìm kiếm học sinh thành công");
                return ResponseEntity.ok().body(new InputStreamResource(in)); // chưa trả được DataRespone Custom
            } else {
                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Exception Tìm kiếm học sinh không thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm học sinh không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * xuất file excel điểm danh của một học sinh theo tháng của 1 lớp NEW
     *
     * @param principal
     * @param attendanceKidsSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid-month-new")
    public ResponseEntity getAllAttendanceKidMonthNew(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<AttendanceKidsDetailDateResponse> listKids = attendanceKidsService.findAttendanceKidsClassOfMonth(idSchoolLogin, attendanceKidsSearchRequest);
        List<ListIdKidDTO> listIdKidDTOS = attendanceKidsService.listIdAttendanceKidsDetailOfMonth(idSchoolLogin, attendanceKidsSearchRequest);
        List<ExcelNewResponse> list = attendanceKidsService.detachedListAttendanceKidsClassOfMonthNew(listKids, listIdKidDTOS, idSchoolLogin, attendanceKidsSearchRequest.getIdClass(), attendanceKidsSearchRequest.getDate());
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * Xuất file excel tổng hợp điểm danh
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid-custom")
    public ResponseEntity getAllAttendanceKidCustom(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchCustomRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal);
        ByteArrayInputStream in = attendanceKidsExcelService.exportAttendaceKidCustom(principal, request);
        return ResponseEntity.ok().body(new InputStreamResource(in));
    }
    /**
     * Xuất file excel tổng hợp điểm danh NEW
     * @param principal
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid-custom-new")
    public ResponseEntity getAllAttendanceKidCustomNew(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchCustomRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal);
        List<ExcelNewResponse> data = attendanceKidsExcelService.exportAttendanceKidCustomNew(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid-custom-detail")
    public ResponseEntity excelExportAttendanceDetailController(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsSearchDetailRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        ExcelDynamicResponse data = attendanceKidsExcelService.excelExportAttendanceKidsDetailService(request);
        return NewDataResponse.setDataSearch(data);
    }
    /**
     * xem thông tin ngày nghỉ theo tháng
     * @param principal
     * @param idClass
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class/dayOff/view/{idClass}")
    public ResponseEntity getDayOffClassYear(@CurrentUser UserPrincipal principal, @PathVariable Long idClass) {
        RequestUtils.getFirstRequest(principal, idClass);
        List<DayOffClassResponse> responseList = dayOffClassService.getDayOffClassView(principal, idClass);
        return NewDataResponse.setDataSearch(responseList);
    }

}
