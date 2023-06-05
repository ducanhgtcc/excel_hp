package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.Medicine;
import com.example.onekids_project.repository.repositorycustom.MedicineRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long>, MedicineRepositoryCustom {
    Optional<Medicine> findByIdAndDelActive(Long id, boolean delActive);

    List<Medicine> findByKidsIdAndIdSchoolAndDelActive(Long idKid, Long idSchool, boolean delActive);

    List<Medicine> findByKidsIdAndIdSchoolAndParentMedicineDelFalseAndDelActiveTrue(Long idKid, Long idSchool);

    Optional<Medicine> findByIdAndDelActiveTrue(Long id);

    Optional<Medicine> findByIdAndConfirmStatusFalseAndDelActiveTrue(Long id);

    int countByIdClassAndParentMedicineDelFalseAndConfirmStatusFalseAndDelActiveTrue(Long idClass);
    int countByIdClassAndParentMedicineDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(Long idClass);

    int countByIdSchoolAndParentMedicineDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(Long idSchool);

    List<Medicine> findAllByIdSchoolAndConfirmStatusFalseAndDelActiveTrue(Long idSchool);

}
