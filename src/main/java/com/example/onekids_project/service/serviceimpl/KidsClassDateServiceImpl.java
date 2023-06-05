package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.repository.KidsClassDateRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.service.servicecustom.KidsClassDateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KidsClassDateServiceImpl implements KidsClassDateService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private KidsClassDateRepository kidsClassDateRepository;

}
