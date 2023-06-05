package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsVaccinate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KidsVaccinateRepository extends JpaRepository<KidsVaccinate, Long> {
    List<KidsVaccinate> findByKidIdAndDoneTrue(Long idKid);

    Optional<KidsVaccinate> findByVaccinateIdAndKidId(Long idVaccinate, Long idKid);

}
