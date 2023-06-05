package com.example.onekids_project.master.service;


import com.example.onekids_project.master.request.NewsRequest;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.master.response.NewsExtraResponse;
import com.example.onekids_project.master.response.NewsResponse;

import java.util.List;

public interface NewsExtraService {

    List<NewsExtraResponse> getAllNewsExtra(SearchNewsRequest searchNewsRequest);

    boolean saveNewsExtra(NewsRequest newsRequest);

    boolean updateNewsExtra(NewsRequest newsRequest);

    boolean deleteNewsExtra(Long id);

    boolean deleteMultiNewsExtra(Long ids[]);

}
