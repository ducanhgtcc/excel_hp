package com.example.onekids_project.mobile.service.serviceimpl;

import com.example.onekids_project.common.EvaluateConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.sample.EvaluateSample;
import com.example.onekids_project.mobile.response.EvaluateSampleMobileResponse;
import com.example.onekids_project.mobile.service.servicecustom.EvaluateMobileService;
import com.example.onekids_project.repository.EvaluateSampleRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluateMobileServiceImpl implements EvaluateMobileService {

    @Autowired
    private EvaluateSampleRepository evaluateSampleRepository;

    @Override
    public EvaluateSampleMobileResponse getEvaluateSample(UserPrincipal principal) {
        EvaluateSampleMobileResponse model = new EvaluateSampleMobileResponse();
        Long idSchool = principal.getIdSchoolLogin();
        List<EvaluateSample> evaluateSampleList;
        if (principal.getSchoolConfig().isShowEvaluateSys()) {
            evaluateSampleList = evaluateSampleRepository.findAllEvaluateSample(idSchool, SystemConstant.ID_SYSTEM);
        } else {
            evaluateSampleList = evaluateSampleRepository.findByIdSchoolAndDelActiveTrueOrderByIdDesc(idSchool);
        }
        List<EvaluateSample> learnList = evaluateSampleList.stream().filter(x -> x.getEvaluateType().equals(EvaluateConstant.LEARN)).collect(Collectors.toList());
        List<EvaluateSample> eatList = evaluateSampleList.stream().filter(x -> x.getEvaluateType().equals(EvaluateConstant.EAT)).collect(Collectors.toList());
        List<EvaluateSample> sleepList = evaluateSampleList.stream().filter(x -> x.getEvaluateType().equals(EvaluateConstant.SLEEP)).collect(Collectors.toList());
        List<EvaluateSample> sanitaryList = evaluateSampleList.stream().filter(x -> x.getEvaluateType().equals(EvaluateConstant.SANITARY)).collect(Collectors.toList());
        List<EvaluateSample> heatlList = evaluateSampleList.stream().filter(x -> x.getEvaluateType().equals(EvaluateConstant.HEALT)).collect(Collectors.toList());
        List<EvaluateSample> commonList = evaluateSampleList.stream().filter(x -> x.getEvaluateType().equals(EvaluateConstant.COMMON)).collect(Collectors.toList());
        model.setLearnList(learnList.stream().map(x -> x.getEvaluateContent()).collect(Collectors.toList()));
        model.setEatList(eatList.stream().map(x -> x.getEvaluateContent()).collect(Collectors.toList()));
        model.setSleepList(sleepList.stream().map(x -> x.getEvaluateContent()).collect(Collectors.toList()));
        model.setSanitaryList(sanitaryList.stream().map(x -> x.getEvaluateContent()).collect(Collectors.toList()));
        model.setHealtList(heatlList.stream().map(x -> x.getEvaluateContent()).collect(Collectors.toList()));
        model.setCommonList(commonList.stream().map(x -> x.getEvaluateContent()).collect(Collectors.toList()));
        return model;
    }
}
