package com.example.onekids_project.repository;

import com.example.onekids_project.entity.sample.HeightSample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeightSampleRepository extends JpaRepository<HeightSample, Long> {
    List<HeightSample> findByType(String type);

    List<HeightSample> findAllByType(String type);
}
