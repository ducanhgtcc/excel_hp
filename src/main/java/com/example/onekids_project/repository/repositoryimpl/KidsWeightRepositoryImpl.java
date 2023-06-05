package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.repository.repositorycustom.KidsWeightRepositoryCustom;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KidsWeightRepositoryImpl extends BaseRepositoryimpl<KidsWeight> implements KidsWeightRepositoryCustom {

    @Override
    public List<KidsWeight> findKidsWeight(Long idKid, LocalDate localDate, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDate != null) {
            queryStr.append("and time_weight<:localDate ");
            mapParams.put("localDate", localDate);
        }
        queryStr.append("order by time_weight desc");
        List<KidsWeight> kidsWeightList = findAllMobile(queryStr.toString(), mapParams, pageable);
        return kidsWeightList;
    }

    @Override
    public List<KidsWeight> findKidsWeightfClass(Long idKid) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        queryStr.append("order by time_weight desc");
        List<KidsWeight> kidsWeightList = findAllNoPaging(queryStr.toString(), mapParams);
        return kidsWeightList;
    }
}
