package com.example.onekids_project.controller.schedule;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.classes.UrlScheuldeFile;
import com.example.onekids_project.importexport.service.SchedulesExcelService;
import com.example.onekids_project.master.controller.AgentController;
import com.example.onekids_project.repository.UrlScheduleFileRepository;
import com.example.onekids_project.request.classmenu.CreateFileAndPictureMenuMultiClassRequest;
import com.example.onekids_project.request.schedule.*;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.schedule.ScheduleInClassWeekResponse;
import com.example.onekids_project.response.schedule.ScheduleResponse;
import com.example.onekids_project.response.schedule.TabDetailScheduleAllClassResponse;
import com.example.onekids_project.response.schedule.TabScheduleViewDetail;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.schedule.ScheduleService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/web/schedules")
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SchedulesExcelService schedulesExcelService;

    @Autowired
    private UrlScheduleFileRepository urlScheduleFileRepository;

    @Autowired
    private ServletContext context;

    /**
     * Tìm kiếm tất cả các thời khóa biểu màn hình xem của tuần
     *
     * @param principal
     * @param searchScheduleRequest
     * @return
     */
    @GetMapping
    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, SearchScheduleRequest searchScheduleRequest) {
        RequestUtils.getFirstRequest(principal, searchScheduleRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<ScheduleResponse> scheduleResponseList = scheduleService.findAllScheduleInWeek(idSchoolLogin, searchScheduleRequest);
        return NewDataResponse.setDataSearch(scheduleResponseList);
    }

    /**
     * Tìm kiếm thời khóa biểu cho một lớp
     *
     * @param principal
     * @param searchScheduleInClassRequest
     * @return
     */
    @GetMapping("/schedule-in-class-week")
    public ResponseEntity findAllScheduleInClassWeek(@CurrentUser UserPrincipal principal, SearchScheduleInClassRequest searchScheduleInClassRequest) {
        RequestUtils.getFirstRequest(principal, searchScheduleInClassRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<ScheduleInClassWeekResponse> scheduleInClassWeekResponseList = scheduleService.findAllScheduleInClassWeekByTimeSchedule(idSchoolLogin, searchScheduleInClassRequest);
        return NewDataResponse.setDataSearch(scheduleInClassWeekResponseList);

    }


    /**
     * Update thời khóa biểu 1 lớp
     *
     * @param principal
     * @param scheduleInClassWeekRequestList
     * @return
     */
    @PostMapping("/schedule-in-class-week")
    public ResponseEntity createScheduleInClassWeek(@CurrentUser UserPrincipal principal, @RequestBody List<ScheduleInClassWeekRequest> scheduleInClassWeekRequestList) {
        RequestUtils.getFirstRequest(principal, scheduleInClassWeekRequestList);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = scheduleService.saveScheduleClassWeek(idSchoolLogin, principal, scheduleInClassWeekRequestList);
        return NewDataResponse.setDataUpdate(check);
    }

    /**
     * Thêm mới thời khóa biểu 1 lớp
     *
     * @param principal
     * @param createMultiSchedule
     * @return
     */
    @PostMapping("/multi-schedule")
    public ResponseEntity createMultiScheduleForClass(@CurrentUser UserPrincipal principal, @RequestBody CreateMultiSchedule createMultiSchedule) {
        RequestUtils.getFirstRequest(principal, createMultiSchedule);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = scheduleService.saveMultiSchedule(idSchoolLogin, principal, createMultiSchedule);
        return NewDataResponse.setDataCreate(check);

    }

    /**
     * Tìm kiếm thời khóa biểu màn hình chi tiết của các lớp
     *
     * @param principal
     * @param searchScheduleRequest
     * @return
     */
    @GetMapping("/all-schedule-detail")
    public ResponseEntity findAllScheduleDetail(@CurrentUser UserPrincipal principal, SearchScheduleRequest searchScheduleRequest) {
        RequestUtils.getFirstRequest(principal, searchScheduleRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<TabDetailScheduleAllClassResponse> tabDetailScheduleAllClassResponseList = scheduleService.findAllScheduleTabDetail(idSchoolLogin, searchScheduleRequest);
        return NewDataResponse.setDataSearch(tabDetailScheduleAllClassResponseList);

    }

    @PutMapping("/approve")
    public ResponseEntity updateApprove(@CurrentUser UserPrincipal principal, @RequestBody ApproveStatus approveStatus) {
        RequestUtils.getFirstRequest(principal, approveStatus);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = scheduleService.updateApprove(idSchoolLogin, approveStatus);
        return NewDataResponse.setDataUpdate(check);

    }

    @PutMapping("/multi-approve")
    public ResponseEntity updateMultiApprove(@CurrentUser UserPrincipal principal, @RequestBody List<ApproveStatus> approveStatusList) {
        RequestUtils.getFirstRequest(principal, approveStatusList);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = scheduleService.updateMultiApprove(idSchoolLogin, approveStatusList);
        return NewDataResponse.setDataUpdate(check);

    }

    /**
     * Tìm kiếm thời khóa biểu màn hình chi tiết các tuần trong năm của 1 lớp
     *
     * @param principal
     * @param
     * @return
     */
    @GetMapping("/schedule-view-detail/{idClass}")
    public ResponseEntity findScheduleViewDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idClass) {
        RequestUtils.getFirstRequest(principal, idClass);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<TabScheduleViewDetail> tabScheduleViewDetailList = scheduleService.findScheduleDetailByClass(idSchoolLogin, idClass);
        return NewDataResponse.setDataSearch(tabScheduleViewDetailList);

    }

    @PutMapping("/delete-content-schedule")
    public ResponseEntity deleteContentSchedule(@CurrentUser UserPrincipal principal, @RequestBody List<SearchScheduleInClassRequest> searchScheduleInClassRequestList) {
        RequestUtils.getFirstRequest(principal, searchScheduleInClassRequestList);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean check = scheduleService.deleteContentSchedule(searchScheduleInClassRequestList);
        return NewDataResponse.setDataDelete(check);

    }

    /**
     * Xuất file excel thời khóa biểu
     *
     * @param principal
     * @param searchScheduleInClassRequest
     * @return
     */

    @RequestMapping(method = RequestMethod.GET, value = "/export-schedule-class")
    public ResponseEntity getAllAttendanceKidMonth(@CurrentUser UserPrincipal principal, @Valid SearchScheduleInClassRequest searchScheduleInClassRequest) {
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (idSchoolLogin == null || idSchoolLogin <= 0) {
                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
            }
            if (searchScheduleInClassRequest.getIdClass() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                List<ScheduleInClassWeekResponse> scheduleInClassWeekResponseList = scheduleService.findAllScheduleInClassWeekByTimeSchedule(idSchoolLogin, searchScheduleInClassRequest);

                if (CollectionUtils.isEmpty(scheduleInClassWeekResponseList)) {
                    logger.error("lỗi tìm kiếm thời khóa biểu");
                    return ErrorResponse.errorData("Không thể tìm kiếm thời khóa biểu", "Không thể tìm kiếm thời khóa biểu", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                ByteArrayInputStream in = null;

                try {
                    LocalDate date = LocalDate.parse(searchScheduleInClassRequest.getTimeSchedule(), formatter);
                    in = schedulesExcelService.schedulesToExcel(scheduleInClassWeekResponseList, idSchoolLogin, searchScheduleInClassRequest.getIdClass(), date);

                } catch (IOException e) {
                    logger.error("Lỗi xuất file thời khóa biểu");
                }
                logger.info("xuất file thời khóa biểu thành công");
                return ResponseEntity.ok().body(new InputStreamResource(in));
            } else {
                logger.info("Thất bại");    // chưa trả được DataRespone CustomExcel
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);

            }


        } catch (Exception e) {
            logger.error("Exception Tìm kiếm thời khóa biểu thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm thời khóa biểu không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Xuất file excel thời khóa biểu NEW
     *
     * @param principal
     * @param searchScheduleInClassRequest
     * @return
     */

    @RequestMapping(method = RequestMethod.GET, value = "/export-schedule-class-new")
    public ResponseEntity getAllAttendanceKidMonthNew(@CurrentUser UserPrincipal principal, @Valid SearchScheduleInClassRequest searchScheduleInClassRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<ExcelResponse> list = scheduleService.findAllScheduleInClassWeekByTimeScheduleNew(idSchoolLogin, searchScheduleInClassRequest);
        return NewDataResponse.setDataSearch(list);
    }

    @PostMapping("/file-and-picture")
    public ResponseEntity handleFileAndPicture(@CurrentUser UserPrincipal principal, @ModelAttribute CreateFileAndPictureRequest createFileAndPictureRequest) throws IOException {
        RequestUtils.getFirstRequest(principal, createFileAndPictureRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = scheduleService.saveScheduleFile(idSchoolLogin, principal, createFileAndPictureRequest);
        return NewDataResponse.setDataCreate(check);

    }

    @PostMapping("/import-schedule-excel")
    public ResponseEntity importExcelSchedule(@CurrentUser UserPrincipal principal, @ModelAttribute UploadScheduleRequest uploadScheduleRequest) throws IOException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        CreateMultiSchedule createMultiSchedule = schedulesExcelService.saveScheduleFileExcel(idSchoolLogin, uploadScheduleRequest);
        if (createMultiSchedule == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File thời khóa biểu không đúng Form nhập liệu!");
        }
        boolean check = scheduleService.saveMultiSchedule(idSchoolLogin, principal, createMultiSchedule);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * Xóa file trong form
     *
     * @param
     * @throws
     */
    @PutMapping("/schedule-file")
    public ResponseEntity deleteScheduleFile(@CurrentUser UserPrincipal principal, @RequestBody Long idUrlScheduleFile) {
        RequestUtils.getFirstRequest(principal, idUrlScheduleFile);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = scheduleService.deleteScheduleFileById(idSchoolLogin, idUrlScheduleFile);
        return NewDataResponse.setDataDelete(check);

    }

    /**
     * Downloadn file Cách 1
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/download1", method = RequestMethod.GET)
    public void download1(HttpServletResponse response) throws IOException {
        try {
            File file = new File(context.getRealPath("/file/demo.txt"));
            byte[] data = FileUtils.readFileToByteArray(file);
            // Thiết lập thông tin trả về
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
            response.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Downlaod file cách 2
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download2/{idUrlScheduleFile}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlScheduleFile") Long idUrlScheuldeFile) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        UrlScheuldeFile urlScheuldeFile = urlScheduleFileRepository.findById(idUrlScheuldeFile).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(urlScheuldeFile.getUrlLocalFile())) {
                file = new File(urlScheuldeFile.getUrlLocalFile());
            } else if (StringUtils.isNotBlank(urlScheuldeFile.getUrlLocalPicture())) {
                file = new File(urlScheuldeFile.getUrlLocalPicture());
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


    @PostMapping("/title-class")
    public ResponseEntity updateTitleClass(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateTitleClassRequest createTitleClassRequest) {
        RequestUtils.getFirstRequest(principal, createTitleClassRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = scheduleService.saveScheduleTitleClass(idSchoolLogin, createTitleClassRequest);
        return NewDataResponse.setDataUpdate(check);

    }

    /**
     * tạo file, ảnh cho nhiều lớp nhiều tuần
     *
     * @param principal
     * @param
     * @return
     */
    @PostMapping("/file-and-picture-multi-class")
    public ResponseEntity createFileAndPictureMultiClass(@CurrentUser UserPrincipal principal, @ModelAttribute CreateFileAndPictureMenuMultiClassRequest fileAndPictureMenuMultiClassRequest) throws IOException {
        RequestUtils.getFirstRequest(principal, fileAndPictureMenuMultiClassRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean data = scheduleService.createFileAndPictureMultiClass(principal, fileAndPictureMenuMultiClassRequest);
        return NewDataResponse.setDataCustom(data,MessageWebConstant.CREATE_FILE_PICTURE);
    }
}
