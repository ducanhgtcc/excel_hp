package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.system.MenuSupport;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.menu.MenuSupportRequest;
import com.example.onekids_project.master.request.menu.MenuUpdateRequest;
import com.example.onekids_project.master.response.MenuSupportResponse;
import com.example.onekids_project.repository.MenuSupportRepository;
import com.example.onekids_project.service.servicecustom.MenuSupportService;
import com.example.onekids_project.util.PrincipalUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-10-15 14:45
 *
 * @author lavanviet
 */
@Service
public class MenuSupportServiceImpl implements MenuSupportService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MenuSupportRepository menuSupportRepository;

    @Override
    public List<MenuSupportResponse> getSupport() {
        List<MenuSupport> menuSupportList = menuSupportRepository.findAll();
        return listMapper.mapList(menuSupportList, MenuSupportResponse.class);
    }

    @Override
    public void createSupport(MenuSupportRequest request) {
        MenuSupport menuSupport = modelMapper.map(request, MenuSupport.class);
        menuSupportRepository.save(menuSupport);
    }

    @Override
    public void updateSupport(MenuUpdateRequest request) {
        MenuSupport menuSupport = menuSupportRepository.findById(request.getId()).orElseThrow();
        modelMapper.map(request, menuSupport);
        menuSupportRepository.save(menuSupport);
    }

    @Override
    public void deleteSupportById(Long id) {
        menuSupportRepository.deleteById(id);
    }

    @Override
    public List<MenuSupportResponse> showMenuSupport() {
        String appType = PrincipalUtils.getUserPrincipal().getAppType();
        List<MenuSupport> menuSupportList = new ArrayList<>();
        if (StringUtils.equals(appType, AppTypeConstant.SCHOOL)) {
            menuSupportList = menuSupportRepository.findByPlusStatusTrue();
        } else if (StringUtils.equals(appType, AppTypeConstant.TEACHER)) {
            menuSupportList = menuSupportRepository.findByTeacherStatusTrue();
        } else if (StringUtils.equals(appType, AppTypeConstant.PARENT)) {
            menuSupportList = menuSupportRepository.findByParentStatusTrue();
        }
        return listMapper.mapList(menuSupportList, MenuSupportResponse.class);
    }
}
