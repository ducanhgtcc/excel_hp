package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.KidsHeightWeightDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.importexport.model.HeightWeightModel;
import com.example.onekids_project.importexport.service.KidsExcelService;
import com.example.onekids_project.request.kidsheightweight.CreateKidsHeightWeightRequest;
import com.example.onekids_project.request.kidsheightweight.SearchKidsHeightWeightRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.kidsheightweight.*;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/web/kid-quality")
public class HeightWeightController {
    private static final Logger logger = LoggerFactory.getLogger(HeightWeightController.class);

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private MaClassService maClassService;

    @Autowired
    private KidsExcelService kidToExcelService;

    @Autowired
    private HeightWeightService heightWeightService;

    @Autowired
    private HeightService heightService;

    @Autowired
    private WeightService weightService;

    // find history
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findHistoryHeightWeight(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        KidsHeightWeightDTO kidsHeightWeightDTOOptional = heightWeightService.findByIdHeightWeight(principal.getIdSchoolLogin(), id).orElseThrow(() -> new NotFoundException("not found HistoryHeightWeight by id"));
        List<KidsHeightResponse> kidsHeightResponseList = kidsHeightWeightDTOOptional.getKidsHeightList().stream().filter(KidsHeightResponse::isDelActive).collect(Collectors.toList());
        List<KidsWeightResponse> kidsWeightResponseList = kidsHeightWeightDTOOptional.getKidsWeightList().stream().filter(KidsWeightResponse::isDelActive).collect(Collectors.toList());
        kidsHeightWeightDTOOptional.setKidsHeightList(kidsHeightResponseList);
        kidsHeightWeightDTOOptional.setKidsWeightList(kidsWeightResponseList);
        return NewDataResponse.setDataSearch(kidsHeightWeightDTOOptional);
    }

