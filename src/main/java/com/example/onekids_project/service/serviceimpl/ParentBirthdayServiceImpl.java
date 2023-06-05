package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.dto.ParentDTO;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.sample.WishesSample;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.ParentRepository;
import com.example.onekids_project.repository.WishesSampleRepository;
import com.example.onekids_project.request.base.BaseRequest;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchWishSampleRequest;
import com.example.onekids_project.response.birthdaymanagement.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.ParentBirthdayService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParentBirthdayServiceImpl implements ParentBirthdayService {
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private WishesSampleRepository wishesSampleRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ListMapper listMapper;

    @Override
    public ListParentResponse findAllParentBirthday(Long idSchoolLogin, PageNumberWebRequest request) {
        ListParentResponse listParentResponse = new ListParentResponse();
        List<Parent> parentList = parentRepository.findAllParentBirthday(idSchoolLogin, request);
        if (CollectionUtils.isEmpty(parentList)) {
            return null;
        }
        List<ParentDTO> parentDTOList = listMapper.mapList(parentList, ParentDTO.class);
        listParentResponse.setParentlist(parentDTOList);

        return listParentResponse;
    }


    @Override
    public ListWishesSampleResponse searchBirthdayKid1(UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<WishesSample> wishesSampleList = wishesSampleRepository.searchWishesSampleKid(idSchoolLogin, searchWishSampleRequest);
        if (CollectionUtils.isEmpty(wishesSampleList)) {
            return null;
        }
        wishesSampleList = filterReceiverType(idSchoolLogin, wishesSampleList);
        if (principal.getSchoolConfig().isShowWishSys() == AppConstant.APP_TRUE) {
            List<WishesSample> wishesSampleList2 = wishesSampleList.stream().filter(x -> x.getIdSchool() == 0 || x.getIdSchool().equals(idSchoolLogin)).collect(Collectors.toList());
            List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList2, WishesSampleResponse.class);
            ListWishesSampleResponse listWishesSampleResponse = new ListWishesSampleResponse();
            listWishesSampleResponse.setWishesSampleResponse(wishesSampleResponseList);
            return listWishesSampleResponse;
        } else {
            List<WishesSample> wishesSampleList2 = wishesSampleList.stream().filter(y -> y.getIdSchool().equals(idSchoolLogin)).collect(Collectors.toList());
            List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList2, WishesSampleResponse.class);
            ListWishesSampleResponse listWishesSampleResponse = new ListWishesSampleResponse();
            listWishesSampleResponse.setWishesSampleResponse(wishesSampleResponseList);
            return listWishesSampleResponse;
        }

    }

    @Override
    public ListWishesSampleResponse searchBirthdayParent(UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest) {
        long idSchoolLogin = principal.getIdSchoolLogin();
        List<WishesSample> wishesSampleList = wishesSampleRepository.searchWishesSampleKid(idSchoolLogin, searchWishSampleRequest);
        if (CollectionUtils.isEmpty(wishesSampleList)) {
            return null;
        }
        wishesSampleList = filterReceiverTypeParent(idSchoolLogin, wishesSampleList);
        if (principal.getSchoolConfig().isShowWishSys() == AppConstant.APP_TRUE) {
            List<WishesSample> wishesSampleList2 = wishesSampleList.stream().filter(x -> x.getIdSchool() == 0 || x.getIdSchool() == idSchoolLogin).collect(Collectors.toList());
            List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList2, WishesSampleResponse.class);
            ListWishesSampleResponse listWishesSampleResponse = new ListWishesSampleResponse();
            listWishesSampleResponse.setWishesSampleResponse(wishesSampleResponseList);
            return listWishesSampleResponse;
        } else {
            List<WishesSample> wishesSampleList2 = wishesSampleList.stream().filter(y -> y.getIdSchool() == idSchoolLogin).collect(Collectors.toList());
            List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList2, WishesSampleResponse.class);
            ListWishesSampleResponse listWishesSampleResponse = new ListWishesSampleResponse();
            listWishesSampleResponse.setWishesSampleResponse(wishesSampleResponseList);
            return listWishesSampleResponse;
        }
    }

    @Override
    public ListWishesSampleResponse searchBirthdayTeacher(UserPrincipal principal, SearchWishSampleRequest searchWishSampleRequest) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<WishesSample> wishesSampleList = wishesSampleRepository.searchWishesSampleKid(idSchoolLogin, searchWishSampleRequest);
        if (CollectionUtils.isEmpty(wishesSampleList)) {
            return null;
        }
        wishesSampleList = filterReceiverTypeTeacher(idSchoolLogin, wishesSampleList);
        if (principal.getSchoolConfig().isShowWishSys() == AppConstant.APP_TRUE) {
            List<WishesSample> wishesSampleList2 = wishesSampleList.stream().filter(x -> x.getIdSchool() == 0 || x.getIdSchool().equals(idSchoolLogin)).collect(Collectors.toList());
            List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList2, WishesSampleResponse.class);
            ListWishesSampleResponse listWishesSampleResponse = new ListWishesSampleResponse();
            listWishesSampleResponse.setWishesSampleResponse(wishesSampleResponseList);
            return listWishesSampleResponse;
        } else {
            List<WishesSample> wishesSampleList2 = wishesSampleList.stream().filter(y -> y.getIdSchool().equals(idSchoolLogin)).collect(Collectors.toList());
            List<WishesSampleResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList2, WishesSampleResponse.class);
            ListWishesSampleResponse listWishesSampleResponse = new ListWishesSampleResponse();
            listWishesSampleResponse.setWishesSampleResponse(wishesSampleResponseList);
            return listWishesSampleResponse;
        }
    }


    @Override
    public List<ParentsBirthdayResponse> searchParentBirthDayNew(UserPrincipal principal, SearchParentBirthDayRequest searchParentBirthDayRequest) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.searchParentBirthdayNew(idSchool, searchParentBirthDayRequest);
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<ParentsBirthdayResponse> parentsBirthdayResponseList = new ArrayList<>();
        kidsList.forEach(x -> {
            ParentsBirthdayResponse model = new ParentsBirthdayResponse();
            model.setFullName(ConvertData.getInforRepresent(x).getFullName());
            model.setBirthday(ConvertData.getInforRepresent(x).getBirthday());
            model.setPhone(ConvertData.getInforRepresent(x).getPhone());
            model.setId(x.getParent().getMaUser().getId());
            model.setNameKid(x.getFullName() + " - " + x.getMaClass().getClassName());
            if (x.getRepresentation().equals(AppConstant.FATHER)) {
                model.setGender(AppConstant.FATHER);
            } else {
                model.setGender(AppConstant.MOTHER);
            }
            parentsBirthdayResponseList.add(model);
        });
        return parentsBirthdayResponseList;
    }

    @Override
    public ListParentBirthDayResponse searchParentBirthDayNewa(UserPrincipal principal, SearchParentBirthDayRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        ListParentBirthDayResponse response = new ListParentBirthDayResponse();
        List<Kids> kidsList = kidsRepository.searchParentBirthdayNewa(idSchool, request);
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<ParentsBirthdayResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            ParentsBirthdayResponse model = new ParentsBirthdayResponse();
            model.setFullName(ConvertData.getInforRepresent(x).getFullName());
            model.setBirthday(ConvertData.getInforRepresent(x).getBirthday());
            model.setPhone(ConvertData.getInforRepresent(x).getPhone());
            model.setId(x.getParent().getId());
            model.setNameKid(x.getFullName() + " - " + x.getMaClass().getClassName());
            if (x.getRepresentation().equals(AppConstant.FATHER)) {
                model.setGender(AppConstant.FATHER);
            } else {
                model.setGender(AppConstant.MOTHER);
            }
            responseList.add(model);
        });
        long total = kidsRepository.countsearchParentBirthdayNewa(idSchool, request);
        response.setTotal(total);
        response.setResponseList(responseList);
        return response;
    }


    private List<WishesSample> filterReceiverType(Long idSchoolLogin, List<WishesSample> wishesSampleList) {
        return wishesSampleList.stream().filter(x -> SampleConstant.KIDS.equals(x.getReceiverType())).collect(Collectors.toList());
    }

    private List<WishesSample> filterReceiverTypeParent(Long idSchoolLogin, List<WishesSample> wishesSampleList) {
        return wishesSampleList.stream().filter(x -> SampleConstant.PARENT.equals(x.getReceiverType())).collect(Collectors.toList());
    }

    private List<WishesSample> filterReceiverTypeTeacher(Long idSchoolLogin, List<WishesSample> wishesSampleList) {
        return wishesSampleList.stream().filter(x -> SampleConstant.TEACHER.equals(x.getReceiverType())).collect(Collectors.toList());
    }
}
