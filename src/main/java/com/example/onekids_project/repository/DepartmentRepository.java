package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.repository.repositorycustom.DepartmentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentRepositoryCustom {

    Optional<Department> findByIdAndSchoolIdAndDelActive(Long id, Long idSchool, boolean delActive);

    List<Department> findBySchoolIdAndDelActiveTrue(Long idSchool);

    List<Department> findByIdInAndSchoolIdAndDelActiveTrue(List<Long> idList, Long idSchool);

    List<Department> findByDepartmentEmployeeList_InfoEmployeeSchool_IdAndDepartmentEmployeeList_DelActiveTrueAndDelActiveTrue(Long idEmployee);

    Optional<Department> findByDepartmentNameAndDelActiveTrueAndSchool_Id(String nameDepartment, Long id);
}
