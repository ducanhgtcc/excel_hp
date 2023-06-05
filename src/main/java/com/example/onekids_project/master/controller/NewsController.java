package com.example.onekids_project.master.controller;

import com.example.onekids_project.master.request.NewsRequest;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.master.response.NewsResponse;
import com.example.onekids_project.master.service.NewsService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/news-onekids")
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    /**
     * Tìm kiếm tin tức
     *
     * @param searchNewsRequest
     * @return
     */
    @GetMapping
    public ResponseEntity searchNewsAdmin(SearchNewsRequest searchNewsRequest) {
        List<NewsResponse> newsResponseList = newsService.getAllNews(searchNewsRequest);
        return NewDataResponse.setDataSearch(newsResponseList);
    }

    /**
     * Thêm mới tin tức
     *
     * @param newsRequest
     * @return
     */
    @PostMapping
    public ResponseEntity createNewsAdmin(@Valid @RequestBody NewsRequest newsRequest) throws FirebaseMessagingException {
        boolean checkCreate = newsService.saveNews(newsRequest);
        return NewDataResponse.setDataCreate(checkCreate);
    }


    /**
     * Sửa tin tức
     *
     * @param newsRequest
     * @return
     */
    @PutMapping
    public ResponseEntity updateNews(@Valid @RequestBody NewsRequest newsRequest) {
        boolean checkUpdate = newsService.updateNews(newsRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        boolean checkDelete = newsService.deleteNews(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    @PutMapping("/delete-multi")
    public ResponseEntity deleteMultiNews(@RequestBody Long[] id) {
        boolean checkDelete = newsService.deleteMultiNews(id);
        return NewDataResponse.setDataDelete(checkDelete);

    }
}
