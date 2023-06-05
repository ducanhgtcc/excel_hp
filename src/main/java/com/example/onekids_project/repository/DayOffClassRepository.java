package com.example.onekids_project.repository;

import com.example.onekids_project.entity.classes.DayOffClass;
import com.example.onekids_project.repository.repositorycustom.DayOffClassRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * date 2021-05-05 13:59
 *
 * @author lavanviet
 */
public interface DayOffClassRepository extends JpaRepository<DayOffClass, Long>, DayOffClassRepositoryCustom {
    Optional<DayOffClass> findByMaClassIdAndDateAndDelActiveTrue(Long idClass, LocalDate date);

    Optional<DayOffClass> findByIdAndDelActiveTrue(Long id);

    List<DayOffClass> findByMaClassIdAndDelActiveTrueOrderByDateDesc(Long idClass);
    boolean existsByDateAndMaClassIdAndDelActiveTrue(LocalDate date, Long idClass);
    void deleteByIdIn(List<Long> idList);

}
