package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.NewsExtra;
import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.NewsParentService;
import com.example.onekids_project.repository.NewsExtraRepository;
import com.example.onekids_project.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsParentServiceImpl implements NewsParentService {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsExtraRepository newsExtraRepository;

    @Override
    public List<NewsMobileResponse> findNews() {
        List<News> newsList = newsRepository.findByAppParentTrueAndDelActiveTrueOrderByCreatedDateDesc();
        List<NewsMobileResponse> dataList = new ArrayList<>();
        newsList.forEach(x -> {
            NewsMobileResponse model = new NewsMobileResponse();
            model.setDate(x.getCreatedDate().toLocalDate());
            model.setTitle(x.getTitle());
            model.setLink(x.getLink());
            model.setPicture(x.getUrlAttachPicture());
            dataList.add(model);
        });
        NewsExtra newsExtra = newsExtraRepository.findFirstByAppParentTrueAndDelActiveTrue().orElseThrow(() -> new NotFoundException("not found newsExtra by for parent"));
        NewsMobileResponse model = new NewsMobileResponse();
        model.setDate(newsExtra.getCreatedDate().toLocalDate());
        model.setTitle(newsExtra.getTitle());
        model.setLink(newsExtra.getLink());
        model.setPicture("");
        dataList.add(model);
        return dataList;
    }
}
