package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.AttendanceEmployee;
import com.example.onekids_project.repository.repositorycustom.AttendanceEmployeeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * date 2021-03-09 3:42 CH
 *
 * @author ADMIN
 */
public interface AttendanceEmployeeRepository extends JpaRepository<AttendanceEmployee, Long>, AttendanceEmployeeRepositoryCustom {

    Optional<AttendanceEmployee> findByInfoEmployeeSchool_IdAndDate(Long id, LocalDate date);

    Optional<AttendanceEmployee> findByIdAndDelActiveTrue(Long id);

    List<AttendanceEmployee> findByInfoEmployeeSchoolIdAndDateGreaterThanEqualAndDateLessThanEqual(Long id, LocalDate startDate, LocalDate endDate);

    List<AttendanceEmployee> findByInfoEmployeeSchoolIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);

    List<AttendanceEmployee> findByInfoEmployeeSchool_School_IdAndDate(Long idSchool, LocalDate date);
}
