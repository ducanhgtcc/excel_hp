package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.sample.EvaluateSample;
import com.example.onekids_project.repository.repositorycustom.EvaluateSampleRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluateSampleRepositoryImpl extends BaseRepositoryimpl<EvaluateSample> implements EvaluateSampleRepositoryCustom {
    @Override
    public List<EvaluateSample> findAllEvaluateSample(Long idSchool, Long idSystem) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idSystem != null) {
            queryStr.append("or id_school=:idSystem ");
            mapParams.put("idSystem", idSystem);
        }
        queryStr.append("order by id_school desc");
        List<EvaluateSample> evaluateSampleList = findAllNoPaging(queryStr.toString(), mapParams);
        return evaluateSampleList;
    }
}
