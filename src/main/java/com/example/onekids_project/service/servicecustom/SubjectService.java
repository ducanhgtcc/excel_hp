package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.SubjectDTO;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.classes.ClassSearchCommonRequest;
import com.example.onekids_project.request.schoolconfig.SubjectCreateRequest;
import com.example.onekids_project.request.schoolconfig.SubjectUpdateRequest;
import com.example.onekids_project.request.subject.CreateSubjectRequest;
import com.example.onekids_project.request.subject.UpdateSubjectRequest;
import com.example.onekids_project.response.schoolconfig.SubjectConfigResponse;
import com.example.onekids_project.response.schoolconfig.SubjectForClassConfigResponse;
import com.example.onekids_project.response.schoolconfig.SubjectForClassResponse;
import com.example.onekids_project.response.subject.CreateSubjectResponse;
import com.example.onekids_project.response.subject.ListSubjectResponse;
import com.example.onekids_project.response.subject.SubjectSearchRequest;
import com.example.onekids_project.response.subject.UpdateSubjectResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    /**
     * Tìm kiếm tất cả các môn học
     *
     * @param idSchool
     * @param pageable
     * @return
     */
    ListSubjectResponse findAllSubject(Pageable pageable, Long idSchool);

    /**
     * Tìm kiếm môn học theo Id
     *
     * @param id
     * @return
     */
    Optional<SubjectDTO> findByIdSubject(Long id, Long idSchool);

    /**
     * Tạo môn học
     *
     * @param createSubjectRequest
     * @return
     */
    CreateSubjectResponse saveSubject(CreateSubjectRequest createSubjectRequest, Long idSchool);

    /**
     * Sửa môn học
     *
     * @param updateSubjectRequest
     * @return
     */
    UpdateSubjectResponse updateSubject(UpdateSubjectRequest updateSubjectRequest, Long idSchool);


    /**
     * Xóa môn học(set delActive chuyển về false)
     *
     * @param id
     */
    boolean deleteSubject(Long idSchool, Long id);

    /**
     * tìm kiếm các môn học của các lớp
     *
     * @param idSchool
     * @param classSearchCommonRequest
     * @return
     */
    List<SubjectForClassConfigResponse> findAllSubjectManegeConfig(Long idSchool, ClassSearchCommonRequest classSearchCommonRequest);

    /**
     * tìm kiếm môn học cho lớp
     *
     * @param idSchool
     * @param idClass
     * @return
     */
    List<SubjectForClassResponse> findSubjectForClass(Long idSchool, Long idClass);

    /**
     * cập nhật subject of class
     *
     * @param idClass
     * @param idObjectRequestList
     * @return
     */
    boolean updateSubjectForClass(Long idClass, List<IdObjectRequest> idObjectRequestList);

    /**
     * tìm kiếm môn học trong cấu hình
     *
     * @param idSchool
     * @param subjectSearchRequest
     * @return
     */
    List<SubjectConfigResponse> searchSubjectConfig(Long idSchool, SubjectSearchRequest subjectSearchRequest);

    /**
     * tao subject
     *
     * @param idSchool
     * @param subjectCreateRequest
     * @return
     */
    SubjectConfigResponse createSubject(Long idSchool, SubjectCreateRequest subjectCreateRequest);

    /**
     * tao subject
     *
     * @param subjectUpdateRequest
     * @return
     */
    SubjectConfigResponse updateSubject(SubjectUpdateRequest subjectUpdateRequest);

    /**
     * xóa một môn học
     *
     * @param id
     * @return
     */
    boolean deleteSubjectOne(Long id);

    /**
     * xóa nhiều môn học
     *
     * @param idObjectRequestList
     * @return
     */
    int deleteSubjectMany(List<IdObjectRequest> idObjectRequestList);
}
