package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.AbsentLetter;
import com.example.onekids_project.repository.repositorycustom.AbsentLetterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AbsentLetterRepository extends JpaRepository<AbsentLetter, Long>, AbsentLetterRepositoryCustom {
    /**
     * tìm kiếm các đơn xin nghỉ của một học sinh
     *
     * @param idKid
     * @param idSchool
     * @param delActive
     * @return
     */
    List<AbsentLetter> findByKidsIdAndIdSchoolAndDelActive(Long idKid, Long idSchool, boolean delActive);

    List<AbsentLetter> findByKidsIdAndIdSchoolAndParentAbsentDelFalseAndDelActiveTrue(Long idKid, Long idSchool);

    Optional<AbsentLetter> findByIdAndDelActiveTrue(Long id);

    Optional<AbsentLetter> findByIdAndConfirmStatusFalseAndDelActiveTrue(Long id);

    int countByIdClassAndParentAbsentDelFalseAndConfirmStatusFalseAndDelActiveTrue(Long idClass);
    int countByIdClassAndParentAbsentDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(Long idClass);

    int countByIdSchoolAndIdClassInAndParentAbsentDelFalseAndConfirmStatusFalseAndDelActiveTrue(Long idSchool, List<Long> idClassList);

    int countByIdSchoolAndParentAbsentDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(Long idSchool);

    /**
     * số đơn xin nghỉ chưa duyệt của một trường chưa duyệt
     * @param idSchool
     * @return
     */
    int countByIdSchoolAndConfirmStatusFalseAndParentAbsentDelFalseAndDelActiveTrue(Long idSchool);

    List<AbsentLetter> findAllByIdSchoolAndConfirmStatusFalseAndDelActiveTrue(Long idSchool);
}
