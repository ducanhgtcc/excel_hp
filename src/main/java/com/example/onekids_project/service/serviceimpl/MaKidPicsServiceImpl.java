package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.repository.MaKidPicsRepository;
import com.example.onekids_project.service.servicecustom.MaKidPicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lavanviet
 */
@Service
public class MaKidPicsServiceImpl implements MaKidPicsService {
    @Autowired
    private MaKidPicsRepository maKidPicsRepository;

    @Override
    public void deleteDataKidPics(Kids kids) {
        kids.setPicJsonUrl(null);
        kids.setPicJsonUrlLocal(null);
        maKidPicsRepository.deleteByKidId(kids.getId());

    }
}
