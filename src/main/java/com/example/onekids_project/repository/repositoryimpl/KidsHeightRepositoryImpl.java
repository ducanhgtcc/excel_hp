package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.repository.repositorycustom.KidsHeightRepositoryCustom;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KidsHeightRepositoryImpl extends BaseRepositoryimpl<KidsHeight> implements KidsHeightRepositoryCustom {

    @Override
    public List<KidsHeight> findKidsHeight(Long idKid, LocalDate localDate,  Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        if (localDate != null) {
            queryStr.append("and time_height<:localDate ");
            mapParams.put("localDate", localDate);
        }
        queryStr.append("order by time_height desc");
        List<KidsHeight> kidsHeightList = findAllMobile(queryStr.toString(), mapParams, pageable);
        return kidsHeightList;
    }

    @Override
    public List<KidsHeight> findKidsHeightOfClass(Long idKid) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idKid != null) {
            queryStr.append("and id_kids=:idKid ");
            mapParams.put("idKid", idKid);
        }
        queryStr.append("order by time_height desc");
        List<KidsHeight> kidsHeightList = findAllNoPaging(queryStr.toString(), mapParams);
        return kidsHeightList;
    }
}
