package com.example.onekids_project.repository;

import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.repositorycustom.BrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {

    Optional<Brand> findByIdAndDelActive(Long id, boolean b);

    Optional<Brand> findByIdAndDelActiveTrue(Long id);

    List<Brand> findByAgentList_Id(Long idAgent);

}
