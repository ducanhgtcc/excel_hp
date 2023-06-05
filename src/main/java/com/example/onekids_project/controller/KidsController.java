package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.importexport.model.KidModelImport;
import com.example.onekids_project.importexport.model.KidModelImportFail;
import com.example.onekids_project.importexport.model.ListKidModelImport;
import com.example.onekids_project.importexport.service.KidsExcelService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.request.common.StatusCommonRequest;
import com.example.onekids_project.request.createnotifyschool.CreateStudentNotify;
import com.example.onekids_project.request.kids.*;
import com.example.onekids_project.request.kids.transfer.KidsTransferCreateRequest;
import com.example.onekids_project.request.kids.transfer.KidsTransferUpdateRequest;
import com.example.onekids_project.request.kids.transfer.SearchKidsTransferRequest;
import com.example.onekids_project.response.classes.GradeClassNameResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.groupout.GroupOutNameResponse;
import com.example.onekids_project.response.kids.KidResponse;
import com.example.onekids_project.response.kids.KidStatusResponse;
import com.example.onekids_project.response.kids.KidsExtraInfoResponse;
import com.example.onekids_project.response.kids.ListStudentResponse;
import com.example.onekids_project.response.school.ListAppIconResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.common.FindSmsService;
import com.example.onekids_project.service.servicecustom.groupout.GroupOutKidsService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.util.StudentUtil;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/web/student")
public class KidsController {

    private static final Logger logger = LoggerFactory.getLogger(KidsController.class);

    @Autowired
    private KidsService kidsService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AppIconParentService appIconParentService;

    @Autowired
    private AppIconParentAddSerivce appIconParentAddSerivce;

    @Autowired
    private KidsExtraInfoService kidsExtraInfoService;

    @Autowired
    private KidsExcelService kidToExcelService;

    @Autowired
    private FindSmsService findSmsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private GroupOutKidsService groupOutKidsService;

    @Autowired
    private MaClassService maClassService;

//    /**
//     * tìm kiếm tất cả học sinh
//     *
//     * @param principal
//     * @param baseRequest
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
//        RequestUtils.getFirstRequestPlus(principal);
//        Pageable pageable = null;
//        if (baseRequest == null || baseRequest.getMaxPageItem() != null) {
//            int pageNumber = ConvertData.getPageNumber(baseRequest.getPageNumber());
//            boolean checkMaxPageItem = false;
//            if (StringUtils.isNotBlank(baseRequest.getMaxPageItem())) {
//                checkMaxPageItem = RequestValidate.checkStringInNumber(baseRequest.getMaxPageItem());
//            }
//            if (!checkMaxPageItem || pageNumber == -1) {
//                logger.error(AppConstant.INVALID_PAGE_NUMBER);
//                return DataResponse.getData(AppConstant.INVALID_PAGE_NUMBER, HttpStatus.BAD_REQUEST);
//            }
//
//            pageable = PageRequest.of(pageNumber, Integer.parseInt(baseRequest.getMaxPageItem()));
//        }
//        ListKidsResponse listStudentResponse = kidsService.findAllKids(principal.getIdSchoolLogin(), pageable);
//        return NewDataResponse.setDataSearch(listStudentResponse);
//
//    }

