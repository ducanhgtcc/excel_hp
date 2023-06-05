package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentRepositoryCustom {

    List<Department> findDepartmentCommon(Long idSchool);
    /**
     * Tìm kiếm Phòng ban
     *
     * @return
     */
    List<Department> findAllDepartment(Long idSchool, PageNumberWebRequest request);

    List<Department> findDepartment(Long idSchool);
}
