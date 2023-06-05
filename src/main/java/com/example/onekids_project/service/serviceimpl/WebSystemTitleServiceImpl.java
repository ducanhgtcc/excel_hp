package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.system.WebSystemTitleConfigRequest;
import com.example.onekids_project.response.system.WebSystemTitleConfigResponse;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WebSystemTitleServiceImpl implements WebSystemTitleService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Override
    public List<WebSystemTitleConfigResponse> findAllWebSystemTitle() {
        List<WebSystemTitle> webSystemTitleList = webSystemTitleRepository.findByDelActiveTrue();
        if (CollectionUtils.isEmpty(webSystemTitleList)) {
            return null;
        }
        List<WebSystemTitleConfigResponse> webSystemTitleConfigResponseList = listMapper.mapList(webSystemTitleList, WebSystemTitleConfigResponse.class);
        return webSystemTitleConfigResponseList;
    }

    @Override
    public Optional<WebSystemTitle> findById(Long id) {
        Optional<WebSystemTitle> webSystemTitleConfigResponse = webSystemTitleRepository.findByIdAndDelActiveTrue(id);
        return webSystemTitleConfigResponse;
    }

    @Transactional
    @Override
    public boolean updateWebSystemTitle(List<WebSystemTitleConfigRequest> webSystemTitleConfigRequestList) {
        if (CollectionUtils.isEmpty(webSystemTitleConfigRequestList)) {
            return false;
        }
        webSystemTitleConfigRequestList.forEach(x -> {
            WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow(() -> new NotFoundException("không tìm thấy websystemtitle theo id"));
            modelMapper.map(x, webSystemTitle);
            webSystemTitleRepository.save(webSystemTitle);
        });
        return true;
    }

    @Override
    public void createWebSystemTitle() {
//        List<WebSystemTitle> webSystemTitleList = webSystemTitleRepository.findByDelActiveTrue();
//        if (!CollectionUtils.isEmpty(webSystemTitleList)) {
//            return;
//        }
//        for (int i = 1; i <= 60; i++) {
//            WebSystemTitle webSystemTitle = new WebSystemTitle();
//            webSystemTitle.setIdCreated(SystemConstant.ID_SYSTEM);
//            webSystemTitle.setCreatedDate(LocalDateTime.now());
//            if (i == 1 || i == 2) {
//                webSystemTitle.setType(SystemConstant.TITLE_WEB);
//            } else if (i > 2 && i <= 8) {
//                webSystemTitle.setType(SystemConstant.TITLE_PLUS);
//            } else if (i > 8 && i <= 14) {
//                webSystemTitle.setType(SystemConstant.TITLE_TEACHER);
//            } else {
//                webSystemTitle.setType(SystemConstant.TITLE_SYSTEM);
//            }
//            webSystemTitleRepository.save(webSystemTitle);
//        }
    }
}
