package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.repository.repositorycustom.EmployeeRepositoryCustom;
import com.example.onekids_project.request.employee.SearchEmployeeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {

    /*@Query(value = "select id from ma_employee where id_school=?1 and del_active=?2", nativeQuery = true)
    List<Long> findAllId(Long id_school, int del_active);

    Optional<Employee> findByIdAndIdSchoolLoginAndDelActive(Long id,Long idSchool,boolean delActive);

    @Modifying
    @Query(value = "insert into ex_school_employee value(?1,?2)", nativeQuery = true)
    void inSertSchoolEmployee(Long idSchool, Long idEmployee);

    List<Employee> findByDepartmentEmployeeListDepartmentIdAndDelActive(Long idDepartment,boolean delActive);
    //boolean existsByIdAndDepartmentEmployeeListId(Long id,Long idEmployee);*/

    Optional<Employee> findByMaUser_IdAndDelActiveTrue(Long idEmployee);

    List<Employee> findAllByDelActiveTrueAndBirthday(LocalDate localDate);

    List<Employee> findAllByIdSchoolLoginAndDelActiveTrue(Long idSchool);

}
