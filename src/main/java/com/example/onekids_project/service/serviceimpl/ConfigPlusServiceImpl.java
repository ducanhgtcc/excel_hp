package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.entity.school.ConfigPlus;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.ConfigPlusRepository;
import com.example.onekids_project.response.schoolconfig.ConfigPlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.ConfigPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigPlusServiceImpl implements ConfigPlusService {

    @Autowired
    private ConfigPlusRepository configPlusRepository;


    @Override
    public void createConfigPlus(School school) {
        ConfigPlus configPlus = new ConfigPlus();
        configPlus.setSchool(school);
        configPlusRepository.save(configPlus);
    }

    @Override
    public ConfigPlusResponse getConfigPlusByIdSchool(UserPrincipal principal) {
        return null;
    }
}
