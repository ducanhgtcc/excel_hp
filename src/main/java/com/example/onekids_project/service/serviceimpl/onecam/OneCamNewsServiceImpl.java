package com.example.onekids_project.service.serviceimpl.onecam;

import com.example.onekids_project.entity.onecam.OneCamNews;
import com.example.onekids_project.repository.OneCamNewsRepository;
import com.example.onekids_project.response.onecam.OneCamNewsResponse;
import com.example.onekids_project.service.servicecustom.onecam.OneCamNewsService;
import com.example.onekids_project.util.SchoolUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lavanviet
 */
@Service
public class OneCamNewsServiceImpl implements OneCamNewsService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OneCamNewsRepository oneCamNewsRepository;

    @Override
    public void createOneCamNews(Long idSchool) {
        OneCamNews model = new OneCamNews();
        model.setIdSchool(idSchool);
        oneCamNewsRepository.save(model);
    }

    @Override
    public OneCamNewsResponse getOneCamNews() {
        Long idSchool = SchoolUtils.getIdSchool();
        OneCamNews oneCamNews = oneCamNewsRepository.findByIdSchool(idSchool).orElseThrow();
        return modelMapper.map(oneCamNews, OneCamNewsResponse.class);
    }

    @Override
    public void updateOneCamNews(OneCamNewsResponse request) {
        OneCamNews oneCamNews = oneCamNewsRepository.findById(request.getId()).orElseThrow();
        modelMapper.map(request, oneCamNews);
        oneCamNewsRepository.save(oneCamNews);

    }
}
