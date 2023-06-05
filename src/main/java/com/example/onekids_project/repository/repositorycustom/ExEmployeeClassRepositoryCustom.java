package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.school.Grade;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExEmployeeClassRepositoryCustom {

    /**
     * Tìm kiếm tất cả các nhân viên trong lớp
     * @param idSchool
     * @param pageable
     * @return
     */
    List<Grade> findAllEmployeeClass(Long idSchool, Pageable pageable);

    /**
     * tìm kiếm theo id
     * @param idClass
     * @return
     */
    List<ExEmployeeClass> findByIdExEmployeeClass(Long idClass);

    ExEmployeeClass findByIdClassAndIdEmployee(Long idClass, Long idInfoEmployeeSchool);
}
