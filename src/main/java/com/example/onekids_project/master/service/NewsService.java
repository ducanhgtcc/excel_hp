package com.example.onekids_project.master.service;


import com.example.onekids_project.master.request.NewsRequest;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.master.response.NewsResponse;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface NewsService {

    List<NewsResponse> getAllNews(SearchNewsRequest searchNewsRequest);

    boolean saveNews(NewsRequest newsRequest) throws FirebaseMessagingException;

    boolean updateNews(NewsRequest newsRequest);

    boolean deleteNews(Long id);

    boolean deleteMultiNews(Long ids[]);

}
