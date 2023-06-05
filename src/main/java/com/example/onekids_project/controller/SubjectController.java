package com.example.onekids_project.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.SubjectDTO;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.subject.CreateSubjectRequest;
import com.example.onekids_project.request.subject.UpdateSubjectRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.subject.CreateSubjectResponse;
import com.example.onekids_project.response.subject.ListSubjectResponse;
import com.example.onekids_project.response.subject.UpdateSubjectResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SubjectService;
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
import java.util.Optional;

@RestController
@RequestMapping("web/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectService subjectService;

    /**
     * Tìm kiếm môn học
     *
     * @param principal
     * @param baseRequest
     * @return
     */
    @GetMapping
    public ResponseEntity search(@CurrentUser UserPrincipal principal, BaseRequest baseRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        //lấy số trang
        int pageNumber = ConvertData.getPageNumber(baseRequest.getPageNumber());
        if (pageNumber == -1) {
            logger.error("Số trang không hợp lệ");
            return DataResponse.getData("Số trang không hợp lệ", HttpStatus.NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(pageNumber, AppConstant.MAX_PAGE_ITEM);

        //thực hiện tìm kiếm theo id
        ListSubjectResponse listSubjectResponse = subjectService.findAllSubject(pageable, idSchoolLogin);
        return NewDataResponse.setDataSearch(listSubjectResponse);
    }

    /**
     * Tìm kiếm môn học theo Id
     *
     * @param principal
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<SubjectDTO> optionalSubjectDTO = subjectService.findByIdSubject(id, idSchoolLogin);
        return NewDataResponse.setDataSearch(optionalSubjectDTO);
    }

    /**
     * Thêm mới monon học
     *
     * @param principal
     * @param createSubjectRequest
     * @return
     */
    @PostMapping
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateSubjectRequest createSubjectRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        CreateSubjectResponse createDepartmentResponse = subjectService.saveSubject(createSubjectRequest, idSchoolLogin);
        return NewDataResponse.setDataCreate(createDepartmentResponse);
    }

    /**
     * Sửa môn học
     *
     * @param id
     * @param principal
     * @param updateSubjectRequest
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateSubjectRequest updateSubjectRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        if (!updateSubjectRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSubjectRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        UpdateSubjectResponse updateSubjectResponse = subjectService.updateSubject(updateSubjectRequest, idSchoolLogin);
        return NewDataResponse.setDataUpdate(updateSubjectResponse);
    }

    /**
     * Xóa phòng ban theo Id
     *
     * @param id
     * @param principal
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = subjectService.deleteSubject(idSchoolLogin, id);
        return NewDataResponse.setDataDelete(checkDelete);
    }


}
