package com.example.onekids_project.controller.classmenu;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.classes.UrlMenuFile;
import com.example.onekids_project.importexport.service.MenuExcelService;
import com.example.onekids_project.repository.UrlMenuFileRepository;
import com.example.onekids_project.request.classmenu.*;
import com.example.onekids_project.request.schedule.ApproveStatus;
import com.example.onekids_project.response.classmenu.TabAllClassMenuInWeekResponse;
import com.example.onekids_project.response.classmenu.TabClassMenuByIdClassInWeek;
import com.example.onekids_project.response.classmenu.TabClassMenuViewDetail;
import com.example.onekids_project.response.classmenu.TabDetailClassMenuAllClassResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.classmenu.ClassMenuService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/web/class-menu")
public class ClassMenuController {

    private static final Logger logger = LoggerFactory.getLogger(ClassMenuController.class);

    @Autowired
    private ClassMenuService classMenuService;

    @Autowired
    private UrlMenuFileRepository urlMenuFileRepository;

    @Autowired
    private MenuExcelService menuExcelService;


    /**
     * tìm kiếm tất cả các thực đơn của các lớp trong một trường
     *
     * @param principal
     * @param searchAllClassMenuRequest
     * @return
     */
    @GetMapping
    public ResponseEntity search(@CurrentUser UserPrincipal principal, SearchAllClassMenuRequest searchAllClassMenuRequest) {
        RequestUtils.getFirstRequest(principal, searchAllClassMenuRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
//        //check pageNumber and maxPageItem
//        Pageable pageable = null;
//        if (searchAllClassMenuRequest == null || searchAllClassMenuRequest.getMaxPageItem() != null) {
//            int pageNumber = ConvertData.getPageNumber(searchAllClassMenuRequest.getPageNumber());
//            boolean checkMaxPageItem = false;
//            if (StringUtils.isNotBlank(searchAllClassMenuRequest.getMaxPageItem())) {
//                checkMaxPageItem = RequestValidate.checkStringInNumber(searchAllClassMenuRequest.getMaxPageItem());
//            }
//            if (!checkMaxPageItem || pageNumber == -1) {
//                logger.error(AppConstant.INVALID_PAGE_NUMBER);
//                return DataResponse.getData(AppConstant.INVALID_PAGE_NUMBER, HttpStatus.BAD_REQUEST);
//            }
//            pageable = PageRequest.of(pageNumber, Integer.parseInt(searchAllClassMenuRequest.getMaxPageItem()));
//        }
        List<TabAllClassMenuInWeekResponse> tabAllClassMenuInWeekResponseList = classMenuService.getAllClassMenuMultiClassInWeek(principal, idSchoolLogin, searchAllClassMenuRequest);
        return NewDataResponse.setDataSearch(tabAllClassMenuInWeekResponseList);

    }


    /**
     * tìm kiếm thực đơn của lớp trong tuần
     *
     * @param principal
     * @param searchAllClassMenuRequest
     * @return
     */
    @GetMapping("/class-menu-idclass")
    public ResponseEntity getClassMenuClasWeek(@CurrentUser UserPrincipal principal, SearchAllClassMenuRequest searchAllClassMenuRequest) {
        RequestUtils.getFirstRequest(principal, searchAllClassMenuRequest);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
//        //check pageNumber and maxPageItem
//        Pageable pageable = null;
//        if (searchAllClassMenuRequest == null || searchAllClassMenuRequest.getMaxPageItem() != null) {
//            int pageNumber = ConvertData.getPageNumber(searchAllClassMenuRequest.getPageNumber());
//            boolean checkMaxPageItem = false;
//            if (StringUtils.isNotBlank(searchAllClassMenuRequest.getMaxPageItem())) {
//                checkMaxPageItem = RequestValidate.checkStringInNumber(searchAllClassMenuRequest.getMaxPageItem());
//            }
//            if (!checkMaxPageItem || pageNumber == -1) {
//                logger.error(AppConstant.INVALID_PAGE_NUMBER);
//                return DataResponse.getData(AppConstant.INVALID_PAGE_NUMBER, HttpStatus.BAD_REQUEST);
//            }
//            pageable = PageRequest.of(pageNumber, Integer.parseInt(searchAllClassMenuRequest.getMaxPageItem()));
//        }

        List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList = classMenuService.getClassMenuByIdClassInWeek(principal, idSchoolLogin, searchAllClassMenuRequest.getIdClass(), searchAllClassMenuRequest);
        return NewDataResponse.setDataSearch(tabClassMenuByIdClassInWeekList);

    }

    @PostMapping("/class-menu-idclass")
    public ResponseEntity createClassMenuInClassWeek(@CurrentUser UserPrincipal principal, @RequestBody List<TabClassMenuByIdClassInWeekRequest> classMenuByIdClassInWeekRequestList) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = classMenuService.createClassMenuInClassInWeek(idSchoolLogin, principal, classMenuByIdClassInWeekRequestList);
        return NewDataResponse.setDataCreate(check);

    }

