package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.master.request.SearchNewsRequest;

import java.util.List;

public interface NewsRepositoryCustom {
    List<News> findAllNews(SearchNewsRequest searchNewsRequest);
}
