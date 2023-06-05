package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.school.Camera;
import com.example.onekids_project.entity.school.DvrCamera;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.CameraRepository;
import com.example.onekids_project.repository.DvrCameraRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.schoolconfig.CameraActiveRequest;
import com.example.onekids_project.request.schoolconfig.CameraCreateRequest;
import com.example.onekids_project.request.schoolconfig.CameraUpdateRequest;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import com.example.onekids_project.response.schoolconfig.CameraForClassResponse;
import com.example.onekids_project.response.schoolconfig.CameraResponse;
import com.example.onekids_project.response.schoolconfig.CameraSettingResponse;
import com.example.onekids_project.service.servicecustom.CameraService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CameraServiceImpl implements CameraService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private DvrCameraRepository dvrCameraRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Override
    public List<CameraResponse> findAllCamera(Long idSchool) {
        List<Camera> cameraList = cameraRepository.findByIdSchoolAndDelActiveTrueOrderByCamNameAsc(idSchool);
        if (CollectionUtils.isEmpty(cameraList)) {
            return null;
        }
        cameraList.forEach(x -> {
            if (x.getDvrCamera().getDvrType().equals(AppConstant.TYPE_OTHER)) {
                x.getDvrCamera().setLinkDvr("");
            }
        });
        List<CameraResponse> cameraResponseList = listMapper.mapList(cameraList, CameraResponse.class);
        return cameraResponseList;
    }

    @Override
    public CameraResponse createCamera(Long idSchool, CameraCreateRequest cameraCreateRequest) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActiveTrue(idSchool);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        Optional<DvrCamera> dvrCameraOptional = dvrCameraRepository.findByIdAndDelActiveTrue(cameraCreateRequest.getIdDvrcamera());
        if (dvrCameraOptional.isEmpty()) {
            return null;
        }
        DvrCamera dvrCamera = dvrCameraOptional.get();
        Camera camera = modelMapper.map(cameraCreateRequest, Camera.class);
        camera.setIdSchool(idSchool);
        camera.setDvrCamera(dvrCamera);
        Camera cameraSaved = cameraRepository.save(camera);
        CameraResponse cameraResponse = modelMapper.map(cameraSaved, CameraResponse.class);
        return cameraResponse;
    }

    @Override
    public CameraResponse updateCamera(Long idSchool, CameraUpdateRequest cameraUpdateRequest) {
        Optional<Camera> cameraOptional = cameraRepository.findByIdAndDelActiveTrue(cameraUpdateRequest.getId());
        if (cameraOptional.isEmpty()) {
            return null;
        }
        Optional<DvrCamera> dvrCameraOptional = dvrCameraRepository.findByIdAndDelActiveTrue(cameraUpdateRequest.getIdDvrcamera());
        if (dvrCameraOptional.isEmpty()) {
            return null;
        }
        Camera camera = cameraOptional.get();
        DvrCamera dvrCamera = dvrCameraOptional.get();
        modelMapper.map(cameraUpdateRequest, camera);
        camera.setDvrCamera(dvrCamera);
        Camera cameraSaved = cameraRepository.save(camera);
        CameraResponse cameraResponse = modelMapper.map(cameraSaved, CameraResponse.class);
        return cameraResponse;
    }

    @Override
    public boolean deleteCameraOne(Long id) {
        Optional<Camera> cameraOptional = cameraRepository.findByIdAndDelActiveTrue(id);
        if (cameraOptional.isEmpty()) {
            return false;
        }
        Camera camera = cameraOptional.get();
        long countNumber = camera.getMaClassList().stream().filter(x -> x.isDelActive()).count();
        if (countNumber > 0) {
            return false;
        }
        camera.setDelActive(AppConstant.APP_FALSE);
        cameraRepository.save(camera);
        return true;
    }

    @Override
    public boolean deleteMediaMany(List<IdObjectRequest> idObjectRequestList) {
        if (CollectionUtils.isEmpty(idObjectRequestList)) {
            return false;
        }
        for (IdObjectRequest idObjectRequest : idObjectRequestList) {
            Optional<Camera> cameraOptional = cameraRepository.findByIdAndDelActiveTrue(idObjectRequest.getId());
            if (cameraOptional.isEmpty()) {
                return false;
            }
            Camera camera = cameraOptional.get();
            camera.setDelActive(AppConstant.APP_FALSE);
            cameraRepository.save(camera);
        }
        return true;
    }

    @Override
    public boolean checkActiveCamera(CameraActiveRequest cameraActiveRequest) {
        Optional<Camera> cameraOptional = cameraRepository.findByIdAndDelActiveTrue(cameraActiveRequest.getId());
        if (cameraOptional.isEmpty()) {
            return false;
        }
        Camera camera = cameraOptional.get();
        camera.setCamActive(cameraActiveRequest.getCamActive());
        cameraRepository.save(camera);
        return true;
    }

    @Override
    public List<CameraSettingResponse> findAllCameraSetting(Long idSchool, MediaSettingSearchRequest mediaSettingSearchRequest) {
        List<MaClass> maClassList = maClassRepository.searchMaclassForMediaSetting(idSchool, mediaSettingSearchRequest);
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        List<CameraSettingResponse> cameraSettingResponseList = listMapper.mapList(maClassList, CameraSettingResponse.class);
        return cameraSettingResponseList;
    }

    @Override
    public List<CameraForClassResponse> findCameraForClass(Long idSchool, Long idClass) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndDelActiveTrue(idClass);
        if (maClassOptional.isEmpty()) {
            return null;
        }
        List<Camera> cameraList = cameraRepository.findByIdSchoolAndDelActiveTrueOrderByCamNameAsc(idSchool);
        if (CollectionUtils.isEmpty(cameraList)) {
            return null;
        }
        List<Long> mediaOfClassList = cameraRepository.findByMaClassList_Id(idClass).stream().map(x -> x.getId()).collect(Collectors.toList());
        List<CameraForClassResponse> cameraForClassResponseList = listMapper.mapList(cameraList, CameraForClassResponse.class);
        cameraForClassResponseList.forEach(y -> {
            mediaOfClassList.forEach(z -> {
                if (y.getId().equals(z)) {
                    y.setUsed(AppConstant.APP_TRUE);
                }
            });
        });
        return cameraForClassResponseList;
    }

    @Transactional
    @Override
    public boolean updateCameraForClass(Long idClass, List<IdObjectRequest> idObjectRequestList) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndDelActiveTrue(idClass);
        if (maClassOptional.isEmpty()) {
            return false;
        }
        cameraRepository.deleteCameraForClass(idClass);
        idObjectRequestList.forEach(x -> {
            cameraRepository.insertCameraForClass(idClass, x.getId());
        });
        return true;
    }
}
