package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.entity.sample.HeightSample;
import com.example.onekids_project.entity.sample.WeightSample;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.request.kids.KidsHeightWeightParentRequest;
import com.example.onekids_project.mobile.parent.response.kids.HeightWeightSampleParentResponse;
import com.example.onekids_project.mobile.parent.response.kids.KidsHeightWeightParentResponse;
import com.example.onekids_project.mobile.parent.response.kids.ListHeightWeightSampleParentResponse;
import com.example.onekids_project.mobile.parent.response.kids.ListKidsHeightWeightParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.KidsHeightWeightParentService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KidsHeightWeightParentServiceImpl implements KidsHeightWeightParentService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsHeightRepository kidsHeightRepository;

    @Autowired
    private KidsWeightRepository kidsWeightRepository;

    @Autowired
    private HeightSampleRepository heightSampleRepository;

    @Autowired
    private WeightSampleRepository weightSampleRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public ListHeightWeightSampleParentResponse findHeightWeightSampleParent(Long idKid) {
        ListHeightWeightSampleParentResponse dataList = new ListHeightWeightSampleParentResponse();
        Kids kids = kidsRepository.findByIdAndDelActive(idKid, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found kids by id"));
        String type = kids.getGender().equals("Nam") ? "boy" : "girl";
        List<HeightSample> heightSampleList = heightSampleRepository.findByType(type);
        List<HeightWeightSampleParentResponse> heightList = listMapper.mapList(heightSampleList, HeightWeightSampleParentResponse.class);
        List<WeightSample> weightSampleList = weightSampleRepository.findByType(type);
        List<HeightWeightSampleParentResponse> wieghtList = listMapper.mapList(weightSampleList, HeightWeightSampleParentResponse.class);
        dataList.setHeightSampleList(heightList);
        dataList.setWeightSampleList(wieghtList);
        return dataList;
    }

    @Transactional
    @Override
    public KidsHeightWeightParentResponse saveHeightWeightParent(UserPrincipal principal, KidsHeightWeightParentRequest kidsHeightWeightParentRequest) {
        String fullName=principal.getFullName();
        Long idKid = principal.getIdKidLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found Kids by id in parent"));
        //save height
        KidsHeightWeightParentResponse model = new KidsHeightWeightParentResponse();
        if (kidsHeightWeightParentRequest.getHeight() != null) {
            Optional<KidsHeight> kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.PARENT, kidsHeightWeightParentRequest.getDate());
            KidsHeight kidsHeight;
            if (kidsHeightOptional.isPresent()) {
                kidsHeight = kidsHeightOptional.get();
            } else {
                kidsHeight = new KidsHeight();
                kidsHeight.setKids(kids);
                kidsHeight.setAppType(AppTypeConstant.PARENT);
            }
            kidsHeight.setTimeHeight(kidsHeightWeightParentRequest.getDate());
            kidsHeight.setHeight(kidsHeightWeightParentRequest.getHeight());
            kidsHeight.setAge(ConvertData.convertDateToMonth(kids.getBirthDay(), kidsHeightWeightParentRequest.getDate()));
            kidsHeight.setCreatedBy(fullName);
            kidsHeightRepository.save(kidsHeight);
        }
        //save weight
        if (kidsHeightWeightParentRequest.getWeight() != null) {
            Optional<KidsWeight> kidsWeightOptional = kidsWeightRepository.findByKidsIdAndAppTypeAndTimeWeight(idKid, AppTypeConstant.PARENT, kidsHeightWeightParentRequest.getDate());
            KidsWeight kidsWeight;
            if (kidsWeightOptional.isPresent()) {
                kidsWeight = kidsWeightOptional.get();
            } else {
                kidsWeight = new KidsWeight();
                kidsWeight.setKids(kids);
                kidsWeight.setAppType(AppTypeConstant.PARENT);
            }
            kidsWeight.setTimeWeight(kidsHeightWeightParentRequest.getDate());
            kidsWeight.setWeight(kidsHeightWeightParentRequest.getWeight());
            kidsWeight.setAge(ConvertData.convertDateToMonth(kids.getBirthDay(), kidsHeightWeightParentRequest.getDate()));
            kidsWeight.setCreatedBy(fullName);
            kidsWeightRepository.save(kidsWeight);
        }
        Optional<KidsHeight> kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.PARENT, kidsHeightWeightParentRequest.getDate());
        Optional<KidsWeight> kidsWeightOptional = kidsWeightRepository.findByKidsIdAndAppTypeAndTimeWeight(idKid, AppTypeConstant.PARENT, kidsHeightWeightParentRequest.getDate());
        if (kidsHeightOptional.isPresent()) {
            KidsHeight kidsHeight = kidsHeightOptional.get();
            model.setIdHeight(kidsHeight.getId());
            model.setDelete(AppConstant.APP_TRUE);
            model.setDate(kidsHeight.getTimeHeight());
            model.setAge(ConvertData.convertAgeToYears(kidsHeight.getAge()));
            model.setHeight(kidsHeight.getHeight());
        }
        if (kidsWeightOptional.isPresent()) {
            KidsWeight kidsWeight = kidsWeightOptional.get();
            model.setIdWeight(kidsWeight.getId());
            model.setDelete(AppConstant.APP_TRUE);
            model.setDate(kidsWeight.getTimeWeight());
            model.setAge(ConvertData.convertAgeToYears(kidsWeight.getAge()));
            model.setWeight(kidsWeight.getWeight());
        }
        return model;
    }

    @Override
    public ListKidsHeightWeightParentResponse findHeightWeight(UserPrincipal principal, LocalDate localDate, Pageable pageable) {
        Long idKid = principal.getIdKidLogin();
        ListKidsHeightWeightParentResponse model = new ListKidsHeightWeightParentResponse();
        List<KidsHeightWeightParentResponse> dataList = new ArrayList<>();
        List<KidsHeight> kidsHeightList = kidsHeightRepository.findKidsHeight(idKid, localDate, pageable);
        List<KidsWeight> kidsWeightList = kidsWeightRepository.findKidsWeight(idKid, localDate, pageable);
        List<LocalDate> heightDateList = kidsHeightList.stream().map(KidsHeight::getTimeHeight).collect(Collectors.toList());
        List<LocalDate> weightDateList = kidsWeightList.stream().map(KidsWeight::getTimeWeight).collect(Collectors.toList());
        List<LocalDate> unionDateList = List.copyOf(Set.copyOf(CollectionUtils.union(heightDateList, weightDateList)));
        unionDateList = unionDateList.stream().sorted(LocalDate::compareTo).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(unionDateList)) {
            return null;
        }
        if (unionDateList.size() <= 10) {
            model.setLastPage(AppConstant.APP_TRUE);
        } else {
            unionDateList = unionDateList.subList(unionDateList.size() - 10, unionDateList.size());
        }
        for (int i = unionDateList.size() - 1; i >= 0; i--) {
            KidsHeightWeightParentResponse modelParent = new KidsHeightWeightParentResponse();
            KidsHeightWeightParentResponse modelPlus = new KidsHeightWeightParentResponse();
            List<LocalDate> finalUnionDateList = unionDateList;
            int finalI = i;
            List<KidsHeight> heightList = kidsHeightList.stream().filter(x -> x.getTimeHeight().isEqual(finalUnionDateList.get(finalI))).collect(Collectors.toList());
            List<KidsWeight> weightList = kidsWeightList.stream().filter(x -> x.getTimeWeight().isEqual(finalUnionDateList.get(finalI))).collect(Collectors.toList());
            //set height
            heightList.forEach(a -> {
                if (a.getAppType().equals(AppTypeConstant.PARENT)) {
                    modelParent.setDate(finalUnionDateList.get(finalI));
                    modelParent.setIdHeight(a.getId());
                    modelParent.setAge(ConvertData.convertAgeToYears(a.getAge()));
                    modelParent.setHeight(a.getHeight());
                    modelParent.setDelete(AppConstant.APP_TRUE);
                } else {
                    modelPlus.setDate(finalUnionDateList.get(finalI));
                    modelPlus.setIdHeight(a.getId());
                    modelPlus.setAge(ConvertData.convertAgeToYears(a.getAge()));
                    modelPlus.setHeight(a.getHeight());
                }
            });
            //set weight
            weightList.forEach(b -> {
                if (b.getAppType().equals(AppTypeConstant.PARENT)) {
                    modelParent.setDate(finalUnionDateList.get(finalI));
                    modelParent.setIdWeight(b.getId());
                    modelParent.setAge(ConvertData.convertAgeToYears(b.getAge()));
                    modelParent.setWeight(b.getWeight());
                    modelParent.setDelete(AppConstant.APP_TRUE);
                } else {
                    modelPlus.setDate(finalUnionDateList.get(finalI));
                    modelPlus.setIdWeight(b.getId());
                    modelPlus.setAge(ConvertData.convertAgeToYears(b.getAge()));
                    modelPlus.setWeight(b.getWeight());
                }
            });
            if (modelParent.getIdHeight() != null || modelParent.getIdWeight() != null) {
                dataList.add(modelParent);
            }
            if (modelPlus.getIdHeight() != null || modelPlus.getIdWeight() != null) {
                dataList.add(modelPlus);
            }
        }
        if (CollectionUtils.isNotEmpty(dataList) && dataList.size() > 20) {
            dataList = dataList.subList(0, 20);
        }
        model.setDataList(dataList);
        return model;
    }

    @Transactional
    @Override
    public boolean deleteHeightWeight(Long idHeight, Long idWeight) {
        if (idHeight != null) {
            KidsHeight kidsHeight = kidsHeightRepository.findByIdAndAppType(idHeight, AppTypeConstant.PARENT).orElseThrow(() -> new NotFoundException("not found kidsHeight by id"));
            kidsHeightRepository.deleteById(kidsHeight.getId());
        }
        if (idWeight != null) {
            KidsWeight kidsWeight = kidsWeightRepository.findByIdAndAppType(idWeight, AppTypeConstant.PARENT).orElseThrow(() -> new NotFoundException("not found kidsWeight by id"));
            kidsWeightRepository.deleteById(kidsWeight.getId());
        }
        return true;
    }
}
