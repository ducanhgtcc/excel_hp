package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsStatusTimeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KidsStatusTimelineRepository extends JpaRepository<KidsStatusTimeline, Long> {
    Optional<KidsStatusTimeline> findFirstByKidsIdOrderByStartDateDesc(Long idKid);

    List<KidsStatusTimeline> findByKidsIdOrderByStartDateDesc(Long idKid);

    Optional<KidsStatusTimeline> findFirstByKidsIdAndStartDateIsBeforeOrderByIdDesc(Long idKid, LocalDate date);
}
