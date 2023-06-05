package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.GroupOutEmployee;
import com.example.onekids_project.repository.repositorycustom.GroupOutEmployeeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-07-14 2:03 PM
 *
 * @author nguyễn văn thụ
 */
public interface GroupOutEmployeeRepository extends JpaRepository<GroupOutEmployee, Long>, GroupOutEmployeeRepositoryCustom {

    List<GroupOutEmployee> findAllByIdSchoolAndDelActiveTrue(Long idSchool);
    Optional<GroupOutEmployee> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);
}