    @PostMapping("/class-menu-multi-idclass")
    public ResponseEntity createClassMenuMultiClassMultiWeek(@CurrentUser UserPrincipal principal, @RequestBody CreateMultiClassMenu createMultiClassMenu) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = classMenuService.saveClassMenuMultiClassMultiWeek(idSchoolLogin, principal, createMultiClassMenu);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * Tìm kiếm thực đơn màn hình chi tiết của các lớp
     *
     * @param principal
     * @param searchAllClassMenuRequest
     * @return
     */
    @GetMapping("/all-classmenu-detail")
    public ResponseEntity findAllClassMenuDetail(@CurrentUser UserPrincipal principal, SearchAllClassMenuRequest searchAllClassMenuRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<TabDetailClassMenuAllClassResponse> tabDetailClassMenuAllClassResponseList = classMenuService.findAllClassMenuTabDetail(idSchoolLogin, searchAllClassMenuRequest);
        return NewDataResponse.setDataSearch(tabDetailClassMenuAllClassResponseList);
    }

    /**
     * Tìm kiếm thực đơn cả năm của 1 lớp theo các tuần
     *
     * @param principal
     * @param
     * @return
     */
    @GetMapping("/classmenu-view-detail/{idClass}")
    public ResponseEntity findClassMenuViewDetail(@CurrentUser UserPrincipal principal, @PathVariable Long idClass) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<TabClassMenuViewDetail> tabClassMenuViewDetailList = classMenuService.findClassMenuDetailByClass(idSchoolLogin, idClass);
        return NewDataResponse.setDataSearch(tabClassMenuViewDetailList);
    }

    @PostMapping("/file-and-picture")
    public ResponseEntity handleFileAndPicture(@CurrentUser UserPrincipal principal, @ModelAttribute CreateFileAndPictureMenuRequest createFileAndPictureMenuRequest) throws IOException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = classMenuService.saveMenuFile(idSchoolLogin, principal, createFileAndPictureMenuRequest);
        return NewDataResponse.setDataCreate(check);
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
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), fileAndPictureMenuMultiClassRequest);
        boolean data = classMenuService.createFileAndPictureMultiClass(principal, fileAndPictureMenuMultiClassRequest);
        return NewDataResponse.setMessage(MessageWebConstant.CREATE_FILE_PICTURE);
    }


    @PutMapping("/approve")
    public ResponseEntity updateApprove(@CurrentUser UserPrincipal principal, @RequestBody ApproveStatus approveStatus) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = classMenuService.updateApprove(idSchoolLogin, approveStatus);
        return NewDataResponse.setDataUpdate(check);
    }

    /*
     * Xuất file excel thực đơn menu lớp
     *
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-menu-class")
    public ResponseEntity exportClassMenu(@CurrentUser UserPrincipal principal, @Valid SearchAllClassMenuRequest searchAllClassMenuRequest) {
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (idSchoolLogin == null || idSchoolLogin <= 0) {
                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
            }
            if (searchAllClassMenuRequest.getIdClass() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                List<TabClassMenuByIdClassInWeek> tabClassMenuByIdClassInWeekList = classMenuService.getClassMenuByIdClassInWeek(principal, idSchoolLogin, searchAllClassMenuRequest.getIdClass(), searchAllClassMenuRequest);

                if (CollectionUtils.isEmpty(tabClassMenuByIdClassInWeekList)) {
                    logger.error("lỗi tìm kiếm thực đơn");
                    return ErrorResponse.errorData("Không thể tìm kiếm thực đơn", "Không thể tìm kiếm thực đơn", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                ByteArrayInputStream in = null;

                try {
                    LocalDate date = LocalDate.parse(searchAllClassMenuRequest.getTimeClassMenu(), formatter);
                    in = menuExcelService.customMenuExcel(tabClassMenuByIdClassInWeekList, idSchoolLogin, searchAllClassMenuRequest.getIdClass(), date);
                } catch (IOException e) {
                    logger.error("Lỗi xuất file thực đơn");
                }
                logger.info("xuất file thực đơn thành công");
                return ResponseEntity.ok().body(new InputStreamResource(in)); // chưa trả được DataRespone Custom
            } else {
                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);

            }


        } catch (Exception e) {
            logger.error("Exception Tìm kiếm thực đơn thành công: " + e.getMessage());
            return ErrorResponse.errorData(e.getMessage(), "Tìm kiếm thời thực đơn không thành công", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Xuất file excel thực đơn menu lớp NEW
     *
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-menu-class-new")
    public ResponseEntity exportClassMenuNew(@CurrentUser UserPrincipal principal, @Valid SearchAllClassMenuRequest searchAllClassMenuRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<ExcelResponse> list = classMenuService.getClassMenuByIdClassInWeekNew(idSchoolLogin, searchAllClassMenuRequest);
//            LocalDate date = LocalDate.parse(searchAllClassMenuRequest.getTimeClassMenu(), formatter);
        return NewDataResponse.setDataSearch(list); // chưa trả được DataRespone Custom
    }

    /**
     * Xóa file
     *
     * @param principal
     * @param idUrlMenuFile
     * @return
     */
    @PutMapping("/menu-file")
    public ResponseEntity deleteClassMenuFile(@CurrentUser UserPrincipal principal, @RequestBody Long idUrlMenuFile) {
        RequestUtils.getFirstRequest(principal, idUrlMenuFile);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean check = classMenuService.deletMenuFileById(idSchoolLogin, idUrlMenuFile);
        return NewDataResponse.setDataDelete(check);

    }

    /**
     * Downlaod file cách 2
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download2/{idUrlMenuFile}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlMenuFile") Long idUrlMenuFile) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        UrlMenuFile urlMenuFile = urlMenuFileRepository.findById(idUrlMenuFile).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(urlMenuFile.getUrlLocalFile())) {
                file = new File(urlMenuFile.getUrlLocalFile());
            } else if (StringUtils.isNotBlank(urlMenuFile.getUrlLocalPicture())) {
                file = new File(urlMenuFile.getUrlLocalPicture());
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

    @PostMapping("/import-menu-excel")
    public ResponseEntity importFileExcel(@CurrentUser UserPrincipal principal, @ModelAttribute CreateFileExcelRequest createFileExcelRequest) throws IOException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        CreateMultiClassMenu createMultiClassMenu = menuExcelService.saveMenuFileExcel(idSchoolLogin, createFileExcelRequest);
        if (createMultiClassMenu == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File thực đơn không đúng Form nhập liệu!");
        }
        boolean check = classMenuService.saveClassMenuMultiClassMultiWeek(idSchoolLogin, principal, createMultiClassMenu);
        return NewDataResponse.setDataCreate(check);

    }

    @PutMapping("/delete-content-menu")
    public ResponseEntity deleteContentMenu(@CurrentUser UserPrincipal principal, @RequestBody List<SearchAllClassMenuRequest> searchAllClassMenuRequests) {
        boolean check = classMenuService.deleteContentMenu(searchAllClassMenuRequests);
        return NewDataResponse.setDataUpdate(check);
    }
}
