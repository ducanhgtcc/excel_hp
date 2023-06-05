package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.mobile.teacher.response.VideoTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.VideoTeacherService;
import com.example.onekids_project.repository.MediaRepository;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
public class VideoTeacherServiceImpl implements VideoTeacherService {

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    SysInforRepository sysInforRepository;

    @Override
    public VideoTeacherResponse findVideoTeacher(UserPrincipal principal) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        List<Media> mediaList = mediaRepository.findByMediaActiveTrueAndDelActiveTrueAndMediaTypeAndMaClassList_Id(AppConstant.TYPE_VIDEO, idClass);
        VideoTeacherResponse model = new VideoTeacherResponse();
        mediaList.forEach(x -> {
            if (x.getScopeType().equals(AppConstant.TYPE_SCHOOL)) {
                model.setLinkSchool(x.getLinkMedia());
            }
            if (x.getScopeType().equals(AppConstant.TYPE_CLASS)) {
                model.setLinkClass(x.getLinkMedia());
            }
        });
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        model.setLinkOnekids(StringUtils.isNotBlank(sysInfor.getVideoLink()) ? sysInfor.getVideoLink() : "");
        return model;
    }
}
