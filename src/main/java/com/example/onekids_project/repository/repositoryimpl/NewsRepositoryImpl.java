package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.repository.repositorycustom.NewsRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsRepositoryImpl extends BaseRepositoryimpl<News> implements NewsRepositoryCustom {

    @Override
    public List<News> findAllNews(SearchNewsRequest searchNewsRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (StringUtils.isNotBlank(searchNewsRequest.getTitle())) {
            queryStr.append("and title like :title");
            mapParams.put("title", "%"+searchNewsRequest.getTitle()+"%");
        }
        queryStr.append(" order by id DESC");
        return findAll(queryStr.toString(), mapParams, null);
    }
}
