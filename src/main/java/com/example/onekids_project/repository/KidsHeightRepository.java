package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.repository.repositorycustom.KidsHeightRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface KidsHeightRepository extends JpaRepository<KidsHeight, Long>, KidsHeightRepositoryCustom {

    Optional<KidsHeight> findByIdAndDelActive(Long id, boolean delActive);

    @Modifying
    @Query(value = "delete from kids_height where id=:id", nativeQuery = true)
    void deletekidsHById(Long id);

    Optional<KidsHeight> findByKidsIdAndAppTypeAndTimeHeight(Long idKid, String appType, LocalDate date);

    Optional<KidsHeight> findByIdAndAppType(Long id, String appType);

    @Query(value = "select * from kids_height where app_type = 'teacher' or app_type = 'plus' and id=:id", nativeQuery = true)
    long findIdKidHeightPlus(Long id);

    List<KidsHeight> findByKidsIdAndDelActiveTrue(Long idKids);
}
