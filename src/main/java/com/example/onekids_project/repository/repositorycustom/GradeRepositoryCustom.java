package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.grade.SearchGradeRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GradeRepositoryCustom {
    List<Grade> findGradeInSchool(Long idSchool);

    /**
     * tìm kiếm tất cả các khối cho các tìm kiếm chung
     * @param idSchool
     * @return
     */
    List<Grade> findAllGradeCommon(Long idSchool);
    /**
     * tìm kiếm tất cả khối học
     *
     * @param idSchool
     * @param request
     * @return
     */
    List<Grade> findAllGrade(Long idSchool, PageNumberWebRequest request);

    long countGrade(Long idSchool);

    /**
     * tìm kiếm khối học theo id
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<Grade> findByIdGrade(Long idSchool, Long id);

    /**
     * tìm kiếm khối học theo tên
     *
     * @param idSchool
     * @param pageable
     * @param searchGradeRequest
     * @return
     */
    List<Grade> searchGrade(Long idSchool, Pageable pageable, SearchGradeRequest searchGradeRequest);
}
