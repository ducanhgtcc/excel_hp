package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.master.request.NewsRequest;
import com.example.onekids_project.master.request.SearchNewsRequest;
import com.example.onekids_project.master.response.NewsResponse;
import com.example.onekids_project.master.service.NewsService;
import com.example.onekids_project.repository.*;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Override
    public List<NewsResponse> getAllNews(SearchNewsRequest searchNewsRequest) {
        List<News> newsList = newsRepository.findAllNews(searchNewsRequest);
        List<NewsResponse> newsResponseList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(newsList)) {
            newsList.forEach(news -> {
                NewsResponse newsResponse = modelMapper.map(news, NewsResponse.class);
                MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(news.getIdCreated()).get();
                if (StringUtils.isNotBlank(maUser.getFullName())) {
                    newsResponse.setCreatedBy(maUser.getFullName());
                }
                newsResponseList.add(newsResponse);
            });
        }
        return newsResponseList;
    }

    @Override
    public boolean saveNews(NewsRequest newsRequest) throws FirebaseMessagingException {
        News news = modelMapper.map(newsRequest, News.class);
        News newsSave = newsRepository.save(news);
        if (newsSave.isAppPlus()) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeStatusAndAppTypeAndEmployeeIsNotNullAndDelActiveTrue(AppConstant.EMPLOYEE_STATUS_WORKING, AppTypeConstant.SCHOOL);
            this.sendFirebaseEmployee(infoEmployeeSchoolList, newsSave);
        }
        if (newsSave.isAppTeacher()) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByEmployeeStatusAndAppTypeAndEmployeeIsNotNullAndDelActiveTrue(AppConstant.EMPLOYEE_STATUS_WORKING, AppTypeConstant.TEACHER);
            this.sendFirebaseEmployee(infoEmployeeSchoolList, newsSave);
        }
        if (newsSave.isAppParent()) {
            List<Kids> kidsList = kidsRepository.findByKidStatusAndParentIsNotNullAndDelActiveTrue(AppConstant.STUDYING);
            this.sendFirebaseStudent(kidsList, newsSave);
        }
        return true;
    }

    @Override
    public boolean updateNews(NewsRequest newsRequest) {
        News news = newsRepository.findById(newsRequest.getId()).orElseThrow();
        modelMapper.map(newsRequest, news);
        newsRepository.save(news);
        return true;
    }

    @Override
    public boolean deleteNews(Long id) {
        News news = newsRepository.findById(id).orElseThrow();
        newsRepository.delete(news);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteMultiNews(Long ids[]) {
        for (Long id : ids) {
            News news = newsRepository.findById(id).orElseThrow();
            newsRepository.delete(news);
        }
        return true;
    }

    private void sendFirebaseEmployee(List<InfoEmployeeSchool> infoEmployeeSchoolList, News newsSave) throws FirebaseMessagingException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(78L).orElseThrow();
        for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
            firebaseFunctionService.sendPlusCommon(Collections.singletonList(x), webSystemTitle.getTitle(), newsSave.getTitle(), x.getSchool().getId(), FirebaseConstant.CATEGORY_NOTIFY);
        }
    }

    private void sendFirebaseStudent(List<Kids> kidsList, News newsSave) throws FirebaseMessagingException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(78L).orElseThrow();
        for (Kids x : kidsList) {
            firebaseFunctionService.sendParentCommon(Collections.singletonList(x), webSystemTitle.getTitle(), newsSave.getTitle(), x.getIdSchool(), FirebaseConstant.CATEGORY_NOTIFY);
        }
    }
}
