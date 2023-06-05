package com.example.onekids_project.controller;

import com.example.onekids_project.annotation.CommonAspectAnnotation;
import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.grade.CreateGradeRequest;
import com.example.onekids_project.request.grade.UpdateGradeRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.grade.GradeResponse;
import com.example.onekids_project.response.grade.ListGradeResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.GradeService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/web/grade")
public class GradeController {

    private static final Logger logger = LoggerFactory.getLogger(GradeController.class);

    @Autowired
    private GradeService gradeService;

    /**
     * tìm kiếm tất cả các khối trong một trường
     *
     * @param request
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, PageNumberWebRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListGradeResponse listGradeResponse = gradeService.findAllGrade(idSchoolLogin, request);
        //set classes for a grade: chỉ lấy ra các lớp chưa bị xóa và thuộc trường đó
        listGradeResponse.getGradeList().forEach(grade -> {
            List<MaClassDTO> maClassList = grade.getMaClassList().stream().filter(x -> x.isDelActive() && idSchoolLogin.equals(x.getIdSchool())).collect(Collectors.toList());
            grade.setMaClassListResponse(maClassList);
        });
        return NewDataResponse.setDataSearch(listGradeResponse);

    }

    /**
     * tìm kiếm khối theo id
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        Optional<GradeDTO> gradeDTOOptional = gradeService.findByIdGrade(principal.getIdSchoolLogin(), id);
        gradeDTOOptional.get().setMaClassListResponse(gradeDTOOptional.get().getMaClassList());
        return NewDataResponse.setDataSearch(gradeDTOOptional);

    }

//    /**
//     * tìm kiếm khối học tùy chọn
//     *
//     * @param principal
//     * @param searchGradeRequest
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.GET, value = "/search")
//    public ResponseEntity search(@CurrentUser UserPrincipal principal, SearchGradeRequest searchGradeRequest) {
//        RequestUtils.getFirstRequest(principal, searchGradeRequest);
//        Long idSchoolLogin = principal.getIdSchoolLogin();
//        //check pageNumber and maxPageItem
////        Pageable pageable = null;
////        if (searchGradeRequest == null || searchGradeRequest.getMaxPageItem() != null) {
////            int pageNumber = ConvertData.getPageNumber(searchGradeRequest.getPageNumber());
////            boolean checkMaxPageItem = false;
////            if (StringUtils.isNotBlank(searchGradeRequest.getMaxPageItem())) {
////                checkMaxPageItem = RequestValidate.checkStringInNumber(searchGradeRequest.getMaxPageItem());
////            }
////            if (!checkMaxPageItem || pageNumber == -1) {
////                logger.error(AppConstant.INVALID_PAGE_NUMBER);
////                return DataResponse.getData(AppConstant.INVALID_PAGE_NUMBER, HttpStatus.BAD_REQUEST);
////            }
////            pageable = PageRequest.of(pageNumber, Integer.parseInt(searchGradeRequest.getMaxPageItem()));
////        }
//        ListGradeResponse listGradeResponse = gradeService.searchGrade(idSchoolLogin, pageable, searchGradeRequest);
//        listGradeResponse.getGradeList().forEach(grade -> {
//            grade.setMaClassListResponse(grade.getMaClassList());
//        });
//        return NewDataResponse.setDataSearch(listGradeResponse);
//
//    }

    /**
     * thêm mới khối học
     *
     * @param principal
     * @param gradeAddRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateGradeRequest gradeAddRequest) {
        RequestUtils.getFirstRequest(principal, gradeAddRequest);
        CommonValidate.checkDataPlus(principal);
        GradeResponse gradeAddResponse = gradeService.createGrade(principal, principal.getIdSchoolLogin(), gradeAddRequest);
        return NewDataResponse.setDataCreate(gradeAddResponse);

    }

    /**
     * Sửa khối học
     *
     * @param id
     * @param principal
     * @param gradeEditRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateGradeRequest gradeEditRequest) {
        RequestUtils.getFirstRequest(principal, gradeEditRequest);
        CommonValidate.checkDataPlus(principal);
        GradeResponse gradeResponse = gradeService.updateGrade(principal, principal.getIdSchoolLogin(), gradeEditRequest);
        return NewDataResponse.setDataUpdate(gradeResponse);

    }

    /**
     * xóa khối học theo id
     *
     * @param id
     * @param principal
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = gradeService.deleteGrade(principal, idSchoolLogin, id);
        return NewDataResponse.setDataDelete(checkDelete);

    }
}
