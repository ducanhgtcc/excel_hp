package com.example.onekids_project.service.serviceimpl.onecam;

import com.example.onekids_project.entity.onecam.OneCamConfig;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.OneCamConfigRepository;
import com.example.onekids_project.request.onecam.OneCamConfigRequest;
import com.example.onekids_project.response.onecam.OneCamConfigResponse;
import com.example.onekids_project.service.servicecustom.onecam.OneCamConfigService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lavanviet
 */
@Service
public class OneCamConfigServiceImpl implements OneCamConfigService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OneCamConfigRepository oneCamConfigRepository;


    @Override
    public void createOneCamConfigDefault(School school) {
        OneCamConfig entity = new OneCamConfig();
        entity.setSchool(school);
        entity.setPlusStatus(true);
        entity.setTeacherStatus(true);
        entity.setParentStatus(true);
        entity.setPlusNumber(5);
        entity.setTeacherNumber(5);
        entity.setParentNumber(5);
        oneCamConfigRepository.save(entity);
    }

    @Override
    public OneCamConfigResponse getOneCamConfigService(Long idSchool) {
        OneCamConfig entity = oneCamConfigRepository.findBySchoolId(idSchool).orElseThrow();
        return modelMapper.map(entity, OneCamConfigResponse.class);
    }

    @Override
    public void getOneCamConfigService(OneCamConfigRequest request) {
        OneCamConfig entity = oneCamConfigRepository.findById(request.getId()).orElseThrow();
        modelMapper.map(request, entity);
        oneCamConfigRepository.save(entity);
    }
}
