package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExDepartmentEmployeeRepository extends JpaRepository<ExDepartmentEmployee, Long> {

    void deleteByInfoEmployeeSchoolId(Long idInfoEmployee);

    void deleteByDepartmentId(Long idDepartment);

    /**
     * Tìm kiếm những nhân viên trong phòng theo id có trạng thái là không nghỉ làm và delActive=true
     *
     * @param idInfoEmployee
     * @param statusInfoEmployee
     * @return
     */
    List<ExDepartmentEmployee> findByInfoEmployeeSchoolIdAndInfoEmployeeSchoolEmployeeStatusNotAndDelActiveTrue(Long idInfoEmployee, String statusInfoEmployee);

    /**
     * Tìm kiếm những phòng có trạng thái nhân viên không nghỉ làm và delActive=true
     *
     * @param idDepartment
     * @param employeeStatus
     * @return
     */
    List<ExDepartmentEmployee> findByDepartmentIdAndDepartmentDelActiveTrueAndInfoEmployeeSchoolEmployeeStatusNotAndInfoEmployeeSchoolDelActiveTrueAndDelActiveTrue(Long idDepartment, String employeeStatus);

    List<ExDepartmentEmployee> findByInfoEmployeeSchool_IdAndDelActiveTrue(Long idEmployee);

    List<ExDepartmentEmployee> findAllByDepartmentIdAndInfoEmployeeSchoolEmployeeStatusAndDelActiveTrue(Long id, String status);


}
