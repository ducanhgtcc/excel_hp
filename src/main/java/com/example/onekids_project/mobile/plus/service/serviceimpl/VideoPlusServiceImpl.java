package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.entity.school.Camera;
import com.example.onekids_project.entity.school.DvrCamera;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.video.SearchVideoPlusRequest;
import com.example.onekids_project.mobile.plus.response.video.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.VideoPlusService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoPlusServiceImpl implements VideoPlusService {

    @Autowired
    MediaRepository mediaRepository;
    @Autowired
    SysInforRepository sysInforRepository;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private DvrCameraRepository dvrCameraRepository;

    @Override
    public ListVideoPlusResponse findVideoPlus(UserPrincipal principal, SearchVideoPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListVideoPlusResponse listVideoPlusResponse = new ListVideoPlusResponse();
        List<VideoPlusResponse> responseList = new ArrayList<>();

        List<Media> mediaforSchoolList = mediaRepository.findVideoSchool(idSchool);
        mediaforSchoolList.forEach(x -> {
            VideoPlusResponse model1 = new VideoPlusResponse();
            model1.setLink(x.getLinkMedia());
            model1.setKey(AppConstant.TYPE_VIDEO_SCHOOL);
            model1.setTypeVideo(AppConstant.SCHOOL);
            responseList.add(model1);
        });
        VideoPlusResponse model = new VideoPlusResponse();
        model.setLink(principal.getSysInfor().getVideoLink());
        model.setKey(AppConstant.TYPE_ONEKIDS);
        model.setTypeVideo("Onekids");
        responseList.add(model);
        List<Long> idClassList = maClassRepository.findIdClassInSchool(idSchool);
        idClassList.forEach(x -> {

            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            List<Long> idMediaList = mediaRepository.findIdMediaByIdClass(x);
            idMediaList.forEach(y -> {
                VideoPlusResponse model2 = new VideoPlusResponse();
                Media media = mediaRepository.findByIdAndDelActiveTrue(y).orElseThrow();
                if (StringUtils.equals(AppConstant.VIDEO, media.getMediaType()) && AppConstant.TYPE_CLASS.equals(media.getScopeType())) {
                    model2.setKey("");
                    model2.setTypeVideo(maClass.getClassName());
                    model2.setLink(media.getLinkMedia());
                    responseList.add(model2);
                }
            });
        });
        listVideoPlusResponse.setResponseList(responseList);
        return listVideoPlusResponse;
    }

    @Override
    public ListCameraPlusResponse searchCameraPlus(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListCameraPlusResponse listCameraPlusResponse = new ListCameraPlusResponse();
        List<CameraPlusResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        List<MaClassOtherResponse> maClassOtherResponseList = listMapper.mapList(maClassList, MaClassOtherResponse.class);
        maClassOtherResponseList.forEach(x -> {
            CameraPlusResponse model = new CameraPlusResponse();
            List<Long> idCamList = mediaRepository.findIdCamByIdClass(x.getId());
            model.setId(x.getId());
            model.setNumberCamera(idCamList.size());
            model.setClassName(x.getClassName());
            responseList.add(model);
        });
        listCameraPlusResponse.setDataList(responseList);
        return listCameraPlusResponse;
    }

    @Override
    public ListCameraClassDetailResponse findDeTailCameraClass(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        ListCameraClassDetailResponse listCameraClassDetailResponse = new ListCameraClassDetailResponse();
        List<CameraClassDetailResponse> responseList = new ArrayList<>();
        List<Long> idCamList = mediaRepository.findIdCamByIdClass(id);
        idCamList.forEach(x -> {
            CameraClassDetailResponse model = new CameraClassDetailResponse();
            Camera camera = cameraRepository.findById(x).orElseThrow();
            DvrCamera dvrCamera = dvrCameraRepository.findByIdAndDelActiveTrue(camera.getDvrCamera().getId()).orElseThrow();
            if (dvrCamera.isDvrActive()) {
                model.setCamName(camera.getCamName());
                model.setLinkCam(dvrCamera.getLinkDvr().concat(camera.getLinkCam()));
                responseList.add(model);
            } else {
                listCameraClassDetailResponse.setDataList(responseList);
            }
        });
        listCameraClassDetailResponse.setDataList(responseList);
        return listCameraClassDetailResponse;
    }

    @Override
    public ListCameraClassDetailResponse findAllCamera(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListCameraClassDetailResponse response = new ListCameraClassDetailResponse();
        List<CameraClassDetailResponse> responseList = new ArrayList<>();
        List<Camera> cameraList = cameraRepository.findAllByIdSchoolAndCamActiveTrueAndDelActiveTrue(idSchool);
        cameraList.stream().filter(x -> x.getDvrCamera().isDvrActive()).forEach(x -> {
            CameraClassDetailResponse model = new CameraClassDetailResponse();
            DvrCamera dvrCamera = dvrCameraRepository.findByIdAndDelActiveTrue(x.getDvrCamera().getId()).orElseThrow();
            model.setCamName(x.getCamName());
            model.setLinkCam(dvrCamera.getLinkDvr().concat(x.getLinkCam()));
            responseList.add(model);
        });
        response.setDataList(responseList);
        return response;
    }

}
