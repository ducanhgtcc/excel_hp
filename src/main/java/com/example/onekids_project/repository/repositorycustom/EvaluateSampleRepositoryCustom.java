package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.sample.EvaluateSample;

import java.util.List;

public interface EvaluateSampleRepositoryCustom {
    /**
     * tìm kiếm mẫu nhận xét mặc định
     *
     * @param idSchool
     * @param idSystem
     * @return
     */
    List<EvaluateSample> findAllEvaluateSample(Long idSchool, Long idSystem);
}
