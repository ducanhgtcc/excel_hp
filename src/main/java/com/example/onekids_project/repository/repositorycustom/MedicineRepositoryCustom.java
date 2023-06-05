package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.mobile.plus.request.medicine.SearchMedicinePlusRequest;
import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.medicine.SearchMedicineTeacherRequest;
import com.example.onekids_project.request.parentdiary.SearchMedicineRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicineRepositoryCustom {

    List<Medicine> findAllMedicine(Long idSchoolLogin, Pageable pageable);

    Optional<Medicine> findByIdMedicine(Long idSchool, Long id);

    List<Medicine> searchMedi(Long idSchool, SearchMedicineRequest request);

    Long getCountMessage(Long idSchool, Long idKid, LocalDateTime localDateTime);

    List<Medicine> findMedicineMoblie1(Long idSchool, Long idKid, Pageable pageable, LocalDateTime localDateTime);

    List<Medicine> searchMedicineForTeacher(Long idSchool, Long idClass, SearchMedicineTeacherRequest searchMedicineTeacherRequest);

    long countTotalAccount(Long idSchool,SearchMedicineRequest request);

    List<Medicine> findMedicineDate(Long idSchool,List<Long> idClassList, LocalDate date);

    List<Medicine> searchMedicineForPlus(Long idSchool, SearchMedicinePlusRequest request);

    List<Medicine> searchMedicineDate(Long idKid, Long idSchool, LocalDate date);
}


