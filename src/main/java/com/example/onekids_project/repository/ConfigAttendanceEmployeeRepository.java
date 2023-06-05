package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.ConfigAttendanceEmployee;
import com.example.onekids_project.repository.repositorycustom.ConfigAttendanceEmployeeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * date 2021-03-08 10:03 SA
 *
 * @author ADMIN
 */
public interface ConfigAttendanceEmployeeRepository extends JpaRepository<ConfigAttendanceEmployee, Long>, ConfigAttendanceEmployeeRepositoryCustom {


    Optional<ConfigAttendanceEmployee> findFirstByInfoEmployeeSchoolIdOrderByCreatedDateDesc(Long id);

    //    GreaterThanEqual
    Optional<ConfigAttendanceEmployee> findFirstByInfoEmployeeSchoolIdAndDateLessThanEqualOrderByCreatedDateDesc(Long id, LocalDate date);

//    Optional<ConfigAttendanceEmployee> findFirstByInfoEmployeeSchoolIdAndDateGreaterThanEqualOrderByCreatedDateDesc(Long id, LocalDate date);

    Optional<ConfigAttendanceEmployee> findByIdAndDelActiveTrue(Long id);

    Optional<ConfigAttendanceEmployee> findByInfoEmployeeSchoolIdAndDate(Long id, LocalDate date);
}
