package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.grade.CreateGradeRequest;
import com.example.onekids_project.request.grade.SearchGradeRequest;
import com.example.onekids_project.request.grade.UpdateGradeRequest;
import com.example.onekids_project.response.grade.GradeOtherResponse;
import com.example.onekids_project.response.grade.GradeResponse;
import com.example.onekids_project.response.grade.GradeUniqueResponse;
import com.example.onekids_project.response.grade.ListGradeResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface GradeService {
    List<GradeUniqueResponse> findGradeInSchool(UserPrincipal principal);

    /**
     * tìm kiếm danh sách khối cho các màn tìm kiếm chung
     *
     * @return
     */
//    List<GradeOtherResponse> findAllGradeForCommon(Long idSchool);

    /**
     * tìm kiếm tất cả các khối học
     *
     * @param idSchool
     * @param request
     * @return
     */
    ListGradeResponse findAllGrade(Long idSchool, PageNumberWebRequest request);

    /**
     * tìm kiếm khối học theo id
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<GradeDTO> findByIdGrade(Long idSchool, Long id);

    /**
     * tìm kiếm khối học theo tên
     *
     * @param idSchool
     * @param pageable
     * @param searchGradeRequest
     * @return
     */
    ListGradeResponse searchGrade(Long idSchool, Pageable pageable, SearchGradeRequest searchGradeRequest);

    /**
     * thêm mới khối học
     *
     * @param idSchool
     * @param createGradeRequest
     * @return
     */
    GradeResponse createGrade(UserPrincipal principal, Long idSchool, CreateGradeRequest createGradeRequest);

    /**
     * sửa khối học
     *
     * @param idSchool
     * @param updateGradeRequest
     * @return
     */
    GradeResponse updateGrade(UserPrincipal principal, Long idSchool, UpdateGradeRequest updateGradeRequest);

    /**
     * xóa khối học theo id
     *
     * @param idSchool
     * @return
     */
    boolean deleteGrade(UserPrincipal principal, Long idSchool, Long id);

}
