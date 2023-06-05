package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.repository.repositorycustom.ExEmployeeClassRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExEmployeeClassRepository extends JpaRepository<ExEmployeeClass, Long>, ExEmployeeClassRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_employee_subject(id_exemployee_class, id_subject) value(:idExEmployeeClass, :idSubject)", nativeQuery = true)
    void insertExEmployeeClassSubject(Long idExEmployeeClass, Long idSubject);

    @Modifying
    @Query(value = "delete from ex_employee_subject where id_exemployee_class=:idExEmployeeClass", nativeQuery = true)
    void deleteExEmployeeClassSubject(Long idExEmployeeClass);

    //List<ExEmployeeClass> findByEmployeeIdAndEmployeeEmployeeStatusNotAndDelActive(Long idEmployeeboolean,String statusCode,boolean delActive);
    void deleteByInfoEmployeeSchoolId(Long id);

    /**
     * tìm kiếm các lớp theo idClass
     *
     * @param idClass
     * @return
     */
    List<ExEmployeeClass> findByMaClassIdAndDelActiveTrue(Long idClass);

    List<ExEmployeeClass> findByInfoEmployeeSchool_IdAndDelActiveTrue(Long idEmployee);

    List<ExEmployeeClass> findByInfoEmployeeSchoolId(Long idInfoEmployee);

}
