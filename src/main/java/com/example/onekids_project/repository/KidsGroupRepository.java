package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsGroup;
import com.example.onekids_project.repository.repositorycustom.KidsGroupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KidsGroupRepository extends JpaRepository<KidsGroup, Long>, KidsGroupRepositoryCustom {

    @Modifying
    @Query(value = "insert into ex_kids_group value(:idGroup, :idKid)", nativeQuery = true)
    void insertTransferKidsGroup(Long idGroup, Long idKid);

    @Modifying
    @Query(value = "delete from ex_kids_group where id_kids_group=:idGroup", nativeQuery = true)
    void deleteTransferKidsGroup(Long idGroup);

    List<KidsGroup> findByDelActiveTrueAndIdSchool(Long idSchool);
}
