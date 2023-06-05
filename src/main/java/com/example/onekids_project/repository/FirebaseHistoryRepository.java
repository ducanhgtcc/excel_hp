package com.example.onekids_project.repository;

import com.example.onekids_project.entity.user.FirebaseHistory;
import com.example.onekids_project.repository.repositorycustom.FirebaseHistoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FirebaseHistoryRepository extends JpaRepository<FirebaseHistory, Long>, FirebaseHistoryRepositoryCustom {
//    List<FirebaseHistory> findAllByDelActiveTrue();
}
