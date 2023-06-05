package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.Vaccinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinateRespository extends JpaRepository<Vaccinate, Long> {
}
