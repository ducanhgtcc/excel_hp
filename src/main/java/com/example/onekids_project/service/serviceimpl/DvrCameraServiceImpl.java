package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.school.DvrCamera;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.DvrCameraRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.schoolconfig.DvrCameraCreateRequest;
import com.example.onekids_project.request.schoolconfig.DvrCameraUpdateRequest;
import com.example.onekids_project.request.schoolconfig.DvrcameraActiveRequest;
import com.example.onekids_project.response.schoolconfig.DvrCameraResponse;
import com.example.onekids_project.service.servicecustom.DvrCameraService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class DvrCameraServiceImpl implements DvrCameraService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private DvrCameraRepository dvrCameraRepository;

    @Override
    public List<DvrCameraResponse> findAllDvrCamera(Long idSchool) {
        List<DvrCamera> dvrCameraList = dvrCameraRepository.findBySchoolIdAndDelActiveTrueOrderByDvrNameAsc(idSchool);
        if (CollectionUtils.isEmpty(dvrCameraList)) {
            return null;
        }
        List<DvrCameraResponse> dvrCameraResponseList = listMapper.mapList(dvrCameraList, DvrCameraResponse.class);
        return dvrCameraResponseList;
    }

    @Override
    public DvrCameraResponse createDvrCamera(Long idSchool, DvrCameraCreateRequest dvrCameraCreateRequest) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActiveTrue(idSchool);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        School school = schoolOptional.get();
        DvrCamera dvrCamera = modelMapper.map(dvrCameraCreateRequest, DvrCamera.class);
        dvrCamera.setSchool(school);
        DvrCamera dvrCameraSaved = dvrCameraRepository.save(dvrCamera);
        DvrCameraResponse dvrCameraResponse = modelMapper.map(dvrCameraSaved, DvrCameraResponse.class);
        return dvrCameraResponse;
    }

    @Override
    public DvrCameraResponse updateDvrCamera(Long idSchool, DvrCameraUpdateRequest dvrCameraUpdateRequest) {
        Optional<DvrCamera> dvrCameraOptional = dvrCameraRepository.findByIdAndDelActiveTrue(dvrCameraUpdateRequest.getId());
        if (dvrCameraOptional.isEmpty()) {
            return null;
        }
        DvrCamera dvrCamera = dvrCameraOptional.get();
        modelMapper.map(dvrCameraUpdateRequest, dvrCamera);
        DvrCamera dvrCameraSaved = dvrCameraRepository.save(dvrCamera);
        DvrCameraResponse dvrCameraResponse = modelMapper.map(dvrCameraSaved, DvrCameraResponse.class);
        return dvrCameraResponse;
    }

    @Override
    public boolean deleteDvrcameraOne(Long id) {
        Optional<DvrCamera> dvrCameraOptional = dvrCameraRepository.findByIdAndDelActiveTrue(id);
        if (dvrCameraOptional.isEmpty()) {
            return false;
        }
        DvrCamera dvrCamera = dvrCameraOptional.get();
        long countNumber = dvrCamera.getCameraList().stream().filter(x -> x.isDelActive()).count();
        if (countNumber > 0) {
            return false;
        }
        dvrCamera.setDelActive(AppConstant.APP_FALSE);
        dvrCameraRepository.save(dvrCamera);
        return true;
    }

    @Override
    public boolean deleteMediaMany(List<IdObjectRequest> idObjectRequestList) {
        if (CollectionUtils.isEmpty(idObjectRequestList)) {
            return false;
        }
        for (IdObjectRequest idObjectRequest : idObjectRequestList) {
            Optional<DvrCamera> dvrCameraOptional = dvrCameraRepository.findByIdAndDelActiveTrue(idObjectRequest.getId());
            if (dvrCameraOptional.isEmpty()) {
                return false;
            }
            DvrCamera dvrCamera = dvrCameraOptional.get();
            dvrCamera.setDelActive(AppConstant.APP_FALSE);
            dvrCameraRepository.save(dvrCamera);
        }
        return true;
    }

    @Override
    public boolean checkActiveDvrcamera(DvrcameraActiveRequest dvrcameraActiveRequest) {
        Optional<DvrCamera> dvrCameraOptional = dvrCameraRepository.findByIdAndDelActiveTrue(dvrcameraActiveRequest.getId());
        if (dvrCameraOptional.isEmpty()) {
            return false;
        }
        DvrCamera dvrCamera = dvrCameraOptional.get();
        dvrCamera.setDvrActive(dvrcameraActiveRequest.getDvrActive());
        dvrCameraRepository.save(dvrCamera);
        return true;
    }
}
