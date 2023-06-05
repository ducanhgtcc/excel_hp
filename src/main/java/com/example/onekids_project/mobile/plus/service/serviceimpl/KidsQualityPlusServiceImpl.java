package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsExtraInfo;
import com.example.onekids_project.entity.kids.KidsHeight;
import com.example.onekids_project.entity.kids.KidsWeight;
import com.example.onekids_project.entity.sample.HeightSample;
import com.example.onekids_project.entity.sample.WeightSample;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.kidsQuality.CreateKidsHeightWeightPlusRequest;
import com.example.onekids_project.mobile.plus.request.kidsQuality.SearchKidsQualityPlusRequest;
import com.example.onekids_project.mobile.plus.response.kidsQuality.KidsHeightWeightPlusResponse;
import com.example.onekids_project.mobile.plus.response.kidsQuality.KidsQualityPlusResponse;
import com.example.onekids_project.mobile.plus.response.kidsQuality.ListKidsQualityPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsQualityPlusService;
import com.example.onekids_project.mobile.teacher.response.qualitykid.HeightWeightSampleTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsExtraQualityResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.KidsHeightWeightTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.qualitykid.ListHeightWeightSampleTeacherResponse;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KidsQualityPlusServiceImpl implements KidsQualityPlusService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private HeightSampleRepository heightSampleRepository;
    @Autowired
    private WeightSampleRepository weightSampleRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private KidsHeightRepository kidsHeightRepository;
    @Autowired
    private KidsWeightRepository kidsWeightRepository;

    @Override
    public ListKidsQualityPlusResponse findQualityKid(UserPrincipal principal, SearchKidsQualityPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListKidsQualityPlusResponse model = new ListKidsQualityPlusResponse();
        List<KidsQualityPlusResponse> responseList = new ArrayList<>();
        List<Kids> dataList = kidsRepository.finQualityKidsforPlus(idSchool, request);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataList.forEach(x -> {
            KidsQualityPlusResponse model1 = new KidsQualityPlusResponse();
            model1.setId(x.getId());
            model1.setFullName(x.getFullName());
            model1.setUrl(x.getAvatarKid() != null ? x.getAvatarKid() : AvatarDefaultConstant.AVATAR_KIDS);
            List<KidsHeight> kidsHeightList = kidsHeightRepository.findKidsHeightOfClass(x.getId());
            List<KidsWeight> kidsWeightList = kidsWeightRepository.findKidsWeightfClass(x.getId());
            if (kidsHeightList.size() > 0) {
                model1.setHeight(kidsHeightList.get(0).getHeight());
                model1.setDate(df.format(kidsHeightList.get(0).getTimeHeight()));
            } else {
                model1.setHeight(0);
                model1.setDate("0");
            }
            if (kidsWeightList.size() > 0) {
                model1.setWeight(kidsWeightList.get(0).getWeight());
                model1.setDate(df.format(kidsWeightList.get(0).getTimeWeight()));
            } else {
                model1.setWeight(0);
                model1.setDate("0");
            }
            responseList.add(model1);
        });
        model.setDataList(responseList);
        return model;
    }

    @Override
    public KidsExtraQualityResponse findKidsExtraQuality(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        Optional<Kids> kids = kidsRepository.findByIdKid(principal.getIdSchoolLogin(), id);
        KidsExtraQualityResponse model = new KidsExtraQualityResponse();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.setProperties(kids, model);
        List<KidsHeightWeightTeacherResponse> dataList = new ArrayList<>();
        List<KidsHeight> kidsHeightList = kidsHeightRepository.findKidsHeightOfClass(id);
        List<KidsWeight> kidsWeightList = kidsWeightRepository.findKidsWeightfClass(id);
        if (kidsHeightList.size() > 0 || kidsWeightList.size() > 0) {
            List<LocalDate> heightDateList = kidsHeightList.stream().map(KidsHeight::getTimeHeight).collect(Collectors.toList());
            List<LocalDate> weightDateList = kidsWeightList.stream().map(KidsWeight::getTimeWeight).collect(Collectors.toList());
            List<LocalDate> unionDateList = List.copyOf(Set.copyOf(CollectionUtils.union(heightDateList, weightDateList)));
            unionDateList = unionDateList.stream().sorted(LocalDate::compareTo).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(unionDateList)) {
                return null;
            }
            for (int i = unionDateList.size() - 1; i >= 0; i--) {
                KidsHeightWeightTeacherResponse modelTeacher = new KidsHeightWeightTeacherResponse();
                KidsHeightWeightTeacherResponse modelPlus = new KidsHeightWeightTeacherResponse();
                List<LocalDate> finalUnionDateList = unionDateList;
                int finalI = i;
                List<KidsHeight> heightList = kidsHeightList.stream().filter(x -> x.getTimeHeight().isEqual(finalUnionDateList.get(finalI))).collect(Collectors.toList());
                List<KidsWeight> weightList = kidsWeightList.stream().filter(x -> x.getTimeWeight().isEqual(finalUnionDateList.get(finalI))).collect(Collectors.toList());
                //set height
                heightList.forEach(a -> {
                    modelTeacher.setDate(df.format(finalUnionDateList.get(finalI)));
                    modelTeacher.setIdHeight(a.getId());
                    modelTeacher.setAge(ConvertData.convertAgeToYears(a.getAge()));
                    modelTeacher.setHeight(a.getHeight());
                    modelTeacher.setDelete(AppConstant.APP_TRUE);
                    modelPlus.setDelete(a.getAppType().equals(AppTypeConstant.PARENT) ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
                });
                //set weight
                weightList.forEach(b -> {
                    modelTeacher.setDate(df.format(finalUnionDateList.get(finalI)));
                    modelTeacher.setIdWeight(b.getId());
                    modelTeacher.setAge(ConvertData.convertAgeToYears(b.getAge()));
                    modelTeacher.setWeight(b.getWeight());
                    modelPlus.setDelete(b.getAppType().equals(AppTypeConstant.PARENT) ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
                });
                if (modelTeacher.getIdHeight() != null || modelTeacher.getIdWeight() != null) {
                    dataList.add(modelTeacher);
                }
                if (modelPlus.getIdHeight() != null || modelPlus.getIdWeight() != null) {
                    dataList.add(modelPlus);
                }
            }
            model.setDataList(dataList);
        } else model.setDataList(dataList);
        return model;
    }

    @Override
    public boolean deleteHeightWeight(Long idHeight, Long idWeight) {
        if (idHeight != null) {
            Long idHeightDelete = kidsHeightRepository.findIdKidHeightPlus(idHeight);
            kidsHeightRepository.deleteById(idHeightDelete);
        }
        if (idWeight != null) {
            Long idWeightDelete = kidsWeightRepository.findIdKidWeightPlus(idWeight);
            kidsWeightRepository.deleteById(idWeightDelete);
        }
        return true;
    }

    @Override
    public KidsHeightWeightPlusResponse saveHeightWeightPlus(UserPrincipal principal, CreateKidsHeightWeightPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idKid = request.getIdKid();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        //save height
        KidsHeightWeightPlusResponse model = new KidsHeightWeightPlusResponse();
        if (request.getHeight() != null) {
            Optional<KidsHeight> kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.SCHOOL, request.getDate());
            KidsHeight kidsHeight;
            if (kidsHeightOptional.isPresent()) {
                kidsHeight = kidsHeightOptional.get();
            } else {
                kidsHeight = new KidsHeight();
                kidsHeight.setKids(kids);
                kidsHeight.setAppType(AppTypeConstant.SCHOOL);
            }
            kidsHeight.setCreatedBy(principal.getFullName());
            kidsHeight.setTimeHeight(request.getDate());
            kidsHeight.setHeight(request.getHeight());
            kidsHeight.setAge(ConvertData.convertDateToMonth(kids.getBirthDay(), request.getDate()));
            kidsHeightRepository.save(kidsHeight);
        }
        //save weight
        if (request.getWeight() != null) {
            Optional<KidsWeight> kidsWeightOptional = kidsWeightRepository.findByKidsIdAndAppTypeAndTimeWeight(idKid, AppTypeConstant.SCHOOL, request.getDate());
            KidsWeight kidsWeight;
            if (kidsWeightOptional.isPresent()) {
                kidsWeight = kidsWeightOptional.get();
            } else {
                kidsWeight = new KidsWeight();
                kidsWeight.setKids(kids);
                kidsWeight.setAppType(AppTypeConstant.SCHOOL);
            }
            kidsWeight.setCreatedBy(principal.getFullName());
            kidsWeight.setTimeWeight(request.getDate());
            kidsWeight.setWeight(request.getWeight());
            kidsWeight.setAge(ConvertData.convertDateToMonth(kids.getBirthDay(), request.getDate()));
            kidsWeightRepository.save(kidsWeight);
        }
        Optional<KidsHeight> kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.SCHOOL, request.getDate());

        Optional<KidsWeight> kidsWeightOptional = kidsWeightRepository.findByKidsIdAndAppTypeAndTimeWeight(idKid, AppTypeConstant.SCHOOL, request.getDate());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (kidsHeightOptional.isPresent()) {
            KidsHeight kidsHeight = kidsHeightOptional.get();
            model.setIdHeight(kidsHeight.getId());
            model.setDelete(AppConstant.APP_TRUE);
            model.setDate(df.format(kidsHeight.getTimeHeight()));
            model.setAge(ConvertData.convertAgeToYears(kidsHeight.getAge()));
            model.setHeight(kidsHeight.getHeight());
        }
        if (kidsWeightOptional.isPresent()) {
            KidsWeight kidsWeight = kidsWeightOptional.get();
            model.setIdWeight(kidsWeight.getId());
            model.setDelete(AppConstant.APP_TRUE);
            model.setDate(df.format(kidsWeight.getTimeWeight()));
            model.setAge(ConvertData.convertAgeToYears(kidsWeight.getAge()));
            model.setWeight(kidsWeight.getWeight());
        }
        return model;
    }

    @Override
    public ListHeightWeightSampleTeacherResponse findKidSamplePlus(Long id) {
        ListHeightWeightSampleTeacherResponse response = new ListHeightWeightSampleTeacherResponse();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        String type = kids.getGender().equals(AppConstant.MALE) ? AppConstant.BOY : AppConstant.GIRL;
        List<HeightSample> heightSampleList = heightSampleRepository.findByType(type);
        List<HeightWeightSampleTeacherResponse> heightList = listMapper.mapList(heightSampleList, HeightWeightSampleTeacherResponse.class);
        List<WeightSample> weightSampleList = weightSampleRepository.findByType(type);
        List<HeightWeightSampleTeacherResponse> wieghtList = listMapper.mapList(weightSampleList, HeightWeightSampleTeacherResponse.class);
        response.setHeightSampleList(heightList);
        response.setWeightSampleList(wieghtList);
        return response;
    }

    private void setProperties(Optional<Kids> kidsOptional, KidsExtraQualityResponse model) {
        Kids kids = kidsOptional.get();
        KidsExtraInfo extraInfo = kids.getKidsExtraInfo();
        model.setAvatar(ConvertData.getAvatarKid(kids));
        model.setName(StringUtils.isNotBlank(kids.getFullName()) ? kids.getFullName() : "");
        model.setNameClass(StringUtils.isNotBlank(kids.getMaClass().getClassName()) ? kids.getMaClass().getClassName() : "");
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
