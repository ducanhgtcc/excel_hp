package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.employee.Subject;
import com.example.onekids_project.response.subject.SubjectSearchRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectRepositoryCustom {

    /**
     * Tìm kiếm Môn học
     * @param pageable
     * @return
     */
    List<Subject> findAllSubject(Long idSchool,Pageable pageable);

    /**
     * tìm kiếm môn học theo danh sách id
     * @param idSchool
     * @param ids
     * @return
     */
    List<Subject> findByIdsSubject(Long idSchool, List<Long> ids);

    /**
     * tìm kiếm môn học
     * @param idSchool
     * @param subjectSearchRequest
     * @return
     */
    List<Subject> searchSubject(Long idSchool, SubjectSearchRequest subjectSearchRequest);
}