    // find by day
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findAllWeightHeightDay(@CurrentUser UserPrincipal principal, @Valid SearchKidsHeightWeightRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListKidsHeightWeightResponse responseList = heightWeightService.searchAddHeightWeight(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    // create
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createHeightWeight(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateKidsHeightWeightRequest createKidsHeightWeightRequest) {
        RequestUtils.getFirstRequest(principal, createKidsHeightWeightRequest);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        heightService.createKidsHeight(idSchoolLogin, principal, createKidsHeightWeightRequest);
        weightService.createKidsWeight(idSchoolLogin, principal, createKidsHeightWeightRequest);
        return NewDataResponse.setDataCustom(null, "Thêm mới số đo thành công");
    }

    @DeleteMapping("/weight/{id}")
    public ResponseEntity deleteWeight(
            @PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = weightService.deleteKidsWeight(idSchoolLogin, id);
        return NewDataResponse.setDataDelete(checkDelete);

    }

    @DeleteMapping("/height/{id}")
    public ResponseEntity deleteHeight(
            @PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = heightService.deleteKidsHeight(idSchoolLogin, id);
        return NewDataResponse.setDataDelete(checkDelete);

    }

//    // bang tieu chuan
//    @RequestMapping(method = RequestMethod.GET, value = "/heightsample")
//    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
//        Long idSchoolLogin = principal.getIdSchoolLogin();
//        int pageNumber = ConvertData.getPageNumber(baseRequest.getPageNumber());
//        if (pageNumber == -1) {
//            logger.error("Số trang không hợp lệ");
//            return DataResponse.getData("Số trang không hợp lệ", HttpStatus.BAD_REQUEST);
//        }
//        Pageable pageable = PageRequest.of(pageNumber, AppConstant.MAX_PAGE_ITEM);
//        ListHeightSampleResponse listHeightSampleResponse =
//                heightService.findHeightSample(idSchoolLogin, pageable, principal.getId());
//        return NewDataResponse.setDataSearch(listHeightSampleResponse);
//    }

//    // bang tieu chuan
//    @RequestMapping(method = RequestMethod.GET, value = "/weightsample")
//    public ResponseEntity findAllaaa(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
//        Long idSchoolLogin = principal.getIdSchoolLogin();
//        int pageNumber = ConvertData.getPageNumber(baseRequest.getPageNumber());
//        if (pageNumber == -1) {
//            logger.error("Số trang không hợp lệ");
//            return DataResponse.getData("Số trang không hợp lệ", HttpStatus.BAD_REQUEST);
//        }
//        Pageable pageable = PageRequest.of(pageNumber, AppConstant.MAX_PAGE_ITEM);
//        ListWeightSampleResponse listWeightSampleResponse = weightService.findWeightSample(idSchoolLogin, pageable, principal.getId());
//        return NewDataResponse.setDataSearch(listWeightSampleResponse);
//    }

    /**
     * export excel chiều cao cân nặng mới
     *
     * @param principal
     * @param searchKidsHeightWeightRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-height-weight-new")
    public ResponseEntity findMenuWeightHeightDay(
            @CurrentUser UserPrincipal principal,
            SearchKidsHeightWeightRequest searchKidsHeightWeightRequest) {
        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (idSchoolLogin == null || idSchoolLogin <= 0) {
                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
            }
            int pageNumber = ConvertData.getPageNumber(searchKidsHeightWeightRequest.getPageNumber());
            if (pageNumber == -1) {
                logger.error("Số trang không hợp lệ");
                return DataResponse.getData("Số trang không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            if (searchKidsHeightWeightRequest.getIdKidsList() != null) {
                logger.info("Thành công");
                ListKidsHeightWeightResponse listKidsHeightWeightResponse = heightWeightService.searchMenuHeightWeight(idSchoolLogin, searchKidsHeightWeightRequest);

                SchoolResponse schoolResponse = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
                String nameSchool = schoolResponse.getSchoolName();

                MaClassDTO classDTO = maClassService.findByIdMaClass(idSchoolLogin, searchKidsHeightWeightRequest.getIdClass()).stream().findFirst().orElse(null);
                String className = "";
                if (classDTO != null) {
                    className = classDTO.getClassName();
                }

                if (listKidsHeightWeightResponse == null) {
                    logger.warn("Không có ");
                    return DataResponse.getData("Không có", HttpStatus.NOT_FOUND);
                }
                List<HeightWeightModel> list = heightWeightService.getFileAllKidByHeightWeight(listKidsHeightWeightResponse, nameSchool);
                ByteArrayInputStream in = null;
                in = kidToExcelService.customKidsToExcelHeightWeight(list, nameSchool, className);
                logger.info("Tìm kiếm học sinh thành công");
                return ResponseEntity.ok()
                        .body(new InputStreamResource(in)); // chưa trả được về kiểu DataReponse Custom
            } else {
                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception tìm kiếm tất cả : " + e.getMessage());
            return ErrorResponse.errorData(
                    e.getMessage(), "Lỗi hệ thống tìm kiếm ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * export excel chiều cao cân nặng mới NEW
     *
     * @param principal
     * @param searchKidsHeightWeightRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-height-weight-new-new")
    public ResponseEntity findMenuWeightHeightDayNew(@CurrentUser UserPrincipal principal, SearchKidsHeightWeightRequest searchKidsHeightWeightRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListKidsHeightWeightResponse listKidsHeightWeightResponse = heightWeightService.searchMenuHeightWeight(idSchoolLogin, searchKidsHeightWeightRequest);
        List<ExcelResponse> list = heightWeightService.getFileAllKidByHeightWeightNew(listKidsHeightWeightResponse, idSchoolLogin, searchKidsHeightWeightRequest.getIdClass());
        return NewDataResponse.setDataSearch(list);
    }

    /**
     * export excel all chiều cao cân của học sinh
     *
     * @param principal
     * @param searchKidsHeightWeightRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-height-weight-all")
    public ResponseEntity findMenuWeightHeightDayAll(
            @CurrentUser UserPrincipal principal,
            SearchKidsHeightWeightRequest searchKidsHeightWeightRequest) {

        try {
            Long idSchoolLogin = principal.getIdSchoolLogin();
            if (idSchoolLogin == null || idSchoolLogin <= 0) {
                logger.error(AppConstant.ID_SCHOOL_LOGIN_ERROR);
                return ErrorResponse.errorData(AppConstant.ID_SCHOOL_LOGIN_ERROR, AppConstant.ID_SCHOOL_LOGIN_CONTENT, HttpStatus.NOT_ACCEPTABLE);
            }
            int pageNumber = ConvertData.getPageNumber(searchKidsHeightWeightRequest.getPageNumber());
            if (pageNumber == -1) {
                logger.error("Số trang không hợp lệ");
                return DataResponse.getData("Số trang không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            if (searchKidsHeightWeightRequest.getIdKidsList() != null) {
                logger.info("Thành công");

                List<KidsHeightWeightResponse> listKids = heightWeightService.findKidsHeightWeightToExcel(idSchoolLogin, searchKidsHeightWeightRequest);

                List<Long> listIdKidDTOS = searchKidsHeightWeightRequest.getIdKidsList();
                List<HeightWeightModel> model = heightWeightService.detachedListKidsHeightWeightAll(listKids, listIdKidDTOS);
                ByteArrayInputStream in = null;
                in = kidToExcelService.customKidsToExcelAllHeightWeight(model, idSchoolLogin, searchKidsHeightWeightRequest.getIdClass());
                logger.info("Tìm kiếm học sinh thành công");
                return ResponseEntity.ok().body(new InputStreamResource(in)); // chưa trả được về kiểu DataReponse Custom
            } else {
                logger.info("Thất bại");
                return ErrorResponse.errorData("không có dữ liệu truyền vào", "không có dữ liệu truyền vào", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception tìm kiếm tất cả : " + e.getMessage());
            return ErrorResponse.errorData(
                    e.getMessage(), "Lỗi hệ thống tìm kiếm ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * export excel all chiều cao cân của học sinh NEW
     *
     * @param principal
     * @param searchKidsHeightWeightRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export-height-weight-all-new")
    public ResponseEntity findMenuWeightHeightDayAllNew(@CurrentUser UserPrincipal principal, SearchKidsHeightWeightRequest searchKidsHeightWeightRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<KidsHeightWeightResponse> listKids = heightWeightService.findKidsHeightWeightToExcel(idSchoolLogin, searchKidsHeightWeightRequest);
        List<Long> listIdKidDTOS = searchKidsHeightWeightRequest.getIdKidsList();
        List<ExcelResponse> model = heightWeightService.detachedListKidsHeightWeightAllNew(listKids, listIdKidDTOS, idSchoolLogin, searchKidsHeightWeightRequest.getIdClass());
        return NewDataResponse.setDataSearch(model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sample/{id}")
    public ResponseEntity findHeightSamplea(@PathVariable("id") Long idKid) {
        List<HeightWeightSampleRespone> responseList = heightWeightService.findHeightWeightSample(idKid);
        return NewDataResponse.setDataCustom(responseList, "Tìm kiếm chiều cao, cân nặng tiêu chuẩn thành công");
    }
}
