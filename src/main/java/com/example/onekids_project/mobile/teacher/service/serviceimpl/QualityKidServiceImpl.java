package com.example.onekids_project.mobile.teacher.service.serviceimpl;

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
import com.example.onekids_project.mobile.parent.response.kids.HeightWeightSampleParentResponse;
import com.example.onekids_project.mobile.parent.response.kids.ListHeightWeightSampleParentResponse;
import com.example.onekids_project.mobile.teacher.request.qualitykid.KidsHeightWeightTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.qualitykid.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.QualityKidService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class QualityKidServiceImpl implements QualityKidService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private KidsHeightRepository kidsHeightRepository;

    @Autowired
    private KidsWeightRepository kidsWeightRepository;

    @Autowired
    private HeightSampleRepository heightSampleRepository;

    @Autowired
    private WeightSampleRepository weightSampleRepository;

    @Override
    public ListQualityKidTeacherResponse findQualityKidOfClass(UserPrincipal principal) {
        CommonValidate.checkDataTeacher(principal);
        ListQualityKidTeacherResponse model = new ListQualityKidTeacherResponse();
        List<QualityKidTeacherResponse> qualityKidTeacherResponseList = new ArrayList<>();
        List<Kids> dataList = kidsRepository.finQualityKidsOfClass(principal);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataList.forEach(data -> {
            QualityKidTeacherResponse qualityKidTeacherResponse = new QualityKidTeacherResponse();
            qualityKidTeacherResponse.setId(data.getId());
            qualityKidTeacherResponse.setNameKid(data.getFullName());
            if (data.getAvatarKid() != null) {
                qualityKidTeacherResponse.setUrl(data.getAvatarKid());
            } else qualityKidTeacherResponse.setUrl(AvatarDefaultConstant.AVATAR_KIDS);

            List<KidsHeight> kidsHeightList = kidsHeightRepository.findKidsHeightOfClass(data.getId());
            List<KidsWeight> kidsWeightList = kidsWeightRepository.findKidsWeightfClass(data.getId());
            if (kidsHeightList.size() > 0) {
                qualityKidTeacherResponse.setHeight(kidsHeightList.get(0).getHeight());
                qualityKidTeacherResponse.setDate(df.format(kidsHeightList.get(0).getTimeHeight()));
            }
            if (kidsWeightList.size() > 0) {
                qualityKidTeacherResponse.setWeight(kidsWeightList.get(0).getWeight());
                qualityKidTeacherResponse.setDate(df.format(kidsWeightList.get(0).getTimeWeight()));
            }
            qualityKidTeacherResponseList.add(qualityKidTeacherResponse);
        });
        model.setDataList(qualityKidTeacherResponseList);
        return model;
    }

    @Override
    public KidsExtraQualityResponse findKidsExtraQuality(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        Optional<Kids> kids = kidsRepository.findByIdKid(principal.getIdSchoolLogin(), id);
        KidsExtraQualityResponse model = new KidsExtraQualityResponse();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.setProperties(kids, model);

//        KidsHeightWeightTeacherResponse kidsHeightWeightTeacherResponse = new KidsHeightWeightTeacherResponse();
        List<KidsHeightWeightTeacherResponse> dataList = new ArrayList<>();
        List<KidsHeight> kidsHeightList = kidsHeightRepository.findKidsHeightOfClass(id);
        List<KidsWeight> kidsWeightList = kidsWeightRepository.findKidsWeightfClass(id);
        if (kidsHeightList.size() > 0 || kidsWeightList.size() >0) {
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
                    if (a.getAppType().equals(AppTypeConstant.TEACHER)) {
                        modelTeacher.setDate(df.format(finalUnionDateList.get(finalI)));
                        modelTeacher.setIdHeight(a.getId());
                        modelTeacher.setAge(ConvertData.convertAgeToYears(a.getAge()));
                        modelTeacher.setHeight(a.getHeight());
                        modelTeacher.setDelete(AppConstant.APP_TRUE);
                    } else {
                        modelPlus.setDate(df.format(finalUnionDateList.get(finalI)));
                        modelPlus.setIdHeight(a.getId());
                        modelPlus.setAge(ConvertData.convertAgeToYears(a.getAge()));
                        modelPlus.setHeight(a.getHeight());
                    }
                });
                //set weight
                weightList.forEach(b -> {
                    if (b.getAppType().equals(AppTypeConstant.TEACHER)) {
                        modelTeacher.setDate(df.format(finalUnionDateList.get(finalI)));
                        modelTeacher.setIdWeight(b.getId());
                        modelTeacher.setAge(ConvertData.convertAgeToYears(b.getAge()));
                        modelTeacher.setWeight(b.getWeight());
                        modelTeacher.setDelete(AppConstant.APP_TRUE);
                    } else {
                        modelPlus.setDate(df.format(finalUnionDateList.get(finalI)));
                        modelPlus.setIdWeight(b.getId());
                        modelPlus.setAge(ConvertData.convertAgeToYears(b.getAge()));
                        modelPlus.setWeight(b.getWeight());
                    }
                });
                if (modelTeacher.getIdHeight() != null || modelTeacher.getIdWeight() != null) {
                    dataList.add(modelTeacher);
                }
                if (modelPlus.getIdHeight() != null || modelPlus.getIdWeight() != null) {
                    dataList.add(modelPlus);
                }
            }
            model.setDataList(dataList);
        }else model.setDataList(dataList);

        return model;
    }

    @Override
    public boolean deleteHeightWeight(Long idHeight, Long idWeight) {
        if (idHeight != null) {
            KidsHeight kidsHeight = kidsHeightRepository.findByIdAndAppType(idHeight, AppTypeConstant.TEACHER).orElseThrow(() -> new NotFoundException("not found kidsHeight by id"));
            kidsHeightRepository.deleteById(kidsHeight.getId());
        }
        if (idWeight != null) {
            KidsWeight kidsWeight = kidsWeightRepository.findByIdAndAppType(idWeight, AppTypeConstant.TEACHER).orElseThrow(() -> new NotFoundException("not found kidsWeight by id"));
            kidsWeightRepository.deleteById(kidsWeight.getId());
        }
        return true;
    }

    @Override
    public KidsHeightWeightTeacherResponse saveHeightWeightTeacher(UserPrincipal principal, KidsHeightWeightTeacherRequest kidsHeightWeightTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idKid = kidsHeightWeightTeacherRequest.getIdKid();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow(() -> new NotFoundException("not found Kids by id in teacher"));
        //save height
        KidsHeightWeightTeacherResponse model = new KidsHeightWeightTeacherResponse();
        if (kidsHeightWeightTeacherRequest.getHeight() != null) {
            Optional<KidsHeight> kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.TEACHER , kidsHeightWeightTeacherRequest.getDate());
            if (kidsHeightOptional.isEmpty()){
                kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.SCHOOL , kidsHeightWeightTeacherRequest.getDate());
            }

            KidsHeight kidsHeight;
            if (kidsHeightOptional.isPresent()) {
                kidsHeight = kidsHeightOptional.get();
            } else {
                kidsHeight = new KidsHeight();
                kidsHeight.setKids(kids);
                kidsHeight.setAppType(AppTypeConstant.TEACHER);
            }
            kidsHeight.setCreatedBy(principal.getFullName());
            kidsHeight.setTimeHeight(kidsHeightWeightTeacherRequest.getDate());
            kidsHeight.setHeight(kidsHeightWeightTeacherRequest.getHeight());
            kidsHeight.setAge(ConvertData.convertDateToMonth(kids.getBirthDay(), kidsHeightWeightTeacherRequest.getDate()));
            kidsHeightRepository.save(kidsHeight);
        }
        //save weight
        if (kidsHeightWeightTeacherRequest.getWeight() != null) {
            Optional<KidsWeight> kidsWeightOptional = kidsWeightRepository.findByKidsIdAndAppTypeAndTimeWeight(idKid, AppTypeConstant.TEACHER, kidsHeightWeightTeacherRequest.getDate());
            KidsWeight kidsWeight;
            if (kidsWeightOptional.isPresent()) {
                kidsWeight = kidsWeightOptional.get();
            } else {
                kidsWeight = new KidsWeight();
                kidsWeight.setKids(kids);
                kidsWeight.setAppType(AppTypeConstant.TEACHER);
            }
            kidsWeight.setCreatedBy(principal.getFullName());
            kidsWeight.setTimeWeight(kidsHeightWeightTeacherRequest.getDate());
            kidsWeight.setWeight(kidsHeightWeightTeacherRequest.getWeight());
            kidsWeight.setAge(ConvertData.convertDateToMonth(kids.getBirthDay(), kidsHeightWeightTeacherRequest.getDate()));
            kidsWeightRepository.save(kidsWeight);
        }
        Optional<KidsHeight> kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.TEACHER, kidsHeightWeightTeacherRequest.getDate());
        if (kidsHeightOptional.isEmpty()){
            kidsHeightOptional = kidsHeightRepository.findByKidsIdAndAppTypeAndTimeHeight(idKid, AppTypeConstant.SCHOOL , kidsHeightWeightTeacherRequest.getDate());
        }
        Optional<KidsWeight> kidsWeightOptional = kidsWeightRepository.findByKidsIdAndAppTypeAndTimeWeight(idKid, AppTypeConstant.TEACHER, kidsHeightWeightTeacherRequest.getDate());
        if (kidsWeightOptional.isEmpty()){
            kidsWeightOptional = kidsWeightRepository.findByKidsIdAndAppTypeAndTimeWeight(idKid, AppTypeConstant.SCHOOL, kidsHeightWeightTeacherRequest.getDate());
        }
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
    public ListHeightWeightSampleTeacherResponse findKidSample(Long id) {
        ListHeightWeightSampleTeacherResponse dataList = new ListHeightWeightSampleTeacherResponse();
        Kids kids = kidsRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found kids by id"));
        String type = kids.getGender().equals("Nam") ? "boy" : "girl";
        List<HeightSample> heightSampleList = heightSampleRepository.findByType(type);
        List<HeightWeightSampleTeacherResponse> heightList = listMapper.mapList(heightSampleList, HeightWeightSampleTeacherResponse.class);
        List<WeightSample> weightSampleList = weightSampleRepository.findByType(type);
        List<HeightWeightSampleTeacherResponse> wieghtList = listMapper.mapList(weightSampleList, HeightWeightSampleTeacherResponse.class);
        dataList.setHeightSampleList(heightList);
        dataList.setWeightSampleList(wieghtList);
        return dataList;
    }

    /**
     * set properties
     *
     * @param
     * @param model
     */
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
