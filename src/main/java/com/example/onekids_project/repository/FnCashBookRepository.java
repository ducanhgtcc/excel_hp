package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.repository.repositorycustom.FnCashBookRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-03-01 10:26
 *
 * @author lavanviet
 */
public interface FnCashBookRepository extends JpaRepository<FnCashBook, Long>, FnCashBookRepositoryCustom {
    Optional<FnCashBook> findBySchoolIdAndYear(Long idSchool, int year);

    Optional<FnCashBook> findByIdAndSchoolId(Long id, Long idSchool);

    Optional<FnCashBook> findBySchoolIdAndYearAndDelActiveTrue(Long idSchool, int year);

    List<FnCashBook> findBySchoolIdOrderByYear(Long idSchool);

    Optional<FnCashBook> findFirstBySchoolIdOrderByYearDesc(Long idSchool);

}
