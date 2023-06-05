package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.NewsExtra;
import com.example.onekids_project.master.request.SearchNewsRequest;

import java.util.List;

public interface NewsExtraRepositoryCustom {
    List<NewsExtra> findAllNewsExtra(SearchNewsRequest searchNewsRequest);
}
