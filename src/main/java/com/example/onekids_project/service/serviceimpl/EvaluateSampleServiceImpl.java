package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.sample.EvaluateSample;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.EvaluateSampleRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.schoolconfig.EvaluateSampleCreateRequest;
import com.example.onekids_project.request.schoolconfig.EvaluateSampleUpdateRequest;
import com.example.onekids_project.response.schoolconfig.EvaluateSampleConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.EvaluateSampleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluateSampleServiceImpl implements EvaluateSampleService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private EvaluateSampleRepository evaluateSampleRepository;

    @Override
    public List<EvaluateSampleConfigResponse> findAllEvaluateSample(Long idSchool, UserPrincipal principal) {
        List<EvaluateSample> evaluateSampleList;
        if (principal.getSchoolConfig().isShowEvaluateSys()) {
            evaluateSampleList = evaluateSampleRepository.findAllEvaluateSample(idSchool, SystemConstant.ID_SYSTEM);
        } else {
            evaluateSampleList = evaluateSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(idSchool);
        }
        if (CollectionUtils.isEmpty(evaluateSampleList)) {
            return null;
        }
        List<EvaluateSampleConfigResponse> evaluateSampleConfigResponseList = listMapper.mapList(evaluateSampleList, EvaluateSampleConfigResponse.class);
        return evaluateSampleConfigResponseList;
    }

    @Override
    public EvaluateSampleConfigResponse createEvaluateSample(Long idSchool, EvaluateSampleCreateRequest evaluateSampleCreateRequest) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActiveTrue(idSchool);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        EvaluateSample evaluateSample = modelMapper.map(evaluateSampleCreateRequest, EvaluateSample.class);
        evaluateSample.setIdSchool(idSchool);
        EvaluateSample evaluateSampleSaved = evaluateSampleRepository.save(evaluateSample);
        EvaluateSampleConfigResponse evaluateSampleConfigResponse = modelMapper.map(evaluateSampleSaved, EvaluateSampleConfigResponse.class);
        return evaluateSampleConfigResponse;
    }

    @Override
    public EvaluateSampleConfigResponse updateEvaluateSample(Long idSchool, EvaluateSampleUpdateRequest evaluateSampleUpdateRequest) {
        Optional<EvaluateSample> evaluateSampleOptional = evaluateSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(evaluateSampleUpdateRequest.getId(), idSchool);
        if (evaluateSampleOptional.isEmpty()) {
            return null;
        }
        EvaluateSample evaluateSample = evaluateSampleOptional.get();
        modelMapper.map(evaluateSampleUpdateRequest, evaluateSample);
        EvaluateSample evaluateSampleSaved = evaluateSampleRepository.save(evaluateSample);
        EvaluateSampleConfigResponse evaluateSampleConfigResponse = modelMapper.map(evaluateSampleSaved, EvaluateSampleConfigResponse.class);
        return evaluateSampleConfigResponse;
    }

    @Override
    public boolean deleteEvaluateSample(Long idSchool, Long id) {
        Optional<EvaluateSample> evaluateSampleOptional = evaluateSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool);
        if (evaluateSampleOptional.isEmpty()) {
            return false;
        }
        EvaluateSample evaluateSample = evaluateSampleOptional.get();
        evaluateSample.setDelActive(AppConstant.APP_FALSE);
        evaluateSampleRepository.save(evaluateSample);
        return true;
    }

    @Override
    public List<EvaluateSampleConfigResponse> findAllEvaluateSampleSystem() {
        List<EvaluateSample> evaluateSampleList = evaluateSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(SystemConstant.ID_SYSTEM);
        if (CollectionUtils.isEmpty(evaluateSampleList)) {
            return null;
        }
        List<EvaluateSampleConfigResponse> evaluateSampleConfigResponseList = listMapper.mapList(evaluateSampleList, EvaluateSampleConfigResponse.class);
        return evaluateSampleConfigResponseList;
    }

    @Override
    public EvaluateSampleConfigResponse createEvaluateSampleSystem(EvaluateSampleCreateRequest evaluateSampleCreateRequest) {
        EvaluateSample evaluateSample = modelMapper.map(evaluateSampleCreateRequest, EvaluateSample.class);
        evaluateSample.setIdSchool(SystemConstant.ID_SYSTEM);
        EvaluateSample evaluateSampleSaved = evaluateSampleRepository.save(evaluateSample);
        EvaluateSampleConfigResponse evaluateSampleConfigResponse = modelMapper.map(evaluateSampleSaved, EvaluateSampleConfigResponse.class);
        return evaluateSampleConfigResponse;
    }

    @Override
    public EvaluateSampleConfigResponse updateEvaluateSampleSystem(EvaluateSampleUpdateRequest evaluateSampleUpdateRequest) {
        Optional<EvaluateSample> evaluateSampleOptional = evaluateSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(evaluateSampleUpdateRequest.getId(), SystemConstant.ID_SYSTEM);
        if (evaluateSampleOptional.isEmpty()) {
            return null;
        }
        EvaluateSample evaluateSample = evaluateSampleOptional.get();
        modelMapper.map(evaluateSampleUpdateRequest, evaluateSample);
        EvaluateSample evaluateSampleSaved = evaluateSampleRepository.save(evaluateSample);
        EvaluateSampleConfigResponse evaluateSampleConfigResponse = modelMapper.map(evaluateSampleSaved, EvaluateSampleConfigResponse.class);
        return evaluateSampleConfigResponse;
    }

    @Override
    public boolean deleteEvaluateSampleSystem(Long id) {
        Optional<EvaluateSample> evaluateSampleOptional = evaluateSampleRepository.findByIdAndIdSchoolAndDelActiveTrue(id, SystemConstant.ID_SYSTEM);
        if (evaluateSampleOptional.isEmpty()) {
            return false;
        }
        EvaluateSample evaluateSample = evaluateSampleOptional.get();
        evaluateSampleRepository.deleteById(id);
        return true;
    }
}
