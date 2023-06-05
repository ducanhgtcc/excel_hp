package com.example.onekids_project.repository;

import com.example.onekids_project.entity.sample.WeightSample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightSampleRepository extends JpaRepository<WeightSample, Long> {
    List<WeightSample> findByType(String type);

    List<WeightSample> findAllByType(String type);
}
