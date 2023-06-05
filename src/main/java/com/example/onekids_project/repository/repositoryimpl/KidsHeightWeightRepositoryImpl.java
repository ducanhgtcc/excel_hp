package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.EvaluateDate;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.repository.repositorycustom.KidsHeightWeightRepositoryCustom;
import com.example.onekids_project.request.kidsheightweight.SearchKidsHeightWeightRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KidsHeightWeightRepositoryImpl extends BaseRepositoryimpl<Kids> implements KidsHeightWeightRepositoryCustom {

    @Override
    public Optional<Kids> findByIdHeightWeight(Long idSchool, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Kids> kidsList = findAllNoPaging(queryStr.toString(), mapParams);
        if (kidsList.size() > 0) {
            return Optional.ofNullable(kidsList.get(0));
        }
        return Optional.empty();
    }


}
