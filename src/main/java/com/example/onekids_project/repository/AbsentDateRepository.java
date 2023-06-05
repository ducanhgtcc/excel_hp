package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.repository.repositorycustom.AbsentDateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface AbsentDateRepository extends JpaRepository<AbsentDate, Long>, AbsentDateRepositoryCustom {


}
