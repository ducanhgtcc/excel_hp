package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.repository.repositorycustom.MediaRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaRepositoryImpl extends BaseRepositoryimpl<Media> implements MediaRepositoryCustom {
    @Override
    public List<Media> findVideoSchool(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append(" and media_type=:mediaType ");
        mapParams.put("mediaType", AppConstant.VIDEO);
        queryStr.append(" and scope_type=:scopeType ");
        mapParams.put("scopeType", AppConstant.ALBUMSCHOOL);
        queryStr.append(" and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
