package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.entity.sample.WeightSample;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.KidsWeightRepository;
import com.example.onekids_project.repository.WeightSampleRepository;
import com.example.onekids_project.request.kidsheightweight.CreateKidsHeightWeightRequest;
import com.example.onekids_project.response.kidsheightweight.KidsWeightResponse;
import com.example.onekids_project.response.kidsheightweight.ListWeightSampleResponse;
import com.example.onekids_project.response.kidsheightweight.WeightSampleResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WeightService;
import com.example.onekids_project.util.ConvertData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeightServiceImpl implements WeightService {

    @Autowired
    private KidsRepository kidsRepository;


    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KidsWeightRepository kidsWeightRepository;

    @Autowired
    private WeightSampleRepository weightSampleRepository;


    @Transactional
    @Override
    public KidsWeightResponse createKidsWeight(Long idSchool, UserPrincipal principal, CreateKidsHeightWeightRequest createKidsHeightWeightRequest) {
        if (createKidsHeightWeightRequest.getWeight() == null) {
            return null;
        }
        Optional<Kids> kidsOptional = kidsRepository.findByIdAndDelActive(createKidsHeightWeightRequest.getId(), true);
        if (kidsOptional.isEmpty()) {
            return null;
        }
        Kids kid = kidsOptional.get();
        List<KidsWeight> kidsWeightList = kid.getKidsWeightList().stream().filter(x -> x.getTimeWeight().isEqual(createKidsHeightWeightRequest.getTimeWeight())).collect(Collectors.toList());
        KidsWeight newWeight = new KidsWeight();
        KidsWeightResponse kidsWeightResponse = new KidsWeightResponse();
        if (CollectionUtils.isEmpty(kidsWeightList)) {
            newWeight.setKids(kid);
        } else {
            newWeight = kidsWeightList.get(0);
            if (createKidsHeightWeightRequest.getWeight() == null) {
                kidsWeightRepository.deletekidsWById(newWeight.getId());
                return kidsWeightResponse;
            }
        }
        newWeight.setAge(ConvertData.convertDateToMonth(kid.getBirthDay(), createKidsHeightWeightRequest.getTimeWeight()));
        newWeight.setTimeWeight(createKidsHeightWeightRequest.getTimeWeight());
        newWeight.setWeight(createKidsHeightWeightRequest.getWeight());
        newWeight.setAppType(principal.getAppType());
        newWeight.setCreatedBy(principal.getFullName());
        newWeight.setDelActive(AppConstant.APP_TRUE);
        KidsWeight saveWeight = kidsWeightRepository.save(newWeight);
        kidsWeightResponse = modelMapper.map(saveWeight, KidsWeightResponse.class);
        return kidsWeightResponse;
    }

    @Override
    public boolean deleteKidsWeight(Long idSchool, Long id) {
        Optional<KidsWeight> kidsWeightOptional = kidsWeightRepository.findByIdAndDelActive(id, true);
        if (kidsWeightOptional.isEmpty()) {
            return false;
        }
        KidsWeight deleteWeight = kidsWeightOptional.get();
        deleteWeight.setDelActive(false);
        kidsWeightRepository.save(deleteWeight);
        return true;
    }

    @Override
    public ListWeightSampleResponse findWeightSample(Long idSchoolLogin, Pageable pageable, Long id) {


        List<WeightSample> weightSampleList = weightSampleRepository.findAllByType(AppConstant.BOY);
        if (CollectionUtils.isEmpty(weightSampleList)) {
            return null;
        }
//        List<WeightSample> listHeightSampleBoy = weightSampleList.stream().filter(x -> x.getType().equals(AppConstant.GENDER_MALE)).collect(Collectors.toList());
//        List<WeightSample> listHeightSampleGirls = weightSampleList.stream().filter(y -> y.getType().equals(AppConstant.GENDER_FEMALE)).collect(Collectors.toList());
        List<WeightSampleResponse> weightSampleResponseList = listMapper.mapList(weightSampleList, WeightSampleResponse.class);
        ListWeightSampleResponse listWeightSampleResponse = new ListWeightSampleResponse();
        listWeightSampleResponse.setWeightSampleResponses(weightSampleResponseList);
        return listWeightSampleResponse;
    }

}
