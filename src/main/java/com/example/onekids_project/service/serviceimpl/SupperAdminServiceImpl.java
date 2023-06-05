package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.usermaster.SupperAdmin;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.KidsStatusTimelineRepository;
import com.example.onekids_project.repository.SupperAdminRepository;
import com.example.onekids_project.security.payload.AdminDataRequest;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.servicecustom.SupperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SupperAdminServiceImpl implements SupperAdminService {
    @Autowired
    private SupperAdminRepository supperAdminRepository;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private KidsStatusTimelineRepository kidsStatusTimelineRepository;

    @Transactional
    @Override
    public boolean createSupperAdmin(AdminDataRequest adminDataRequest) {
        MaUser maUser = maUserService.createAccountSupperAdmin(adminDataRequest);
        SupperAdmin supperAdmin = new SupperAdmin();
        supperAdmin.setMaUser(maUser);
        supperAdmin.setFullName(maUser.getFullName());
        supperAdmin.setIdCreated(SystemConstant.ID_SYSTEM);
        supperAdmin.setCreatedDate(LocalDateTime.now());
        supperAdminRepository.save(supperAdmin);
        return true;
    }
}
