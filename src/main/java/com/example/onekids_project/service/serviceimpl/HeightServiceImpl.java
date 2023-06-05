package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.sample.HeightSample;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.HeightSampleRepository;
import com.example.onekids_project.repository.KidsHeightRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.request.kidsheightweight.CreateKidsHeightWeightRequest;
import com.example.onekids_project.response.kidsheightweight.HeightSampleResponse;
import com.example.onekids_project.response.kidsheightweight.ListHeightSampleResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.HeightService;
import com.example.onekids_project.util.ConvertData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HeightServiceImpl implements HeightService {

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private HeightSampleRepository heightSampleRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsHeightRepository kidsHeightRepository;


    @Transactional
    @Override
    public boolean createKidsHeight(Long idSchool, UserPrincipal principal, CreateKidsHeightWeightRequest createKidsHeightWeightRequest) {
        if (createKidsHeightWeightRequest.getHeight() == null) {
            return false;
        }
        Optional<Kids> kidsOptional = kidsRepository.findByIdAndDelActive(createKidsHeightWeightRequest.getId(), AppConstant.APP_TRUE);
        if (kidsOptional.isEmpty()) {
            return false;
        }
        Kids kid = kidsOptional.get();
        List<KidsHeight> kidsHeightList = kid.getKidsHeightList().stream().filter(x -> x.getTimeHeight().isEqual(createKidsHeightWeightRequest.getTimeHeight())).collect(Collectors.toList());
        KidsHeight newHeight = new KidsHeight();
        if (CollectionUtils.isEmpty(kidsHeightList)) {
            newHeight.setKids(kid);
        } else {
            newHeight = kidsHeightList.get(0);
            if (createKidsHeightWeightRequest.getHeight() == null) {
                kidsHeightRepository.deletekidsHById(newHeight.getId());
                return true;
            }
        }
        newHeight.setAge(ConvertData.convertDateToMonth(kid.getBirthDay(), createKidsHeightWeightRequest.getTimeHeight()));
        newHeight.setTimeHeight(createKidsHeightWeightRequest.getTimeHeight());
        newHeight.setHeight(createKidsHeightWeightRequest.getHeight());
        newHeight.setAppType(principal.getAppType());
        newHeight.setDelActive(AppConstant.APP_TRUE);
        newHeight.setCreatedBy(principal.getFullName());
        kidsHeightRepository.save(newHeight);
        return true;
    }

    @Override
    public boolean deleteKidsHeight(Long idSchoolLogin, Long id) {
        Optional<KidsHeight> kidsHeightOptional = kidsHeightRepository.findByIdAndDelActive(id, true);
        if (kidsHeightOptional.isEmpty()) {
            return false;
        }
        KidsHeight deleteHeight = kidsHeightOptional.get();
        deleteHeight.setDelActive(AppConstant.APP_FALSE);
        kidsHeightRepository.save(deleteHeight);
        return true;
    }

    @Override
    public ListHeightSampleResponse findHeightSample(Long idSchoolLogin, Pageable pageable, Long id) {
        String type = AppConstant.GIRL;
        List<HeightSample> heightSampleList = heightSampleRepository.findAllByType(type);
        if (CollectionUtils.isEmpty(heightSampleList)) {
            return null;
        }
        List<HeightSampleResponse> heightSampleResponseList = listMapper.mapList(heightSampleList, HeightSampleResponse.class);
        ListHeightSampleResponse listHeightSampleResponse = new ListHeightSampleResponse();
        listHeightSampleResponse.setHeightSampleResponses(heightSampleResponseList);
        return listHeightSampleResponse;
    }

}
