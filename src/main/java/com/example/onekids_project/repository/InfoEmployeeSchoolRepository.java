package com.example.onekids_project.repository;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.repository.repositorycustom.InfoEmployeeSchoolRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InfoEmployeeSchoolRepository extends JpaRepository<InfoEmployeeSchool, Long>, InfoEmployeeSchoolRepositoryCustom {
    @Modifying
    @Query(value = "insert into ex_employee_account_type(id_info_employee, id_account_type) value(:idInfoEmployee, :idAccountType)", nativeQuery = true)
    void insertEmployeeAccountType(Long idInfoEmployee, Long idAccountType);

    @Modifying
    @Query(value = "delete from ex_employee_account_type where id_info_employee=:idInfoEmployee", nativeQuery = true)
    void deleteEmployeeAccountType(Long idInfoEmployee);

    Optional<InfoEmployeeSchool> findByIdAndSchoolIdAndDelActiveTrue(Long id, Long idSchool);

    Optional<InfoEmployeeSchool> findByIdAndSchoolIdAndDelActiveFalse(Long id, Long idSchool);

    Optional<InfoEmployeeSchool> findByIdAndSchoolId(Long id, Long idSchool);

    Optional<InfoEmployeeSchool> findByIdAndDelActive(Long id, boolean delActive);

    /**
     * tìm kiếm nhân viên theo id và chưa bị xóa
     *
     * @param id
     * @return
     */
    Optional<InfoEmployeeSchool> findByIdAndDelActiveTrue(Long id);

    List<InfoEmployeeSchool> findBySchoolIdAndDelActiveTrue(Long idSchool);

    List<InfoEmployeeSchool> findBySchoolId(Long idSchool);

    List<InfoEmployeeSchool> findByExEmployeeClassList_MaClass_IdAndDelActiveTrue(Long idClass);

    List<InfoEmployeeSchool> findByExEmployeeClassList_MaClass_IdAndAppTypeAndDelActiveTrue(Long idClass, String type);

    List<InfoEmployeeSchool> findBySchool_IdAndDelActiveTrue(Long idSchool);

    List<InfoEmployeeSchool> findBySchoolIdAndAppTypeAndDelActiveTrue(Long idSchool, String type);

    List<InfoEmployeeSchool> findAllByDelActiveTrueAndBirthdayAndEmployeeStatus(LocalDate localDate, String status);

    List<InfoEmployeeSchool> findBySchoolIdAndPhoneAndAppTypeAndDelActiveTrue(Long idSchool, String phone, String appType);

    List<InfoEmployeeSchool> findBySchoolIdAndEmployeeStatusAndDelActiveTrue(Long idSchool, String status);

    List<InfoEmployeeSchool> findBySchoolIdAndEmployeeStatusAndAppTypeAndDelActiveTrue(Long idSchool, String appType, String status);

    List<InfoEmployeeSchool> findBySchoolIdAndEmployeeStatusAndAppTypeAndActivatedTrueAndDelActiveTrue(Long idSchool, String appType, String status);

    List<InfoEmployeeSchool> findBySchoolIdAndEmployeeStatusAndAppTypeAndEmployeeNotNullAndActivatedTrueAndDelActiveTrue(Long idSchool, String appType, String status);

    List<InfoEmployeeSchool> findBySchoolIdAndEmployeeStatusAndAppTypeAndEmployeeIsNotNullAndActivatedTrueAndDelActiveTrue(Long idSchool, String appType, String status);

    InfoEmployeeSchool findByDelActiveTrueAndSchool_IdAndEmployee_MaUser_Id(Long idSchool, Long idUser);

    List<InfoEmployeeSchool> findBySchoolIdAndAppTypeAndEmployeeStatusAndDelActiveTrue(Long idSchool, String appType, String employeeStatus);

    List<InfoEmployeeSchool> findByIdInAndSchoolIdAndDelActiveTrue(List<Long> idList, Long idSchool);

    List<InfoEmployeeSchool> findByAppTypeAndExEmployeeClassListMaClassIdAndDelActiveTrue(String appType, Long idClass);

    List<InfoEmployeeSchool> findByAppTypeAndSchoolIdAndDelActiveTrue(String appType, Long idSchool);

    List<InfoEmployeeSchool> findByAppTypeAndSchoolId(String appType, Long idSchool);

    List<InfoEmployeeSchool> findByIdInAndDelActiveTrue(List<Long> idList);

    List<InfoEmployeeSchool> findByIdInAndEmployeeIsNotNull(List<Long> idList);

    long countByEmployeeStatusAndSchoolIdAndAppTypeAndEmployeeIsNotNullAndDelActiveTrue(String status, Long idSchool, String type);

    List<InfoEmployeeSchool> findByEmployeeStatusAndAppTypeAndEmployeeIsNotNullAndDelActiveTrue(String status, String type);

}
