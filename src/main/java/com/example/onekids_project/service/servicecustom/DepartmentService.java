package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.DepartmentDTO;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.department.CreateDepartmentRequest;
import com.example.onekids_project.request.department.UpdateDepartmentRequest;
import com.example.onekids_project.request.employee.TransferEmployeeDepartmentRequest;
import com.example.onekids_project.response.department.CreateDepartmentResponse;
import com.example.onekids_project.response.department.DepartmentOtherResponse;
import com.example.onekids_project.response.department.DepartmentResponse;
import com.example.onekids_project.response.department.UpdateDepartmentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    /**
     * tìm kiếm phòng ban chung cho các api
     *
     * @param principal
     * @return
     */
    List<DepartmentOtherResponse> findDepartmentCommon(UserPrincipal principal);

    /**
     * Tìm kiếm phòng ban*
     *
     * @return
     */
    List<DepartmentResponse> findAllDepartment(UserPrincipal principal, PageNumberWebRequest request);

    /**
     * Tìm kiếm Phòng ban theo Id
     *
     * @param id
     * @return
     */
    Optional<DepartmentDTO> findByIdDepartment(Long idSchool, Long id);

    /**
     * Tìm kiếm Phòng ban theo Id
     *
     * @param id
     * @return
     */
    Optional<Department> findOne(Long id);

    /**
     * Tạo phòng ban
     *
     * @param createDepartmentRequest
     * @return
     */
    CreateDepartmentResponse saveDeparment(Long idSchool, CreateDepartmentRequest createDepartmentRequest);

    /**
     * Sửa nhân viên
     *
     * @param updateDepartmentRequest
     * @return
     */
    boolean updateDeparment(Long idSchool, UpdateDepartmentRequest updateDepartmentRequest);


    /**
     * Xóa nhân viên(set delActive chuyển về false)
     *
     * @param id
     */
    boolean deleteDepartment(Long idSchool, Long id);

    /**
     * Thêm nhân viên vào phòng
     *
     * @param idSchool
     * @param transferEmployeeDepartmentRequest
     * @return
     */
    boolean insertTransferEmployeeInDepartment(Long idSchool, TransferEmployeeDepartmentRequest transferEmployeeDepartmentRequest);
}
