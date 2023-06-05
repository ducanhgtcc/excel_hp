package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.kids.KidsExtraInfo;
import com.example.onekids_project.repository.repositorycustom.KidsExtraInfoRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KidsExtraInfoRepositoryImpl extends BaseRepositoryimpl<KidsExtraInfo> implements KidsExtraInfoRepositoryCustom {
    @Override
    public Optional<KidsExtraInfo> findByIdKidsExtraInfo(Long idSchool, Long id) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (id != null) {
            queryStr.append("and id_kid=:id");
            mapParams.put("id", id);
        }
        List<KidsExtraInfo> kidsExtraInfoList = findAllNoPaging(queryStr.toString(), mapParams);
        if (kidsExtraInfoList.size() > 0) {
            return Optional.ofNullable(kidsExtraInfoList.get(0));
        }
        return Optional.empty();
    }
}
