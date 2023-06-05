package com.example.onekids_project.repository;

import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.repository.repositorycustom.FnOrderKidsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * date 2021-02-23 15:19
 *
 * @author lavanviet
 */
public interface FnOrderKidsRepository extends JpaRepository<FnOrderKids, Long>, FnOrderKidsRepositoryCustom {
    Optional<FnOrderKids> findFirstByOrderByIdDesc();

    Optional<FnOrderKids> findByKidsIdAndMonthAndYear(Long idKid, int month, int year);

    List<FnOrderKids> findByKidsIdAndYearOrderByMonthDesc(Long idKid, int year);

    int countByKidsIdAndMonthIn(Long idKid, List<Integer> monthList);

    List<FnOrderKids> findByKidsIdAndMonthIn(Long idKid, List<Integer> monthList);

    List<FnOrderKids> findByKidsIdAndYearAndMonthGreaterThanEqualAndMonthLessThanEqual(Long idKid, int year, int startMonth, int endMonth);

    List<FnOrderKids> findByKidsId(Long idKid);

    List<FnOrderKids> findByYearAndMonthAndKidsIdSchoolAndDelActiveTrue(int year, int month, Long idSchool);

    List<FnOrderKids> findByKidsIdAndYearAndMonthAndKidsIdSchoolAndDelActiveTrue(Long idKid, int year, int month, Long idSchool);

    List<FnOrderKids> findByYearAndMonthInAndKidsIdSchoolAndDelActiveTrue(int year, List<Integer> month, Long idSchool);
}
