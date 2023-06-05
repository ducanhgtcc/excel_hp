package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.repository.repositorycustom.KidsWeightRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface KidsWeightRepository extends JpaRepository<KidsWeight, Long>, KidsWeightRepositoryCustom {

    Optional<KidsWeight> findByIdAndDelActive(Long id, boolean delActive);

    @Modifying
    @Query(value = "delete from kids_weight where id=:id", nativeQuery = true)
    void deletekidsWById(Long id);

    Optional<KidsWeight> findByKidsIdAndAppTypeAndTimeWeight(Long idKid, String appType, LocalDate date);

    Optional<KidsWeight> findByIdAndAppType(Long id, String appType);

    @Query(value = "select * from kids_weight where app_type = 'teacher' or app_type = 'plus' and id=:id", nativeQuery = true)
    long findIdKidWeightPlus(Long id);

    List<KidsWeight> findByKidsIdAndDelActiveTrue(Long idKids);


}
