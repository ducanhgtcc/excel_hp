package com.example.onekids_project.master.controller;

import com.example.onekids_project.master.request.NewsRequest;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.master.response.NewsExtraResponse;
import com.example.onekids_project.master.service.NewsExtraService;
import com.example.onekids_project.response.common.NewDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/news-extra-onekids")
public class NewsExtraController {
    @Autowired
    private NewsExtraService newsExtraService;

    /**
     * Tìm kiếm tin tức
     *
     * @param searchNewsRequest
     * @return
     */
    @GetMapping
    public ResponseEntity searchNewsExtra(SearchNewsRequest searchNewsRequest) {
        List<NewsExtraResponse> newsResponseList = newsExtraService.getAllNewsExtra(searchNewsRequest);
        return NewDataResponse.setDataSearch(newsResponseList);

    }

    /**
     * Thêm mới tin tức
     *
     * @param newsRequest
     * @return
     */
    @PostMapping
    public ResponseEntity createNews(@Valid @RequestBody NewsRequest newsRequest) {
        boolean checkCreate = newsExtraService.saveNewsExtra(newsRequest);
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
        boolean checkUpdate = newsExtraService.updateNewsExtra(newsRequest);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }

    /**
     * Xóa 1 tin tức
     *
     * @param
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        boolean checkDelete = newsExtraService.deleteNewsExtra(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    @PutMapping("/delete-multi")
    public ResponseEntity deleteMultiNews(@RequestBody Long[] id) {
        boolean checkDelete = newsExtraService.deleteMultiNewsExtra(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }
}
