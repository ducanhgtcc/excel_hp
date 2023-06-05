package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.MaKidPics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MaKidPicsRepository extends JpaRepository<MaKidPics, Long> {
    @Modifying
    @Query(value = "delete from ma_kid_pics where id=:id", nativeQuery = true)
    void deleteByIdCustom(Long id);

    void deleteByKidId(Long idKid);
}