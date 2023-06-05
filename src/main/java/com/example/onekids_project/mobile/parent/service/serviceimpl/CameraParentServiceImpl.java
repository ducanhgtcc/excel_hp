package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.Camera;
import com.example.onekids_project.mobile.parent.response.CameraParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.CameraParentService;
import com.example.onekids_project.repository.CameraRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CameraParentServiceImpl implements CameraParentService {

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private CameraRepository cameraRepository;


    @Override
    public List<CameraParentResponse> findCameraParent(UserPrincipal principal) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found Kids by id in camera"));
        List<Camera> cameraList = cameraRepository.findByIdSchoolAndDelActiveTrueAndMaClassList_Id(principal.getIdSchoolLogin(), kids.getMaClass().getId());
        List<CameraParentResponse> dataList = new ArrayList<>();
        cameraList.stream().filter(x->x.getDvrCamera().isDvrActive() && x.isCamActive()).forEach(x -> {
            CameraParentResponse model = new CameraParentResponse();
            model.setName(x.getCamName());

            model.setLink(x.getDvrCamera().getDvrType().equals(AppConstant.TYPE_OTHER) ? x.getLinkCam() : x.getDvrCamera().getLinkDvr() + x.getLinkCam());
            dataList.add(model);
        });
        return dataList;
    }
}
