package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.repository.AccountTypeRepository;
import com.example.onekids_project.repository.ConfigPlusRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.service.servicecustom.ConfigPlusService;
import com.example.onekids_project.service.servicecustom.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private SchoolServiceImpl schoolService;

    @Autowired
    private ConfigPlusRepository configPlusRepository;
    @Autowired
    private ConfigPlusService configPlusService;

}
