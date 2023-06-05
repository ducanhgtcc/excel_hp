package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.mobile.parent.response.VideoParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.VideoParentService;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MediaRepository;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoParentServiceImpl implements VideoParentService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private SysInforRepository sysInforRepository;

    @Override
    public VideoParentResponse findVideoParent(UserPrincipal principal) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found Kids by id in vide parent"));
        List<Media> mediaList = mediaRepository.findByMediaActiveTrueAndDelActiveTrueAndMediaTypeAndMaClassList_Id(AppConstant.TYPE_VIDEO, kids.getMaClass().getId());
        VideoParentResponse model = new VideoParentResponse();
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

    @Override
    public List<String> findVideoOther(UserPrincipal principal) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found Kids by id in vide parent"));
        List<Media> mediaList = mediaRepository.findByMediaActiveTrueAndDelActiveTrueAndMediaTypeAndMaClassList_Id(AppConstant.TYPE_OTHER, kids.getMaClass().getId());
        return mediaList.stream().map(Media::getLinkMedia).collect(Collectors.toList());
    }
}
