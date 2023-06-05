package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.UploadDownloadConstant;
import com.example.onekids_project.common.UrlFileConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsExtraInfo;
import com.example.onekids_project.mobile.parent.request.kids.KidsExtraInforRequest;
import com.example.onekids_project.mobile.parent.response.kids.KidsExtraInforResponse;
import com.example.onekids_project.mobile.parent.response.kids.KidsParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsParentService;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;

@Service
public class KidsParentServiceImpl implements KidsParentService {
    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public KidsParentResponse findKidById(UserPrincipal principal) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found kids by id in kidInfo"));
        KidsParentResponse model = new KidsParentResponse();
        model.setFullName(kids.getFullName());
        model.setGender(kids.getGender());
        model.setBirthDay(kids.getBirthDay());
        model.setClassName(kids.getMaClass().getClassName());
        model.setAvatar(ConvertData.getAvatarKid(kids));
        return model;
    }

    @Override
    public KidsExtraInforResponse findKidsExtraInfo(UserPrincipal principal) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found kids by id in kidsExtra"));
        KidsExtraInforResponse model = new KidsExtraInforResponse();
        this.setProperties(kids, model);
        return model;
    }

    @Override
    public KidsExtraInforResponse updateKidsExtraInfo(UserPrincipal principal, KidsExtraInforRequest kidsExtraInforRequest) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found kids by id in kidsExtra"));
        KidsExtraInforResponse model = new KidsExtraInforResponse();
        Long idSchool = principal.getIdSchoolLogin();
        MultipartFile multipartFile = kidsExtraInforRequest.getAvatar();
        //update avatar
        if (multipartFile != null) {
            //xóa avatar in folder
            String urlLocalOld = kids.getAvatarKidLocal();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);

            String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR);
            String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
            try {
                HandleFileUtils.createPictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.AVATAR) + fileName;
            String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR) + fileName;
            kids.setAvatarKid(urlWeb);
            kids.setAvatarKidLocal(urlLocal);
        }
        kids.setNickName(kidsExtraInforRequest.getNickName());
        modelMapper.map(kidsExtraInforRequest, kids.getKidsExtraInfo());
        Kids kidsSaved = kidsRepository.save(kids);
        this.setProperties(kidsSaved, model);
        return model;
    }

    /**
     * set properties
     *
     * @param kids
     * @param model
     */
    private void setProperties(Kids kids, KidsExtraInforResponse model) {
        KidsExtraInfo extraInfo = kids.getKidsExtraInfo();
        model.setAvatar(ConvertData.getAvatarKid(kids));
        model.setNickName(StringUtils.isNotBlank(kids.getNickName()) ? kids.getNickName() : "");
        model.setBloodType(StringUtils.isNotBlank(extraInfo.getBloodType()) ? extraInfo.getBloodType() : "");
        model.setSwim(StringUtils.isNotBlank(extraInfo.getSwim()) ? extraInfo.getSwim() : "");
        model.setAllery(StringUtils.isNotBlank(extraInfo.getAllery()) ? extraInfo.getAllery() : "");
        model.setDiet(StringUtils.isNotBlank(extraInfo.getDiet()) ? extraInfo.getDiet() : "");
        model.setEar(StringUtils.isNotBlank(extraInfo.getEar()) ? extraInfo.getEar() : "");
        model.setNose(StringUtils.isNotBlank(extraInfo.getNose()) ? extraInfo.getNose() : "");
        model.setThroat(StringUtils.isNotBlank(extraInfo.getThroat()) ? extraInfo.getThroat() : "");
        model.setEyes(StringUtils.isNotBlank(extraInfo.getEyes()) ? extraInfo.getEyes() : "");
        model.setSkin(StringUtils.isNotBlank(extraInfo.getSkin()) ? extraInfo.getSkin() : "");
        model.setHeart(StringUtils.isNotBlank(extraInfo.getHeart()) ? extraInfo.getHeart() : "");
        model.setFat(StringUtils.isNotBlank(extraInfo.getFat()) ? extraInfo.getFat() : "");
    }
}
