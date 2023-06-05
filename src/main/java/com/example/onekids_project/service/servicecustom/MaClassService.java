package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.request.classes.ClassSearchNewRequest;
import com.example.onekids_project.request.classes.CreateMaClassRequest;
import com.example.onekids_project.request.classes.SearchMaClassRequest;
import com.example.onekids_project.request.classes.UpdateMaClassRequest;
import com.example.onekids_project.response.classes.*;
import com.example.onekids_project.response.teacher.ClassTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MaClassService {
    /**
     * tìm kiếm danh sách lớp trong một khối
     *
     * @param idGrade
     * @return
     */
    List<MaClassOtherResponse> findClassInGrade(UserPrincipal principal, Long idGrade);

    /**
     * tìm kiếm tất cả các lớp cho việc tìm kiếm ở các màn hình
     *
     * @return
     */
    List<MaClassOtherResponse> findAllMaClassOther(Long idSchool);

    List<GradeClassNameResponse> findAllMaClassOtherNew(Long idSchool);

    List<ClassTeacherResponse> findClassTeacher(UserPrincipal principal);

    ListClassNewResponse searchClassNew(UserPrincipal principal, ClassSearchNewRequest request);

    MaClassNewResponse findClassNewById(UserPrincipal principal, Long id);

    List<TeacherInClassResponse> findTeacherInClass(UserPrincipal principal, Long id);

    List<EmployeeInClassResponse> findEmployeeInClass(UserPrincipal principal, Long id);

    /**
     * tìm kiếm lớp học theo id
     *
     * @param idSchool
     * @param id
     * @return
     */
    Optional<MaClassDTO> findByIdMaClass(Long idSchool, Long id);

    /**
     * tìm kiếm lớp học theo tùy chọn
     *
     * @param idSchool
     * @param pageable
     * @param searchMaClassRequest
     * @return
     */
    ListMaClassResponse searchMaClass(Long idSchool, Pageable pageable, SearchMaClassRequest searchMaClassRequest);

    /**
     * tạo mới lớp học
     *
     * @param principal
     * @param createMaClassRequest
     * @return
     */
    MaClassResponse createMaClass(UserPrincipal principal, CreateMaClassRequest createMaClassRequest);

    /**
     * cập nhật lớp học
     *
     * @param principal
     * @param updateMaClassRequest
     * @return
     */
    boolean updateMaClass(UserPrincipal principal, UpdateMaClassRequest updateMaClassRequest);

    /**
     * xóa lớp theo id
     *
     * @param principal
     * @param id
     * @return
     */
    boolean deleteMaClass(UserPrincipal principal, Long id);


}
