package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.repositorycustom.SchoolBrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolBrandRepository extends JpaRepository<School, Long>, SchoolBrandRepositoryCustom {

}
