package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.FnKidsPackageDefaultRepository;
import com.example.onekids_project.repository.FnKidsPackageRepository;
import com.example.onekids_project.repository.FnPackageRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.request.finance.*;
import com.example.onekids_project.response.finance.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.FnKidsPackageDefaultService;
import com.example.onekids_project.util.FilterDataUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FnKidsPackageDefaultServiceImpl implements FnKidsPackageDefaultService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnPackageRepository fnPackageRepository;

    @Autowired
    private FnKidsPackageDefaultRepository fnKidsPackageDefaultRepository;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;

    @Override
    public void addPackageForKids(Long idClass, Long idPackage) {
//        List<Kids> kidsList = kidsRepository.findKidsWithStatus(idClass);
        List<Kids> kidsList = kidsRepository.findByMaClassIdAndKidStatusAndDelActiveTrue(idClass, KidsStatusConstant.STUDYING);
        FnPackage fnPackage = fnPackageRepository.findByIdAndDelActiveTrue(idPackage).orElseThrow();
        kidsList.forEach(x -> {
            Optional<FnKidsPackageDefault> fnKidsPackageDefaultOptional = fnKidsPackageDefaultRepository.findByKidsIdAndFnPackageId(x.getId(), idPackage);
            if (fnKidsPackageDefaultOptional.isEmpty()) {
                createPackageDefaultForKids(x, idClass, fnPackage);
            } else {
                FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultOptional.get();
                fnKidsPackageDefault.setIdClass(idClass);
                fnKidsPackageDefaultRepository.save(fnKidsPackageDefault);
            }
        });
    }

    @Override
    public void createPackageDefaultForKids(Kids kids) {
        Long idClass = kids.getMaClass().getId();
        List<FnPackage> fnPackageList = fnPackageRepository.findByMaClassSetIdAndDelActiveTrue(idClass);
        fnPackageList.forEach(x -> createPackageDefaultForKids(kids, idClass, x));
    }

    @Override
    public void deletePackageForKids(Long idClass, Long idPackage) {
        fnKidsPackageDefaultRepository.deletePackageInClass(idClass, idPackage);
    }

    @Transactional
    @Override
    public boolean updateKidsPackageDefaultOne(UserPrincipal principal, KidsPackageDefaultRequest request) {
        this.updateKidsPackage(request);
        return true;
    }

    @Transactional
    @Override
    public boolean updateKidsPackageDefaultMany(UserPrincipal principal, List<KidsPackageDefaultRequest> requestList) {
        requestList.forEach(this::updateKidsPackage);
        return true;
    }

    @Override
    public List<PackageResponse> getPackageForDefaultPackageAdd(UserPrincipal principal, Long idKid) {
        CommonValidate.checkDataPlus(principal);
        List<FnPackage> fnPackageList = fnPackageRepository.getPackageExcludePackageDefaultKid(principal.getIdSchoolLogin(), idKid);
        return listMapper.mapList(fnPackageList, PackageResponse.class);
    }

    @Override
    public boolean createPackageDefault(UserPrincipal principal, Long idKid, PackageDefaultCreateRequest request) {
        CommonValidate.checkDataPlus(principal);
        Optional<FnKidsPackageDefault> fnKidsPackageDefaultOptional = fnKidsPackageDefaultRepository.findByKidsIdAndFnPackageId(idKid, request.getIdPackage());
        if (fnKidsPackageDefaultOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EXIST_PACKAGE_DEFAULT);
        }
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        FnPackage fnPackage = fnPackageRepository.findByIdAndDelActiveTrue(request.getIdPackage()).orElseThrow();
        FnKidsPackageDefault fnKidsPackageDefault = modelMapper.map(request, FnKidsPackageDefault.class);
        fnKidsPackageDefault.setFnPackage(fnPackage);
        fnKidsPackageDefault.setKids(kids);
        Long idClass = kids.getMaClass().getId();
        int checkExist = fnPackageRepository.checkExistsPackageClass(idClass, fnPackage.getId());
        fnKidsPackageDefault.setIdClass(checkExist == 1 ? idClass : AppConstant.NUMBER_ZERO);
        fnKidsPackageDefaultRepository.save(fnKidsPackageDefault);
        return true;
    }

    @Override
    public List<PackageDefaultDetailResponse> getPackageDefaultKid(UserPrincipal principal, Long idKid) {
        CommonValidate.checkDataPlus(principal);
        List<FnKidsPackageDefault> fnKidsPackageDefaultList = fnKidsPackageDefaultRepository.getPackageDefaultKids(idKid);
        List<PackageDefaultDetailResponse> responseList = new ArrayList<>();
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        fnKidsPackageDefaultList.forEach(x -> {
            PackageDefaultDetailResponse model = modelMapper.map(x, PackageDefaultDetailResponse.class);
            Optional<FnKidsPackage> fnKidsPackageOptional = fnKidsPackageRepository.findByKidsIdAndFnPackageIdAndMonthAndYearAndDelActiveTrue(idKid, x.getFnPackage().getId(), month, year);
            model.setExistKidsPackage(fnKidsPackageOptional.isPresent());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public boolean activePackageDefaultKid(UserPrincipal principal, PackageDefaultActiveRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultRepository.findById(request.getId()).orElseThrow();
        fnKidsPackageDefault.setActive(request.isActive());
        fnKidsPackageDefaultRepository.save(fnKidsPackageDefault);
        return true;
    }

    @Override
    public boolean deletePackageDefaultKid(UserPrincipal principal, Long idPackageDefault) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultRepository.findById(idPackageDefault).orElseThrow();
        if (fnKidsPackageDefault.getIdClass() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EXIST_PACKAGE_DEFAULT);
        }
        fnKidsPackageDefaultRepository.deleteById(idPackageDefault);
        return true;
    }

    @Transactional
    @Override
    public void updatePackageDefaultForChangeClass(Kids kids) {
        //xóa toàn bộ khản mặc định
        fnKidsPackageDefaultRepository.deleteByKidsId(kids.getId());
        Long idClass = kids.getMaClass().getId();
//        List<FnKidsPackageDefault> fnKidsPackageDefaultList = fnKidsPackageDefaultRepository.findByKidsId(kids.getId());
        List<FnPackage> fnPackageList = fnPackageRepository.findByMaClassSetIdAndDelActiveTrue(idClass);
//        List<FnKidsPackageDefault> includePackageDefaultClassList = fnKidsPackageDefaultList.stream().filter(x -> fnPackageList.stream().anyMatch(y -> x.getFnPackage().equals(y))).collect(Collectors.toList());
//        List<FnKidsPackageDefault> excludePackageDefaultClassList = fnKidsPackageDefaultList.stream().filter(x -> fnPackageList.stream().noneMatch(y -> x.getFnPackage().equals(y))).collect(Collectors.toList());
//        List<FnPackage> excludePackageList = fnPackageList.stream().filter(x -> fnKidsPackageDefaultList.stream().noneMatch(y -> x.equals(y.getFnPackage()))).collect(Collectors.toList());
//        includePackageDefaultClassList.forEach(x -> x.setIdClass(idClass));
//        excludePackageDefaultClassList.forEach(x -> x.setIdClass(SystemConstant.ID_SYSTEM));
//        fnKidsPackageDefaultRepository.saveAll(includePackageDefaultClassList);
//        fnKidsPackageDefaultRepository.saveAll(excludePackageDefaultClassList);
        fnPackageList.forEach(x -> this.createPackageDefaultForKids(kids, idClass, x));
    }

    @Override
    public List<PackageInClassResponse> searchKidsPackageDefaultInClass(UserPrincipal principal, Long idClass) {
        List<FnKidsPackageDefault> fnPackageList = fnKidsPackageDefaultRepository.findByIdClassAndDelActiveTrue(idClass);
        fnPackageList = fnPackageList.stream().filter(FilterDataUtils.distinctBy(x -> x.getFnPackage().getId())).collect(Collectors.toList());
        List<PackageInClassResponse> responseList = new ArrayList<>();
        fnPackageList.forEach(x -> {
            PackageInClassResponse model = modelMapper.map(x, PackageInClassResponse.class);
            FnPackage fnPackage = x.getFnPackage();
            model.setName(fnPackage.getName());
            model.setCategory(fnPackage.getCategory());
            model.setType(fnPackage.getType());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public ListKidsPackageDefaultResponse searchKidsPackageDefault(UserPrincipal principal, KidsPackageKidsSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListKidsPackageDefaultResponse response = new ListKidsPackageDefaultResponse();
        List<FnPackage> packageInClassList = fnPackageRepository.findByIdSchoolAndMaClassSetIdAndDelActiveTrueOrderByCategory(idSchool, request.getIdClass());
        List<PackageBriefObject> packageList = listMapper.mapList(packageInClassList, PackageBriefObject.class);

        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusName(request.getIdClass(), request.getStatus(), request.getFullName());
        List<KidsPackageDefaultResponse> dataList = listMapper.mapList(kidsList, KidsPackageDefaultResponse.class);
        response.setPackageList(packageList);
        response.setDataList(dataList);
        return response;
    }

    @Override
    public PackageDefaultUpdateResponse getPackageDefaultById(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultRepository.findById(id).orElseThrow();
        return modelMapper.map(fnKidsPackageDefault, PackageDefaultUpdateResponse.class);
    }

    @Override
    public boolean updatePackageDefault(UserPrincipal principal, PackageDefaultUpdateRequest request) {
        FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultRepository.findById(request.getId()).orElseThrow();
        this.checkUpdateDefault(fnKidsPackageDefault.getFnPackage().getType(), request);
        modelMapper.map(request, fnKidsPackageDefault);
        fnKidsPackageDefaultRepository.save(fnKidsPackageDefault);
        return true;
    }

    private void createPackageDefaultForKids(Kids kids, Long idClass, FnPackage fnPackage) {
        FnKidsPackageDefault fnKidsPackageDefault = modelMapper.map(fnPackage, FnKidsPackageDefault.class);
        fnKidsPackageDefault.setId(null);
        fnKidsPackageDefault.setIdClass(idClass);
        fnKidsPackageDefault.setFnPackage(fnPackage);
        fnKidsPackageDefault.setKids(kids);
        fnKidsPackageDefaultRepository.save(fnKidsPackageDefault);
    }

    private void updateKidsPackage(KidsPackageDefaultRequest request) {
        request.getFnKidsPackageDefaultList().forEach(x -> {
            FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultRepository.findById(x.getId()).orElseThrow();
            modelMapper.map(x, fnKidsPackageDefault);
            fnKidsPackageDefaultRepository.save(fnKidsPackageDefault);
        });
    }

    private void checkUpdateDefault(String type, PackageDefaultUpdateRequest request) {
        if (type.equals(FinanceConstant.TYPE_MULTIPLE)) {
            if (
                    !request.isT1() &&
                            !request.isT2() &&
                            !request.isT3() &&
                            !request.isT4() &&
                            !request.isT5() &&
                            !request.isT6() &&
                            !request.isT7() &&
                            !request.isT8() &&
                            !request.isT9() &&
                            !request.isT10() &&
                            !request.isT11() &&
                            !request.isT12()
            ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa chọn tháng nào áp dụng");
            }
        }
    }
}
