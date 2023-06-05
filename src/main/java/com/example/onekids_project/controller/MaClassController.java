package com.example.onekids_project.controller;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.request.classes.ClassSearchNewRequest;
import com.example.onekids_project.request.classes.CreateMaClassRequest;
import com.example.onekids_project.request.classes.UpdateMaClassRequest;
import com.example.onekids_project.response.classes.*;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.ExEmployeeClassService;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.serviceimpl.ListExEmployeeClassRequest;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web/class")
public class MaClassController {

    private static final Logger logger = LoggerFactory.getLogger(MaClassController.class);

    @Autowired
    private MaClassService maClassService;

    @Autowired
    private ExEmployeeClassService exEmployeeClassService;

    @RequestMapping(method = RequestMethod.GET, value = "/search/new")
    public ResponseEntity searchNewClass(@CurrentUser UserPrincipal principal, @Valid ClassSearchNewRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListClassNewResponse response = maClassService.searchClassNew(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new/{id}")
    public ResponseEntity getMaClassById(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        MaClassNewResponse response = maClassService.findClassNewById(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/teacher/{id}")
    public ResponseEntity getTeacherInClass(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<TeacherInClassResponse> responseList = maClassService.findTeacherInClass(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/employee/{id}")
    public ResponseEntity getEmployeeInClass(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<EmployeeInClassResponse> responseList = maClassService.findEmployeeInClass(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }


    /**
     * tìm kiếm lớp học theo id
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        Optional<MaClassDTO> maClassDTOOptional = maClassService.findByIdMaClass(principal.getIdSchoolLogin(), id);
        MaClassDTO maClassDTO = maClassDTOOptional.get();
        maClassDTO.setGradeResponse(maClassDTO.getGrade());
        maClassDTO.setExEmployeeClassListResponse(maClassDTO.getExEmployeeClassList());
        maClassDTO.setKidsListResponse(maClassDTO.getKidsList());
        return NewDataResponse.setDataSearch(maClassDTOOptional);

    }

    /**
     * tạo mới lớp học
     *
     * @param principal
     * @param createMaClassRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateMaClassRequest createMaClassRequest) {
        RequestUtils.getFirstRequest(principal, createMaClassRequest);
        CommonValidate.checkDataPlus(principal);
        MaClassResponse maClassResponse = maClassService.createMaClass(principal, createMaClassRequest);
        return NewDataResponse.setDataCreate(maClassResponse);
    }

    /**
     * cập nhật lớp học theo id
     *
     * @param id
     * @param principal
     * @param updateMaClassRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateMaClassRequest updateMaClassRequest) {
        RequestUtils.getFirstRequest(principal, updateMaClassRequest);
        CommonValidate.checkDataPlus(principal);
        boolean maClassResponse = maClassService.updateMaClass(principal, updateMaClassRequest);
        return NewDataResponse.setDataUpdate(maClassResponse);
    }

    /**
     * xóa lớp học theo id
     *
     * @param id
     * @param principal
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        boolean checkDelete = maClassService.deleteMaClass(principal, id);
        return NewDataResponse.setDataCustom(checkDelete, MessageWebConstant.DELETE_CLASS_SUCCESS);
    }

    /**
     * tìm kiếm danh sách giáo viên trong class
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/class-employee/{id}")
    public ResponseEntity findByIdExEmployeeClass(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<ExEmployeeClassResponse> exEmployeeClassResponseList = exEmployeeClassService.findByIdExEmployeeClass(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(exEmployeeClassResponseList);
    }

    @Deprecated
    @RequestMapping(method = RequestMethod.PUT, path = "/class-employee")
    public ResponseEntity updateExEmployeeClass(@CurrentUser UserPrincipal principal, @Valid @RequestBody ListExEmployeeClassRequest listExEmployeeClassRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkUpdate = exEmployeeClassService.updateExEmployeeClass(idSchoolLogin, listExEmployeeClassRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }
}
