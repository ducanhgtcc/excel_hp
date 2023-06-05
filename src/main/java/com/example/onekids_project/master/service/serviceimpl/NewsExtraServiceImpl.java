package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.NewsExtra;
import com.example.onekids_project.master.request.NewsRequest;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.master.response.NewsExtraResponse;
import com.example.onekids_project.master.response.NewsResponse;
import com.example.onekids_project.master.service.NewsExtraService;
import com.example.onekids_project.master.service.NewsService;
import com.example.onekids_project.repository.NewsExtraRepository;
import com.example.onekids_project.repository.NewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsExtraServiceImpl implements NewsExtraService {

    @Autowired
    private NewsExtraRepository newsExtraRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<NewsExtraResponse> getAllNewsExtra(SearchNewsRequest searchNewsRequest) {
        List<NewsExtra> newsExtraList = newsExtraRepository.findAllNewsExtra(searchNewsRequest);
        List<NewsExtraResponse> newsResponseList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(newsExtraList)) {
            newsExtraList.forEach(news -> {
                NewsExtraResponse newsExtraResponse = modelMapper.map(news, NewsExtraResponse.class);
                newsResponseList.add(newsExtraResponse);
            });
        }
        return newsResponseList;
    }

    @Override
    public boolean saveNewsExtra(NewsRequest newsRequest) {
        NewsExtra newsExtra=modelMapper.map(newsRequest,NewsExtra.class);
        newsExtra=newsExtraRepository.save(newsExtra);
        return true;
    }

    @Override
    public boolean updateNewsExtra(NewsRequest newsRequest) {
        Optional<NewsExtra> newsExtraOptional=newsExtraRepository.findById(newsRequest.getId());
        if(newsExtraOptional.isEmpty()){
            return false;
        }
        NewsExtra oldNewsExtra=newsExtraOptional.get();
        modelMapper.map(newsRequest,oldNewsExtra);
        newsExtraRepository.save(oldNewsExtra);
        return true;
    }

    @Override
    public boolean deleteNewsExtra(Long id) {
        Optional<NewsExtra> newsExtraOptional=newsExtraRepository.findById(id);
        if(newsExtraOptional.isEmpty()){
            return false;
        }
        NewsExtra oldNewsExtra=newsExtraOptional.get();
        newsExtraRepository.delete(oldNewsExtra);
        return true;
    }

    @Override
    public boolean deleteMultiNewsExtra(Long ids[]) {
        for (Long id:ids){
            Optional<NewsExtra> newsExtraOptional=newsExtraRepository.findById(id);
            if(newsExtraOptional.isEmpty()){
                return false;
            }
            NewsExtra oldNewsExtra=newsExtraOptional.get();
            newsExtraRepository.delete(oldNewsExtra);
        }
        return true;
    }
}
