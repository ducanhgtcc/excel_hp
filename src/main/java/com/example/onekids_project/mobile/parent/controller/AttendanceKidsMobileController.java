package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.common.UrlFileConstant;
import com.example.onekids_project.mobile.parent.importexport.model.AttendanceKidsExModel;
import com.example.onekids_project.mobile.parent.importexport.service.AttendanceKidsExcelMobileService;
import com.example.onekids_project.mobile.parent.response.attendance.AttendanceEatResponse;
import com.example.onekids_project.mobile.parent.response.attendance.AttendanceMobileExcelResponse;
import com.example.onekids_project.mobile.parent.response.attendance.ListAttendanceMobileResponse;
import com.example.onekids_project.mobile.parent.response.attendance.ListAttendanceMonthResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.AttendanceMobileService;
import com.example.onekids_project.mobile.request.AttendanceRequest;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.mobile.request.PageNumberDateNullableRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.HandleFileUtils;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mob/parent/attendance")
public class AttendanceKidsMobileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttendanceMobileService attendanceMobileService;
    @Autowired
    private AttendanceKidsExcelMobileService attendanceKidsExcelMobileService;

    /**
     * tìm kiếm danh sách lời nhắn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchAttendanceKids(@CurrentUser UserPrincipal principal, @Valid PageNumberDateNullableRequest pageNumberDateNullableRequest) {
        RequestUtils.getFirstRequest(principal, pageNumberDateNullableRequest);
        CommonValidate.checkDataParent(principal);
        ListAttendanceMobileResponse dataList = attendanceMobileService.findAttendace(principal, pageNumberDateNullableRequest.getPageNumber(), pageNumberDateNullableRequest.getDate());
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * tìm kiếm ngày nghỉ trong tháng
     *
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity searchAttendanceMonth(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        ListAttendanceMonthResponse data = attendanceMobileService.findAttendanceMonth(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(data);

    }


    /**
     * tìm kiếm danh sách lời nhắn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/eat")
    public ResponseEntity searchAttendanceEat(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        AttendanceEatResponse data = attendanceMobileService.findAttendanceEatMonth(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * xuất file excel điểm danh học của 1 học sinh trong 1 tháng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-kid-month")
    public ResponseEntity getAllAttendanceKidMonth(@CurrentUser UserPrincipal principal, @Valid AttendanceRequest attendanceRequest) {
        CommonValidate.checkDataParent(principal);
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (principal.getIdKidLogin() != null) {
                LocalDate date = attendanceRequest.getDate();
                int monthS = date.getMonthValue();
                int yearS = date.getYear();
                Object url = null;

                List<AttendanceMobileExcelResponse> listAttendanceKids = attendanceMobileService.findExportMonthAttendace(principal, attendanceRequest.getDatePage(), attendanceRequest.getDate());

                if (CollectionUtils.isEmpty(listAttendanceKids)) {
                    url = AppConstant.URL_DEFAULT_FILE;
                    return NewDataResponse.setDataCustom(url, "lỗi tìm kiếm học sinh điểm danh");
                }
                List<AttendanceKidsExModel> attendanceKidsExModels = attendanceMobileService.detachedAttendanceKidsOfMonth(principal, listAttendanceKids);
                ByteArrayInputStream in = null;
                try {

                    in = attendanceKidsExcelMobileService.attendanceToExcelMonth(principal, attendanceKidsExModels, attendanceRequest.getDate());

                    String urlFolder = HandleFileUtils.getUrl(idSchoolLogin, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.KHAC);
                    String fileName = "thong_ke_diem_danh_thang_" + monthS + "_" + yearS + "_" + System.currentTimeMillis();
                    Path fileNameAndPath = Paths.get(urlFolder, fileName + ".xlsx");
                    Files.write(fileNameAndPath, in.readAllBytes());

                    LocalDate time = LocalDate.now();

                    int month = time.getMonthValue();

                    int year = time.getYear();

                    url = AppConstant.URL_DEFAULT + idSchoolLogin + "/" + year + "/T" + month + "/" + UploadDownloadConstant.KHAC + "/" + fileName + ".xlsx";

                } catch (IOException e) {
                    logger.error("Lỗi xuất file điểm danh tháng");
                    return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.BAD_REQUEST);
                }
                return NewDataResponse.setDataSearch(url);
            } else {
                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception Tìm kiếm học sinh không thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm học sinh không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * xuất file excel điểm danh ăn của 1 học sinh trong 1 tháng
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-attendance-eat-kid-month")
    public ResponseEntity getAllAttendanceKidEatMonth(@CurrentUser UserPrincipal principal, @Valid AttendanceRequest attendanceRequest) {
        RequestUtils.getFirstRequest(principal, attendanceRequest);
        CommonValidate.checkDataParent(principal);
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (principal.getIdKidLogin() != null) {

                LocalDate date = attendanceRequest.getDate();

                int monthS = date.getMonthValue();

                int yearS = date.getYear();
                Object url = null;
                List<AttendanceMobileExcelResponse> listAttendanceKids = attendanceMobileService.findExportMonthAttendace(principal, attendanceRequest.getDatePage(), attendanceRequest.getDate());

                if (CollectionUtils.isEmpty(listAttendanceKids)) {
                    url = AppConstant.URL_DEFAULT_FILE;
                    return NewDataResponse.setDataCustom(url, "Lỗi tìm kiếm học sinh điểm danh ăn");
                }
                List<AttendanceKidsExModel> attendanceKidsExModels = attendanceMobileService.detachedAttendanceKidsOfMonth(principal, listAttendanceKids);

                ByteArrayInputStream in = null;

                try {

                    in = attendanceKidsExcelMobileService.attendanceEatToExcelMonth(principal, attendanceKidsExModels, attendanceRequest.getDate());

                    String urlFolder = HandleFileUtils.getUrl(idSchoolLogin, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.KHAC);
                    String fileName = "thong_ke_diem_danh_an_" + monthS + "_" + yearS + "_" + System.currentTimeMillis();
                    Path fileNameAndPath = Paths.get(urlFolder, fileName + ".xlsx");
                    Files.write(fileNameAndPath, in.readAllBytes());
                    LocalDate time = LocalDate.now();

                    int month = time.getMonthValue();

                    int year = time.getYear();

                    url = AppConstant.URL_DEFAULT + idSchoolLogin + "/" + year + "/T" + month + "/" + UploadDownloadConstant.KHAC + "/" + fileName + ".xlsx";

                } catch (IOException e) {
                    logger.error("Lỗi xuất file điểm danh");
                }
                return NewDataResponse.setDataCustom(url, "Tạo điểm danh thành công");
            } else {

                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception Tìm kiếm học sinh không thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm học sinh không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
