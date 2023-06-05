package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.KidsExtraInfo;
import com.example.onekids_project.repository.repositorycustom.KidsExtraInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KidsExtraInfoRepository extends JpaRepository<KidsExtraInfo, Long>, KidsExtraInfoRepositoryCustom {
}
