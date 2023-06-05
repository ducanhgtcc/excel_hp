package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.NewsExtra;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.repository.NewsExtraRepository;
import com.example.onekids_project.repository.repositorycustom.NewsExtraRepositoryCustom;
import com.example.onekids_project.repository.repositorycustom.NewsRepositoryCustom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NewsExtraRepositoryImpl extends BaseRepositoryimpl<NewsExtra> implements NewsExtraRepositoryCustom {

    @Override
    public List<NewsExtra> findAllNewsExtra(SearchNewsRequest searchNewsRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (StringUtils.isNotBlank(searchNewsRequest.getTitle())) {
            queryStr.append("and title=:title");
            mapParams.put("title",searchNewsRequest.getTitle());
        }
        queryStr.append(" order by id DESC");
        return findAll(queryStr.toString(), mapParams, null);
    }
}
