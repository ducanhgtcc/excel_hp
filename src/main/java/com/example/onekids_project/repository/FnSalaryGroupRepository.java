package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.employeesalary.FnSalaryGroup;
import com.example.onekids_project.repository.repositorycustom.FnSalaryGroupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-06-02 14:41
 *
 * @author lavanviet
 */
public interface FnSalaryGroupRepository extends JpaRepository<FnSalaryGroup, Long>, FnSalaryGroupRepositoryCustom {
    List<FnSalaryGroup> findByIdSchoolAndDelActiveTrueOrderByIdDesc(Long idSchool);

    List<FnSalaryGroup> findByIdSchoolAndNameContainingAndDelActiveTrueOrderByIdDesc(Long idSchool, String name);

    Optional<FnSalaryGroup> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    List<FnSalaryGroup> findByIdInAndIdSchoolAndDelActiveTrue(List<Long> idList, Long idSchool);

}