    /**
     * tìm kiếm học sinh theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        Optional<KidsDTO> kidsDTOOptional = kidsService.findByIdKid(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(kidsDTOOptional);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/search/new")
    public ResponseEntity searchNew(@CurrentUser UserPrincipal principal, @Valid SearchKidsRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListStudentResponse data = kidsService.searchKidsService(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * tạo học sinh
     *
     * @param principal
     * @param createStudentRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateKidsRequest createStudentRequest) {
        RequestUtils.getFirstRequestPlus(principal, createStudentRequest);
        boolean data = kidsService.createKids(principal, createStudentRequest);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.CREATE_STUDENT);
    }

    /**
     * create avatar
     *
     * @param principal
     * @param multipartFile
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/avatar")
    public ResponseEntity createAvatar(@CurrentUser UserPrincipal principal, @ModelAttribute MultipartFile multipartFile) throws IOException {
        RequestUtils.getFirstRequestPlus(principal);
        kidsService.createAvatar(principal, multipartFile);
        return NewDataResponse.setDataCreate(null);
    }


    /**
     * xóa học sinh theo id
     *
     * @param id
     * @param principal
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        boolean check = kidsService.deleteKids(principal, id);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.DELETE_STUDENT);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/transfer/search")
    public ResponseEntity searchKidsTransfer(@CurrentUser UserPrincipal principal, @Valid SearchKidsTransferRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        return kidsService.searchKidsTransferService(principal, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transfer")
    public ResponseEntity searchKidsTransfer(@CurrentUser UserPrincipal principal, @RequestParam Long idKid) {
        RequestUtils.getFirstRequestPlus(principal, idKid);
        return kidsService.searchKidsTransferByIdService(principal, idKid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/transfer")
    public ResponseEntity kidsTransferCreate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute KidsTransferCreateRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, request);
        kidsService.kidsTransferCreateService(principal, request);
        return NewDataResponse.setDataCreate(true);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transfer")
    public ResponseEntity kidsTransferUpdate(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute KidsTransferUpdateRequest request) throws IOException {
        RequestUtils.getFirstRequestPlus(principal, request);
        kidsService.kidsTransferUpdateService(principal, request);
        return NewDataResponse.setDataUpdate(true);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transfer/in/{id}")
    public ResponseEntity kidsTransferInStatusUpdate(@CurrentUser UserPrincipal principal, @PathVariable Long id, @RequestParam Boolean status) {
        RequestUtils.getFirstRequestPlus(principal, id, status);
        kidsService.kidsTransferInStatusByIdService(principal, id, status);
        return NewDataResponse.setDataUpdate(true);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/transfer/out/{id}")
    public ResponseEntity kidsTransferOutStatusUpdate(@CurrentUser UserPrincipal principal, @PathVariable Long id, @RequestParam Boolean status) {
        RequestUtils.getFirstRequestPlus(principal, id, status);
        kidsService.kidsTransferOutStatusByIdService(principal, id, status);
        return NewDataResponse.setDataUpdate(true);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/transfer")
    public ResponseEntity kidsTransferDeleteById(@CurrentUser UserPrincipal principal, @RequestParam Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        kidsService.kidsTransferDeleteByIdService(principal, id);
        return NewDataResponse.setDataDelete(true);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/transfer/kids")
    public ResponseEntity kidsTransferDeleteByKidsList(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idKidList) {
        RequestUtils.getFirstRequestPlus(principal, idKidList);
        kidsService.kidsTransferDeleteByIdKidListService(principal, idKidList);
        return NewDataResponse.setDataDelete(true);
    }

    /**
     * tìm kiếm thông tin mở rộng mới học sinh
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "extra/{id}")
    public ResponseEntity findByIdExtra(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        KidResponse kidResponse = kidsService.findIdKidExtra(id);
        return NewDataResponse.setDataSearch(kidResponse);
    }

    /**
     * cập nhật thông tin mở rộng của một học sinh
     *
     * @param id
     * @param principal
     * @param updateStudentRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/extra/{id}")
    public ResponseEntity updateKids(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateKidsRequest updateStudentRequest) {
        RequestUtils.getFirstRequestPlus(principal, updateStudentRequest);
        boolean kidResponse = kidsService.updateKids(principal, id, updateStudentRequest);
        return NewDataResponse.setDataCustom(kidResponse, MessageWebConstant.UPDATE_STUDENT);
    }

    /**
     * tìm kiếm trạng thái của học sinh
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kid-status")
    public ResponseEntity getKidStatus() {
        List<KidStatusResponse> kidStatusResponseList = StudentUtil.getKidStatus();
        return NewDataResponse.setDataSearch(kidStatusResponseList);
    }

    /**
     * cập nhật is_active cho một học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/one-actived")
    public ResponseEntity updateOneKidsActive(@CurrentUser UserPrincipal principal, @Valid @RequestBody ActivedOneKidsRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean checkUpdate = kidsService.updateActiveOneKids(principal.getIdSchoolLogin(), request);
        String message = request.getCheckOneActive() ? MessageWebConstant.UPDATE_ACTIVE_ACCOUNT : MessageWebConstant.UPDATE_UNACTIVE_ACCOUNT;
        return NewDataResponse.setDataCustom(checkUpdate, message);
    }

    /**
     * kích hoạt sms cho một học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/one-actived-sms")
    public ResponseEntity updateOneKidsActiveSMS(@CurrentUser UserPrincipal principal, @Valid @RequestBody ActivedOneKidsSMSRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean checkUpdate = kidsService.updateActiveKidsSMS(request);
        String message = request.getCheckOneActiveSMS() ? MessageWebConstant.UPDATE_ACTIVE_SMS : MessageWebConstant.UPDATE_UNACTIVE_SMS;
        return NewDataResponse.setDataCustom(checkUpdate, message);
    }

    /**
     * tìm kiếm extra info
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/extra-info/{id}")
    public ResponseEntity getExtraInfo(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<KidsExtraInfoResponse> kidsExtraInfoResponseOptional = kidsExtraInfoService.findByIdKidsExtraInfo(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(kidsExtraInfoResponseOptional);

    }

    /**
     * tìm kiếm icon parent cho trường học tạo mới
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/icon-parent-create")
    public ResponseEntity getIconParentCreate(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAppIconResponse listAppIconResponse = appIconParentAddSerivce.findAppIconParentAddCreate(idSchoolLogin);
        return NewDataResponse.setDataSearch(listAppIconResponse);

    }

    /**
     * tìm kiếm icon parent theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/icon-parent-update/{id}")
    public ResponseEntity getIconParentUpdate(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListAppIconResponse listAppIconResponse = appIconParentAddSerivce.findAppIconParentAddUpdate(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(listAppIconResponse);
    }

//    /**
//     * export excel học sinh theo khối
//     *
//     * @param searchKidsExportRequest
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.GET, value = "/list-by-grade-class")
//    public ResponseEntity getAllKidByGradeClass(@CurrentUser UserPrincipal principal, @Valid SearchKidsExportRequest searchKidsExportRequest) {
//        RequestUtils.getFirstRequestPlus(principal, searchKidsExportRequest);
//        Long idSchoolLogin = principal.getIdSchoolLogin();
//        if (idSchoolLogin == null || idSchoolLogin <= 0) {
//            logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
//            return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
//        }
//        if (searchKidsExportRequest.getIdGrade() != null || searchKidsExportRequest.getIdClass() != null || searchKidsExportRequest.getIdKidsList() != null || searchKidsExportRequest.getIdGroup() != null) {
//
//            List<KidsExportResponse> listKids = kidsService.searchKidsByGradeClass(idSchoolLogin, searchKidsExportRequest);
//
//            SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
//            assert schoolResponse != null;
//            String nameSchool = schoolResponse.getSchoolName();
//            if (listKids == null || listKids.size() == 0) {
//                logger.error("lỗi tìm kiếm học sinh");
//                return ErrorResponse.errorData("Không thể tìm kiếm học sinh", "Không thể tìm kiếm học sinh", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//            List<KidModel> list = kidsService.getFileAllKidByGrade(listKids, nameSchool);
//            ByteArrayInputStream in = null;
//
//            try {
//                in = kidToExcelService.customKidsToExcel(list, nameSchool);
//
//            } catch (IOException e) {
//                logger.info("Lỗi" + e.getMessage());
//            }
//            logger.info("Tìm kiếm học sinh thành công");
//            return ResponseEntity.ok().body(new InputStreamResource(in));
//        } else {
//            logger.info("Thất bại");
//            return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * export excel học sinh theo khối new
     *
     * @param searchKidsExportRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list-by-grade-class-new")
    public ResponseEntity exportExcelKidsNew(@CurrentUser UserPrincipal principal, @Valid SearchKidsExportRequest searchKidsExportRequest) {
        RequestUtils.getFirstRequestPlus(principal, searchKidsExportRequest);
        List<ExcelNewResponse> list = kidsService.getFileAllKidByGradeNew(searchKidsExportRequest, principal);
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * create student notify
     *
     * @param principal
     * @param createStudentNotify
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/student-notify")
    public ResponseEntity createNotifyStudent(@CurrentUser UserPrincipal principal, CreateStudentNotify createStudentNotify) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, createStudentNotify);
        boolean checkCreate = kidsService.createStudentNotify(principal, createStudentNotify);
        return NewDataResponse.setDataCreate(checkCreate);
    }

//    /**
//     * gửi sms học sinh
//     *
//     * @param principal
//     * @param smsNotifyRequest
//     * @return
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    @Deprecated
//    @RequestMapping(method = RequestMethod.POST, value = "/student-sms")
//    public ResponseEntity createNotifyStudentSms(@CurrentUser UserPrincipal principal, @Valid SmsNotifyRequest smsNotifyRequest) throws ExecutionException, InterruptedException {
//        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), smsNotifyRequest);
//        boolean checkCreate = kidsService.createStudentNotifySms(principal, smsNotifyRequest);
//        return NewDataResponse.setDataCustom(checkCreate, MessageConstant.SMS_SEND);
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/sms/account")
    public ResponseEntity sendAccountStudentSms(@CurrentUser UserPrincipal principal, @NotEmpty @RequestBody List<Long> idStudents) throws IOException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, idStudents);
        CommonValidate.checkDataPlus(principal);
        boolean checkCreate = kidsService.sendAccountStudentSms(principal, idStudents);
        return NewDataResponse.setDataCustom(checkCreate, MessageConstant.SMS_ACCOUNT);
    }

    /**
     * gửi sms kiểu mới
     *
     * @param principal
     * @param request
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sms")
    public ResponseEntity createStudentSms(@CurrentUser UserPrincipal principal, @Valid @RequestBody SmsStudentRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = kidsService.createStudentSmsService(principal, request);
        return NewDataResponse.setDataCreate(check);
    }

    /**
     * importExcelKids
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/import-excel-kid")
    public ResponseEntity importExcelKids(@CurrentUser UserPrincipal principal, @NotNull @ModelAttribute MultipartFileRequest multipartFileRequest) throws IOException {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        if (idSchoolLogin == null || idSchoolLogin <= 0) {
            logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
            return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
        }
        ListKidModelImport kidModelImports = kidToExcelService.importExcelKids(principal, multipartFileRequest.getMultipartFile());
        List<KidModelImportFail> kidModelImportFailList = new ArrayList<>();
        if (kidModelImports.getKidModelImportList().size() > 0) {
            List<KidModelImport> kidModelImportFail = kidsService.convertDataKids(kidModelImports.getKidModelImportList(), principal);
            kidModelImportFailList = listMapper.mapList(kidModelImportFail, KidModelImportFail.class);
        }
        kidModelImportFailList.addAll(kidModelImports.getKidModelImportFailList().stream().filter(x -> x.getBirthDay() != null).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(kidModelImportFailList)) {
            ByteArrayInputStream inp = null;
            try {
                inp = kidToExcelService.customKidsImportFailExcel(kidModelImportFailList, principal.getSchool().getSchoolName());
            } catch (IOException e) {
                logger.info("Lỗi" + e.getMessage());
            }
            logger.info("Dữ liệu học sinh từ file excel có lỗi");
            return ResponseEntity.ok().body(new InputStreamResource(inp));
        } else {
            logger.info("Tạo dữ liệu học sinh từ file excel thành công");
            return NewDataResponse.setDataCreate(MessageConstant.CREATE_KIDS_EXCEL);
        }
    }

    /**
     * importExcelKids new
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/import-excel-kid-new")
    public ResponseEntity importExcelNewKids(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateKidsExcelRequest request) {
        kidToExcelService.importExcelNewKids(principal, request);
        return NewDataResponse.setDataCreate(MessageConstant.CREATE_KIDS_EXCEL);
    }

    /**
     * cập nhật trạng thái học sinh
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-status")
    public ResponseEntity updatePackage(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusCommonRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = kidsService.updateKidsStatus(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.UPDATE_STATUS);
    }

    /**
     * Cập nhật trạng thái ra trường của học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/kids-group-out")
    public ResponseEntity updateKidsGroupOutController(@CurrentUser UserPrincipal principal, @Valid @RequestBody KidsGroupOutRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        int data = kidsService.updateKidsGroupOut(principal, request);
        return NewDataResponse.setDataUpdate(data);
    }

    /**
     * tìm kiếm danh sách thư mục trong trường học sinh trong dialog
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/kids-group-out/group-name")
    public ResponseEntity searchGroupOutName(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<GroupOutNameResponse> data = groupOutKidsService.findAllGroupName(principal);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * tìm kiếm danh sách lớp theo trường new
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/class-grade-name")
    public ResponseEntity findAllOtherNew(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<GradeClassNameResponse> responseList = maClassService.findAllMaClassOtherNew(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(responseList);
    }
}
