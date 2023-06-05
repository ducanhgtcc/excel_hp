package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.school.Camera;
import com.example.onekids_project.mobile.teacher.response.CameraTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.CameraTeacherService;
import com.example.onekids_project.repository.CameraRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CameraTeacherServiceImpl implements CameraTeacherService {

    @Autowired
    KidsRepository kidsRepository;

    @Autowired
    CameraRepository cameraRepository;

    @Override
    public List<CameraTeacherResponse> findCameraTeacher(UserPrincipal principal) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        List<Camera> cameraList = cameraRepository.findByIdSchoolAndDelActiveTrueAndMaClassList_Id(principal.getIdSchoolLogin(), idClass);
        List<CameraTeacherResponse> dataList = new ArrayList<>();
        cameraList.stream().filter(x -> x.getDvrCamera().isDvrActive() && x.isCamActive()).forEach(x -> {
            CameraTeacherResponse model = new CameraTeacherResponse();
            model.setName(x.getCamName());
            model.setLink(x.getDvrCamera().getDvrType().equals(AppConstant.TYPE_OTHER) ? x.getLinkCam() : x.getDvrCamera().getLinkDvr() + x.getLinkCam());
            dataList.add(model);
        });
        return dataList;
    }
}
