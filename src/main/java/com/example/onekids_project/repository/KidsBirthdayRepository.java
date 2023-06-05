package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.repository.repositorycustom.KidsBirthdayRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KidsBirthdayRepository extends JpaRepository<Kids, Long>, KidsBirthdayRepositoryCustom {

}
