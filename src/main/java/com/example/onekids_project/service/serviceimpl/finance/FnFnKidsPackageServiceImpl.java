package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.enums.DateEnum;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.finance.*;
import com.example.onekids_project.request.finance.approved.KidsPackageInKidsSearchDetailRequest;
import com.example.onekids_project.request.finance.kidspackage.SaveNumberManyRequest;
import com.example.onekids_project.request.finance.kidspackage.TransferNumberManyRequest;
import com.example.onekids_project.request.finance.kidspackage.TransferNumberOneRequest;
import com.example.onekids_project.request.finance.kidspackage.UserNumberRequest;
import com.example.onekids_project.response.attendancekids.AttendanceKidsStatisticalResponse;
import com.example.onekids_project.response.finance.*;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsStatisticalService;
import com.example.onekids_project.service.servicecustom.cashbook.CashBookHistoryService;
import com.example.onekids_project.service.servicecustom.finance.FnKidsPackageService;
import com.example.onekids_project.service.servicecustom.finance.FnPackageService;
import com.example.onekids_project.service.servicecustom.finance.extend.FnPackageKidsExtendService;
import com.example.onekids_project.util.AttendanceKidsUtil;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.PrincipalUtils;
import com.example.onekids_project.util.SchoolUtils;
import com.example.onekids_project.util.objectdata.FnMonthObject;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FnFnKidsPackageServiceImpl implements FnKidsPackageService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;

    @Autowired
    private FnPackageRepository fnPackageRepository;

    @Autowired
    private FnKidsPackageDefaultRepository fnKidsPackageDefaultRepository;

    @Autowired
    private AttendanceKidsStatisticalService attendanceKidsStatisticalService;

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private FnPackageKidsExtendService fnPackageKidsExtendService;
    @Autowired
    private FnPackageKidsExtendRepository fnPackageKidsExtendRepository;

    @Autowired
    private FnPackageService fnPackageService;
    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;
    @Autowired
    private CashBookHistoryService cashBookHistoryService;

    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;


    @Override
    public ListKidsPackageInClassResponse searchKidsPackageInClass(UserPrincipal principal, KidsPackageInClassSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListKidsPackageInClassResponse response = new ListKidsPackageInClassResponse();
        List<FnPackage> packageInClassList = fnPackageRepository.findByIdSchoolAndMaClassSetIdAndDelActiveTrueOrderByCategory(idSchool, request.getIdClass());
        List<PackageBriefObject> packageList = listMapper.mapList(packageInClassList, PackageBriefObject.class);

        LocalDate date = request.getDate();
        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusName(request.getIdClass(), request.getStatus(), request.getFullName());
        List<KidsCustom1> kidsCustom1List = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsCustom1 model = modelMapper.map(x, KidsCustom1.class);
            List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(x, date, AppConstant.APP_FALSE, AppConstant.APP_FALSE);
            long count = fnKidsPackageList.stream().filter(a -> packageList.stream().noneMatch(b -> a.getFnPackage().getId().equals(b.getId()))).count();
            model.setNumber((int) count);
            model.setFnKidsPackageList(listMapper.mapList(fnKidsPackageList, KidsPackageCustom1.class));
            kidsCustom1List.add(model);
        });
        response.setPackageList(packageList);
        response.setDataList(kidsCustom1List);
        return response;
    }

    @Transactional
    @Override
    public int generateKidsPackageOne(UserPrincipal principal, Long idKid, LocalDate date) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        int year = date.getYear();
        int month = date.getMonthValue();
        return this.generateForKidsPackage(kids, principal.getSchoolConfig(), month, year, principal.getId(), FinanceConstant.GENERATE_MANUAL);
    }

    @Transactional
    @Override
    public boolean generateKidsPackageMany(UserPrincipal principal, GenerateKidsPackageRequest request) {
        List<Long> idKidList = request.getIdKidList().stream().map(IdObjectRequest::getId).collect(Collectors.toList());
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndIdInAndDelActiveTrue(principal.getIdSchoolLogin(), idKidList);
        LocalDate date = request.getDate();
        int month = date.getMonthValue();
        int year = date.getYear();
        Long idUser = principal.getId();
        SchoolConfigResponse schoolConfig = principal.getSchoolConfig();
        kidsList.forEach(x -> this.generateForKidsPackage(x, schoolConfig, month, year, idUser, FinanceConstant.GENERATE_MANUAL));
        return true;
    }

    @Override
    public void generateKidsPackageAuto(School school, Kids kids, LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        SchoolConfigResponse schoolConfigResponse = modelMapper.map(school.getSchoolConfig(), SchoolConfigResponse.class);
        this.generateForKidsPackage(kids, schoolConfigResponse, month, year, 1L, FinanceConstant.GENERATE_AUTO);
    }

    private void setCreateProperties(FnKidsPackage fnKidsPackage, SchoolConfigResponse schoolConfig) {
        fnKidsPackage.setShowParent(schoolConfig.isAutoShowFeesFutureKids());
        fnKidsPackage.setShowTeacher(schoolConfig.isAutoShowFeesFutureKidsForTeacher());
    }

    @Override
    public boolean activeKidsPackageOne(UserPrincipal principal, KidsPackageActiveRequest request) {
        this.activeKidsPackage(request);
        return true;
    }

    @Override
    public boolean activeKidsPackageMany(UserPrincipal principal, List<KidsPackageActiveRequest> requestList) {
        requestList.forEach(this::activeKidsPackage);
        return true;
    }

    @Override
    public List<Package2Response> getPackageForKidsPackageAdd(UserPrincipal principal, Long idKid, LocalDate date) {
        CommonValidate.checkDataPlus(principal);
        List<FnPackage> fnPackageList = fnPackageRepository.getPackageExcludePackageDefaultAndKidsPackage(principal.getIdSchoolLogin(), idKid, date.getMonthValue(), date.getYear());
        return listMapper.mapList(fnPackageList, Package2Response.class);
    }

    @Override
    public List<PackageInClassResponse> getPackageForKidsPackageAddMany(UserPrincipal principal, Long idClass) {
        List<PackageInClassResponse> responseList = fnPackageService.getPackageInClass(principal, idClass);
        return responseList.stream().filter(x -> !x.isUsed()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public int createPackageForKidsPackageAddMany(UserPrincipal principal, AddPackageForKidsRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate date = request.getDate();
        int month = date.getMonthValue();
        int year = date.getYear();
        SchoolConfigResponse schoolConfig = principal.getSchoolConfig();
        List<Kids> kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(request.getIdKidList(), idSchool);
        AtomicInteger count = new AtomicInteger();
        kidsList.forEach(x -> {
            List<FnPackage> fnPackageList = fnPackageRepository.getPackageExcludePackageDefaultAndKidsPackageForAdd(request.getIdPackageList(), idSchool, x.getId(), month, year);
            List<FnKidsPackage> fnKidsPackageList = new ArrayList<>();
            fnPackageList.forEach(a -> {
                FnKidsPackage model = modelMapper.map(a, FnKidsPackage.class);
                model.setId(null);
                model.setMonth(month);
                model.setYear(year);
                model.setFnPackage(a);
                model.setKids(x);
                this.setCreateProperties(model, schoolConfig);
                fnKidsPackageList.add(model);
            });
            List<FnKidsPackage> fnKidsPackageSaveList = fnKidsPackageRepository.saveAll(fnKidsPackageList);
            fnPackageKidsExtendService.saveKidsExtend(fnKidsPackageSaveList, FinanceConstant.GENERATE_PART);
            count.addAndGet(fnKidsPackageSaveList.size());
        });
        return count.get();
    }

    @Transactional
    @Override
    public boolean createKidsPackageFromSchool(UserPrincipal principal, Long idKid, KidsPackageCreateRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
        int month = date.getMonthValue();
        int year = date.getYear();
        Optional<FnKidsPackage> fnKidsPackageOptional = fnKidsPackageRepository.findByKidsIdAndFnPackageIdAndMonthAndYearAndDelActiveTrue(idKid, request.getIdPackage(), month, year);
        if (fnKidsPackageOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EXIST_PACKAGE_KIDS);
        }
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        FnPackage fnPackage = fnPackageRepository.findByIdAndDelActiveTrue(request.getIdPackage()).orElseThrow();
        FnKidsPackage fnKidsPackage = modelMapper.map(request, FnKidsPackage.class);
        SchoolConfigResponse schoolConfig = principal.getSchoolConfig();

        fnKidsPackage.setMonth(month);
        fnKidsPackage.setYear(year);
        this.setCreateProperties(fnKidsPackage, schoolConfig);
        fnKidsPackage.setFnPackage(fnPackage);
        fnKidsPackage.setKids(kids);
        FnKidsPackage fnKidsPackageSaved = fnKidsPackageRepository.save(fnKidsPackage);
        fnPackageKidsExtendService.saveKidsExtend(Collections.singletonList(fnKidsPackageSaved), FinanceConstant.GENERATE_PART);
        return true;
    }

    @Override
    public FnKidsPackage createKidsPackageRootFromPackage(Kids kids, FnPackage fnPackage, int month, int year, double price, double paid) {
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        CommonValidate.checkDataPlus(principal);
        Optional<FnKidsPackage> fnKidsPackageOptional = fnKidsPackageRepository.findByKidsIdAndFnPackageIdAndMonthAndYearAndDelActiveTrue(kids.getId(), fnPackage.getId(), month, year);
        FnKidsPackage fnKidsPackage;
        if (fnKidsPackageOptional.isEmpty()) {
            fnKidsPackage = modelMapper.map(fnPackage, FnKidsPackage.class);
            fnKidsPackage.setId(null);
            fnKidsPackage.setMonth(month);
            fnKidsPackage.setYear(year);
            SchoolConfigResponse schoolConfig = principal.getSchoolConfig();
            this.setCreateProperties(fnKidsPackage, schoolConfig);
            fnKidsPackage.setFnPackage(fnPackage);
            fnKidsPackage.setKids(kids);
            fnKidsPackage.setRootStatus(true);
            fnKidsPackage.setApproved(true);
            fnKidsPackage.setIdApproved(principal.getId());
            fnKidsPackage.setTimeApproved(LocalDateTime.now());
            fnKidsPackage.setUsedNumber(1);
            fnKidsPackage.setFnPackage(fnPackage);
        } else {
            fnKidsPackage = fnKidsPackageOptional.get();
        }
        fnKidsPackage.setPrice(price + fnKidsPackage.getPrice());
        fnKidsPackage.setPaid(paid + fnKidsPackage.getPaid());
        if (fnKidsPackage.getPaid() > 0) {
            fnKidsPackage.setLocked(true);
            fnKidsPackage.setIdLocked(principal.getId());
            fnKidsPackage.setTimeLocked(LocalDateTime.now());
        }
        return fnKidsPackageRepository.save(fnKidsPackage);
    }

    @Override
    public List<KidsPackageDetailResponse> getKidsPackageByIdKid(UserPrincipal principal, Long idKid, SearchKidsPackageDetailRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
        List<FnPackage> packageInClassList = fnPackageRepository.findByIdSchoolAndMaClassSetIdAndDelActiveTrueOrderBySortNumber(principal.getIdSchoolLogin(), request.getIdClass());
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.getKidsPackageForKid(principal.getIdSchoolLogin(), idKid, date.getMonthValue(), date.getYear());
        List<KidsPackageDetailResponse> responseList = listMapper.mapList(fnKidsPackageList, KidsPackageDetailResponse.class);
        List<KidsPackageDetailResponse> responseFilterList = responseList.stream().filter(x -> packageInClassList.stream().anyMatch(y -> y.getId().equals(x.getFnPackage().getId()))).collect(Collectors.toList());
        responseFilterList.forEach(x -> x.setDefaultStatus(AppConstant.APP_TRUE));
        return responseList;
    }

    @Override
    public KidsPackageUpdateResponse getKidsPackageById(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findById(id).orElseThrow();
        return modelMapper.map(fnKidsPackage, KidsPackageUpdateResponse.class);
    }

    @Override
    public boolean updateKidsPackage(UserPrincipal principal, KidsPackageUpdateRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findById(request.getId()).orElseThrow();
        this.checkBeforeUpdateKidsPackage(fnKidsPackage);
        modelMapper.map(request, fnKidsPackage);
        fnKidsPackageRepository.save(fnKidsPackage);
        return true;
    }

    @Transactional
    @Override
    public boolean deletePackageKidById(UserPrincipal principal, Long idKidsPackage) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findById(idKidsPackage).orElseThrow();
        this.checkMultiple(fnKidsPackage);
        fnKidsPackage.setDelActive(AppConstant.APP_FALSE);
        fnKidsPackageRepository.save(fnKidsPackage);
        //xóa khoản đính kèm của khoản này
        if (fnKidsPackage.getFnPackageKidsExtend() != null) {
            fnPackageKidsExtendRepository.deleteById(fnKidsPackage.getFnPackageKidsExtend().getId());
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deletePackageKidsMany(UserPrincipal principal, List<Long> idList) {
        CommonValidate.checkDataPlus(principal);
        idList.forEach(x -> this.deletePackageKidById(principal, x));
        return true;
    }

    @Override
    public int deletePackageKidsManyKids(List<Long> idKidList, List<Long> idPackageList, LocalDate date) {
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByYearAndMonthAndKidsIdInAndFnPackageIdInAndApprovedFalseAndLockedFalseAndDelActiveTrue(date.getYear(), date.getMonthValue(), idKidList, idPackageList);
        fnKidsPackageList = fnKidsPackageList.stream().filter(a -> a.getPaid() == 0).collect(Collectors.toList());
        fnKidsPackageList.forEach(a -> a.setDelActive(AppConstant.APP_FALSE));
        fnKidsPackageRepository.saveAll(fnKidsPackageList);
        return fnKidsPackageList.size();
    }

    @Override
    public boolean addFromPackageDefault(UserPrincipal principal, Long idKid, Long idDefaultPackage) {
        CommonValidate.checkDataPlus(principal);
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultRepository.findById(idDefaultPackage).orElseThrow();
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();
        Optional<FnKidsPackage> fnKidsPackageOptional = fnKidsPackageRepository.findByKidsIdAndFnPackageIdAndMonthAndYearAndDelActiveTrue(idKid, fnKidsPackageDefault.getFnPackage().getId(), month, year);
        if (fnKidsPackageOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EXIST_PACKAGE);
        }
        boolean checkExist = this.checkBeforeAddFromPackageDefault(fnKidsPackageDefault, month, year);
        if (!checkExist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EXPIRED_PACKAGE);
        }
        this.createKidsPackage(kids, fnKidsPackageDefault, principal.getSchoolConfig(), month, year, principal.getId(), FinanceConstant.GENERATE_MANUAL);
        return true;
    }

    @Override
    @Transactional
    public int addFromPackageDefaultMany(UserPrincipal principal, List<Long> idKidList, List<Long> idPackageDefaultList) {
        CommonValidate.checkDataPlus(principal);
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();
        AtomicInteger count = new AtomicInteger();
        idKidList.forEach(idKid -> {
            idPackageDefaultList.forEach(idDefaultPackage -> {
                Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
                FnKidsPackageDefault fnKidsPackageDefault = fnKidsPackageDefaultRepository.findById(idDefaultPackage).orElseThrow();
                Optional<FnKidsPackage> fnKidsPackageOptional = fnKidsPackageRepository.findByKidsIdAndFnPackageIdAndMonthAndYearAndDelActiveTrue(idKid, fnKidsPackageDefault.getFnPackage().getId(), month, year);
                if (fnKidsPackageOptional.isEmpty()) {
                    boolean checkExist = this.checkBeforeAddFromPackageDefault(fnKidsPackageDefault, month, year);
                    if (checkExist) {
                        count.getAndIncrement();
                        this.createKidsPackage(kids, fnKidsPackageDefault, principal.getSchoolConfig(), month, year, principal.getId(), FinanceConstant.GENERATE_MANUAL);
                    }
                }
            });
        });
        return count.getAndIncrement();
    }

    @Override
    public boolean activeKidsPackageOne(UserPrincipal principal, PackageDefaultActiveRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        this.checkBeforeUpdateKidsPackage(fnKidsPackage);
        fnKidsPackage.setActive(request.isActive());
        fnKidsPackageRepository.save(fnKidsPackage);
        return true;
    }

    @Override
    public List<KidsForKidsPackageResponse> searchKidsPackageForKids(UserPrincipal principal, KidsPackageInKidsSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusName(request.getIdClass(), request.getStatus(), request.getFullName());
        List<KidsForKidsPackageResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsForKidsPackageResponse model = modelMapper.map(x, KidsForKidsPackageResponse.class);
            List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(x, date, AppConstant.APP_FALSE, AppConstant.APP_FALSE);
            List<KidsPackageCustom2> kidsPackageCustom2List = listMapper.mapList(fnKidsPackageList, KidsPackageCustom2.class);
            model.setFnKidsPackageList(kidsPackageCustom2List);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<KidsPackageForApprovedResponse> searchKidsPackageForApproved(UserPrincipal principal, KidsPackageInKidsSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusName(request.getIdClass(), request.getStatus(), request.getFullName(), request.getIdKidList());
        return this.searchKidsPackageForApprovedByKids(kidsList, date);
    }

    @Override
    public List<KidsPackageForApprovedResponse> searchKidsPackageForApprovedDetail(UserPrincipal principal, KidsPackageInKidsSearchDetailRequest request) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        return this.searchKidsPackageForApprovedByKids(Collections.singletonList(kids), request.getDate());
    }

    private List<KidsPackageForApprovedResponse> searchKidsPackageForApprovedByKids(List<Kids> kidsList, LocalDate date) {
        List<KidsPackageForApprovedResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsPackageForApprovedResponse model = modelMapper.map(x, KidsPackageForApprovedResponse.class);
            List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(x, date, AppConstant.APP_TRUE, AppConstant.APP_FALSE);
            List<KidsPackageCustom3> kidsPackageCustom3List = new ArrayList<>();
            fnKidsPackageList.forEach(y -> {
                KidsPackageCustom3 kidsPackageCustom3 = modelMapper.map(y, KidsPackageCustom3.class);
                kidsPackageCustom3.setMoney(FinanceUltils.getMoneyCalculate(y));
                kidsPackageCustom3.setMoneyExtend(FinanceUltils.getMoneyExtendFinal(y));
                int calculateNumber = this.getCalculateNumber(x, y);
                kidsPackageCustom3.setCalculateNumber(calculateNumber);
                kidsPackageCustom3.setShowNumber(y.getUsedNumber());
                kidsPackageCustom3.setMoneyTemp(FinanceUltils.getMoneyWidthNumber(y, calculateNumber));
                kidsPackageCustom3List.add(kidsPackageCustom3);
            });
            List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
            List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
            List<FnKidsPackage> inApprovedList = inList.stream().filter(FnKidsPackage::isApproved).collect(Collectors.toList());
            List<FnKidsPackage> outApprovedList = outList.stream().filter(FnKidsPackage::isApproved).collect(Collectors.toList());
            model.setApprovedNumber(this.getApprovedNumber(fnKidsPackageList));
            model.setLockedNumber(this.getLockedNumber(fnKidsPackageList));
            model.setTotalMoneyIn(inList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum());
            model.setTotalMoneyOut(outList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum());
            //còn lại phải thu các khoản đã duyệt
            double inRemainApproved = inApprovedList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum() - inApprovedList.stream().mapToDouble(FnKidsPackage::getPaid).sum();
            //còn lại phải chi các khoản đã duyệt
            double outRemainApproved = outApprovedList.stream().mapToDouble(FinanceUltils::getMoneyCalculate).sum() - outApprovedList.stream().mapToDouble(FnKidsPackage::getPaid).sum();
            double moneyRemain = inRemainApproved - outRemainApproved;
            if (moneyRemain > 0) {
                model.setTotalMoneyRemainIn(moneyRemain);
            } else {
                model.setTotalMoneyRemainOut(Math.abs(moneyRemain));
            }
            model.setFnKidsPackageList(kidsPackageCustom3List);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public int getCalculateNumber(Kids kids, FnKidsPackage fnKidsPackage) {
        int number = fnKidsPackage.getNumber();
        FnPackage fnPackage = fnKidsPackage.getFnPackage();
        if (fnPackage.isAttendance()) {
            Long idSchool = SchoolUtils.getIdSchool();
            String attendanceType = fnPackage.getAttendanceType();
            String attendanceDetail = fnPackage.getAttendanceDetail();
            int month = fnKidsPackage.getMonth();
            int year = fnKidsPackage.getYear();

            List<LocalDate> dateList = FinanceUltils.getRangeDateFees(month, year);
            LocalDate startDate = dateList.get(0);
            LocalDate endDate = dateList.get(1);

            //trả trước
            if (fnPackage.getAttendancePaid().equals(FinanceConstant.ATTENDANCE_PAID_BEFORE)) {
                MaClass maClass = kids.getMaClass();
                switch (attendanceType) {
                    //điểm danh ăn kiểu mới
                    case FinanceConstant.EAT_TYPE_NEW:
                        switch (attendanceDetail) {
                            //thu tiền cho tháng hiện tại
                            case FinanceConstant.EAT_BREAKFAST_1:
                            case FinanceConstant.EAT_REMAIN_1:
                            case FinanceConstant.EAT_DAY_2:
                                number = FinanceUltils.getAttendanceNumberLearnMonth(maClass, startDate, endDate, null);
                                break;
                            //trả lại tiền cho tháng trước
                            case FinanceConstant.EAT_BREAKFAST_OUT_1:
                                number = FinanceUltils.getAttendanceNumberNot(kids, startDate, endDate, FinanceConstant.EAT_BREAKFAST_OUT_1);
                                break;
                            case FinanceConstant.EAT_REMAIN_OUT_1:
                                number = FinanceUltils.getAttendanceNumberNot(kids, startDate, endDate, FinanceConstant.EAT_REMAIN_OUT_1);
                                break;
                            case FinanceConstant.EAT_DAY_OUT_2:
                                number = FinanceUltils.getAttendanceNumberNot(kids, startDate, endDate, FinanceConstant.EAT_DAY_OUT_2);
                                break;
                            case FinanceConstant.EAT_BREAKFAST_OUT_2:
                                number = FinanceUltils.getAttendanceNumberNot(kids, startDate, endDate, FinanceConstant.EAT_BREAKFAST_OUT_2);
                                break;
                            case FinanceConstant.EAT_REPAY_DINNER26:
                                number = FinanceUltils.getAttendanceNumberNot(kids, startDate, endDate, FinanceConstant.EAT_REPAY_DINNER26);
                                break;
                            default:
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE);
                        }
                        break;
                    case FinanceConstant.ARRIVE_TYPE_NEW:
                        switch (attendanceDetail) {
                            case FinanceConstant.ARRIVE_GO_SCHOOL:
                                number = FinanceUltils.getAttendanceNumberLearnMonth(maClass, startDate, endDate, null);
                                break;
                            case FinanceConstant.ARRIVE_GO_SCHOOL27:
                                List<LocalDate> sundayList = FinanceUltils.getSundayList(startDate, endDate);
                                number = FinanceUltils.getAttendanceNumberLearn27(maClass, startDate, endDate, sundayList);
                                break;
                            case FinanceConstant.ARRIVE_GO_SCHOOL26:
                                List<LocalDate> saturdaySundayList = FinanceUltils.getSaturdaySundayList(startDate, endDate);
                                number = FinanceUltils.getAttendanceNumberLearnMonth(maClass, startDate, endDate, saturdaySundayList);
                                break;
                            case FinanceConstant.ARRIVE_GO_SCHOOL7:
                                List<LocalDate> allIgnoreSaturdayList = FinanceUltils.getAllIgnoreSaturdayList(startDate, endDate);
                                number = FinanceUltils.getAttendanceNumberLearnMonth(maClass, startDate, endDate, allIgnoreSaturdayList);
                                break;
                            case FinanceConstant.ARRIVE_ABSENT_YES:
                            case FinanceConstant.ARRIVE_ABSENT_NO:
                            case FinanceConstant.ARRIVE_ABSENT_YES_NO27:
                            case FinanceConstant.ARRIVE_ABSENT_YES26:
                            case FinanceConstant.ARRIVE_ABSENT_YES_NO26:
                            case FinanceConstant.ARRIVE_ABSENT_YES7:
                            case FinanceConstant.ARRIVE_ABSENT_YES_NO7:
                                number = FinanceUltils.getAttendanceNumberNot(kids, startDate, endDate, attendanceDetail);
                                break;
                        }
                        break;
                }
            } else if (fnPackage.getAttendancePaid().equals(FinanceConstant.ATTENDANCE_PAID_AFTER)) {
                //trả sau
//                LocalDate startDate = LocalDate.of(fnKidsPackage.getYear(), fnKidsPackage.getMonth(), 1);
//                LocalDate endDate = startDate.plusMonths(1).minusDays(1);
                AttendanceKidsStatisticalResponse statistical = attendanceKidsStatisticalService.attendanceKidsStatistical(kids, startDate, endDate);
                switch (attendanceType) {
                    //đi học
                    case FinanceConstant.ATTENDANCE_GO_SCHOOL:
                        switch (attendanceDetail) {
                            case FinanceConstant.DAY_MORNING:
                                number = statistical.getMorning();
                                break;
                            case FinanceConstant.DAY_AFTERNOON:
                                number = statistical.getAfternoon();
                                break;
                            case FinanceConstant.DAY_EVENING:
                                number = statistical.getEvening();
                                break;
                            case FinanceConstant.ALL_DAY:
                                number = statistical.getAllDay();
                                break;
                            case FinanceConstant.ONELY_SARTUDAY:
                                List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThanEqual(kids.getId(), startDate, endDate);
                                attendanceKidsList = attendanceKidsList.stream().filter(x -> x.getAttendanceDate().getDayOfWeek().toString().equals(DateEnum.SATURDAY.name())).collect(Collectors.toList());
                                attendanceKidsList = attendanceKidsList.stream().filter(x -> AttendanceKidsUtil.checkArrive(x.getAttendanceArriveKids())).collect(Collectors.toList());
                                number = attendanceKidsList.size();
                                break;
                            case FinanceConstant.ONELY_SARTUDAY_BEFORE:
                                List<LocalDate> newDateList = FinanceUltils.getDateSameMonth(startDate, endDate);
                                LocalDate startDateBefore = newDateList.get(0);
                                LocalDate endDateBefore = newDateList.get(1);
                                List<AttendanceKids> attendanceKidsBeforeList = attendanceKidsRepository.findByKidsIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThanEqual(kids.getId(), startDateBefore, endDateBefore);
                                attendanceKidsBeforeList = attendanceKidsBeforeList.stream().filter(x -> x.getAttendanceDate().getDayOfWeek().toString().equals(DateEnum.SATURDAY.name())).collect(Collectors.toList());
                                attendanceKidsBeforeList = attendanceKidsBeforeList.stream().filter(x -> AttendanceKidsUtil.checkArrive(x.getAttendanceArriveKids())).collect(Collectors.toList());
                                number = attendanceKidsBeforeList.size();
                                break;
                            default:
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE);
                        }
                        break;
                    //nghỉ có phép
                    case FinanceConstant.ATTENDANCE_ABSENT_YES:
                        switch (attendanceDetail) {
                            case FinanceConstant.DAY_MORNING:
                                number = statistical.getMorningYes();
                                break;
                            case FinanceConstant.DAY_AFTERNOON:
                                number = statistical.getAfternoonYes();
                                break;
                            case FinanceConstant.DAY_EVENING:
                                number = statistical.getEveningYes();
                                break;
                            case FinanceConstant.ALL_DAY:
                                number = statistical.getAllDayYes();
                                break;
                            default:
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE);
                        }
                        break;
                    //nghỉ không phép
                    case FinanceConstant.ATTENDANCE_ABSENT_NO:
                        switch (attendanceDetail) {
                            case FinanceConstant.DAY_MORNING:
                                number = statistical.getMorningNo();
                                break;
                            case FinanceConstant.DAY_AFTERNOON:
                                number = statistical.getAfternoonNo();
                                break;
                            case FinanceConstant.DAY_EVENING:
                                number = statistical.getEveningNo();
                                break;
                            case FinanceConstant.ALL_DAY:
                                number = statistical.getAllDayNo();
                                break;
                            default:
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE);
                        }
                        break;
                    //điểm danh ăn
                    case FinanceConstant.ATTENDANCE_EAT:
                        switch (attendanceDetail) {
                            case FinanceConstant.EAT_BREAKFAST:
                                number = statistical.getEatMorning();
                                break;
                            case FinanceConstant.EAT_BREAKFAST_SECOND:
                                number = statistical.getEatMorningSecond();
                                break;
                            case FinanceConstant.EAT_LUNCH:
                                number = statistical.getEatNoon();
                                break;
                            case FinanceConstant.EAT_AFTERNOON:
                                number = statistical.getEatAfternoon();
                                break;
                            case FinanceConstant.EAT_AFTERNOON_SECOND:
                                number = statistical.getEatAfternoonSecond();
                                break;
                            case FinanceConstant.EAT_DINNER:
                                number = statistical.getEatEvening();
                                break;
                            case FinanceConstant.ALL_DAY:
                                number = statistical.getEatAllDay();
                                break;
                            default:
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ATTENDANCE_TYPE);
                        }
                        break;
                    //điểm danh đón muộn
                    case FinanceConstant.ATTENDANCE_PICKUP_LATE:
                        number = statistical.getMinutesLate();
                        break;
                }
            }
        }
        return number;
    }

    @Override
    public boolean approvedKidsPackage(UserPrincipal principal, ApprovedRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        this.checkMoneyZero(fnKidsPackage);
        this.checkLocked(fnKidsPackage);
        this.checkPayment(fnKidsPackage);
        this.saveApproved(fnKidsPackage, request.isApproved(), principal.getId(), LocalDateTime.now());
        return true;
    }

    @Transactional
    @Override
    public void approvedKidsPackageAdvance(UserPrincipal principal, Long id) {
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndApprovedTrueAndDelActiveTrue(id).orElseThrow();
        this.transferMoneyToCashbook(fnKidsPackage, FinanceConstant.CASH_BOOK_ORIGIN_UNAPPROVED);
        fnKidsPackage.setApproved(false);
        fnKidsPackage.setLocked(false);
        fnKidsPackage.setPaid(0);
        fnKidsPackageRepository.save(fnKidsPackage);
    }

    @Override
    public void deleteKidsPackageAdvance(UserPrincipal principal, Long id) {
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        this.transferMoneyToCashbook(fnKidsPackage, FinanceConstant.CASH_BOOK_ORIGIN_DELETE);
        fnKidsPackage.setApproved(false);
        fnKidsPackage.setLocked(false);
        fnKidsPackage.setPaid(0);
        fnKidsPackage.setDelActive(false);
        fnKidsPackageRepository.save(fnKidsPackage);
    }

    private void transferMoneyToCashbook(FnKidsPackage fnKidsPackage, String origin) {
        Long idSchool = SchoolUtils.getIdSchool();
        double moneyPaid = fnKidsPackage.getPaid();
        if (moneyPaid > 0) {
            FnOrderKids fnOrderKids = fnOrderKidsRepository.findByKidsIdAndMonthAndYear(fnKidsPackage.getKids().getId(), fnKidsPackage.getMonth(), fnKidsPackage.getYear()).orElseThrow();
            String category = fnKidsPackage.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN) ? FinanceConstant.CATEGORY_OUT : FinanceConstant.CATEGORY_IN;
            cashBookHistoryService.saveCashBookHistory(idSchool, category, FinanceConstant.CASH_BOOK_OTHER, LocalDate.now(), moneyPaid, origin, fnOrderKids.getCode());
        }
    }

    @Override
    public boolean lockedKidsPackage(UserPrincipal principal, LockedRequest request) {
        CommonValidate.checkDataPlus(principal);
        this.saveLocked(request.getId(), request.isLocked(), principal.getId(), LocalDateTime.now());
        return true;
    }

    @Transactional
    @Override
    public boolean approvedKidsPackageMany(UserPrincipal principal, StatusKidsListRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idUser = principal.getId();
        boolean status = request.isStatus();
        LocalDateTime nowTime = LocalDateTime.now();
        request.getKidsList().forEach(x -> x.getFnKidsPackageList().forEach(y -> {
                    FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(y.getId()).orElseThrow();
                    if (!fnKidsPackage.isLocked() && fnKidsPackage.getPaid() == 0 && fnKidsPackage.getUsedNumber() > 0 && fnKidsPackage.getPrice() > 0) {
                        this.saveApproved(fnKidsPackage, status, idUser, nowTime);
                    }
                })
        );
        return true;
    }

    @Override
    public boolean lockedKidsPackageMany(UserPrincipal principal, StatusKidsListRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idUser = principal.getId();
        boolean status = request.isStatus();
        LocalDateTime nowTime = LocalDateTime.now();
        request.getKidsList().forEach(x -> x.getFnKidsPackageList().forEach(y -> this.saveLocked(y.getId(), status, idUser, nowTime)));
        return true;
    }

    @Override
    public boolean saveUsedNumber(UserPrincipal principal, UserNumberRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        this.checkMultiple(fnKidsPackage);
        fnKidsPackage.setUsedNumber(request.getUsedNumber());
        if (!this.checkMoneyZeroResult(fnKidsPackage)) {
            this.saveApproved(fnKidsPackage, AppConstant.APP_TRUE, principal.getId(), LocalDateTime.now());
        } else {
            fnKidsPackageRepository.save(fnKidsPackage);
        }
        return true;
    }

    @Override
    public boolean saveTransferNumberOne(UserPrincipal principal, TransferNumberOneRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        this.checkMultiple(fnKidsPackage);
        fnKidsPackage.setUsedNumber(request.getCalculateNumber());
        if (!this.checkMoneyZeroResult(fnKidsPackage)) {
            this.saveApproved(fnKidsPackage, AppConstant.APP_TRUE, principal.getId(), LocalDateTime.now());
        } else {
            fnKidsPackageRepository.save(fnKidsPackage);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean saveTransferNumberMany(UserPrincipal principal, List<TransferNumberManyRequest> requestList) {
        requestList.forEach(x -> x.getFnKidsPackageList().forEach(y -> {
            FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(y.getId()).orElseThrow();
            if (!fnKidsPackage.isApproved() && !fnKidsPackage.isLocked() && fnKidsPackage.getPaid() == 0) {
                fnKidsPackage.setUsedNumber(y.getCalculateNumber());
                if (!this.checkMoneyZeroResult(fnKidsPackage)) {
                    this.saveApproved(fnKidsPackage, AppConstant.APP_TRUE, principal.getId(), LocalDateTime.now());
                } else {
                    fnKidsPackageRepository.save(fnKidsPackage);
                }
            }
        }));
        return true;
    }

    @Override
    public boolean saveUseNumberMany(UserPrincipal principal, List<SaveNumberManyRequest> requestList) {
        requestList.forEach(x -> x.getFnKidsPackageList().forEach(y -> {
            FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(y.getId()).orElseThrow();
            if (!fnKidsPackage.isApproved() && !fnKidsPackage.isLocked() && fnKidsPackage.getPaid() == 0) {
                fnKidsPackage.setUsedNumber(y.getUsedNumber());
                if (!this.checkMoneyZeroResult(fnKidsPackage)) {
                    this.saveApproved(fnKidsPackage, AppConstant.APP_TRUE, principal.getId(), LocalDateTime.now());
                } else {
                    fnKidsPackageRepository.save(fnKidsPackage);
                }
            }
        }));
        return true;
    }

    private boolean checkBeforeGenerate(Long idKid, FnKidsPackageDefault fnKidsPackageDefault, int month, int year) {
        FnPackage fnPackage = fnKidsPackageDefault.getFnPackage();
        String type = fnPackage.getType();
        Long idPackage = fnPackage.getId();
        //với khoản thu một lần
        if (type.equals(FinanceConstant.TYPE_SINGLE)) {
            LocalDate date = LocalDate.of(year, month, 1);
            //check ngày hết hạn phải lớn hơn hoặc bằng ngày mùng 1 của tháng cần tạo
            LocalDate dateExpired = fnKidsPackageDefault.getEndDateExpired();
            boolean checkNotExpired = true;
            if (fnKidsPackageDefault.isExpired() && dateExpired != null) {
                checkNotExpired = dateExpired.isAfter(date) || dateExpired.isEqual(date);
            }
            if (checkNotExpired) {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndFnPackageIdAndDelActiveTrue(idKid, idPackage);
                //chưa có cái nào trong quá khứ ở trạng thái chưa xóa
                return CollectionUtils.isEmpty(fnKidsPackageList);
            }
            //với khoản thu nhiều lần
        } else if (type.equals(FinanceConstant.TYPE_MULTIPLE)) {
            FnMonthObject fnMonthObject = modelMapper.map(fnKidsPackageDefault, FnMonthObject.class);
            boolean checkMonth = FinanceUltils.getStatusOfMonth(month, fnMonthObject);
            //trùng với tháng được check tạo
            if (checkMonth) {
                Optional<FnKidsPackage> fnKidsPackageOptional = fnKidsPackageRepository.findByKidsIdAndFnPackageIdAndMonthAndYearAndDelActiveTrue(idKid, idPackage, month, year);
                //chưa được tạo trong tháng này
                return fnKidsPackageOptional.isEmpty();
            }
        }
        return false;
    }

    private void activeKidsPackage(KidsPackageActiveRequest request) {
        request.getFnKidsPackageList().forEach(x -> {
            FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findById(x.getId()).orElseThrow();
            modelMapper.map(x, fnKidsPackage);
            fnKidsPackageRepository.save(fnKidsPackage);
        });
    }

    private int generateForKidsPackage(Kids kids, SchoolConfigResponse schoolConfig, int month, int year, Long idUser, String generateType) {
        AtomicInteger number = new AtomicInteger();
        List<FnKidsPackageDefault> fnKidsPackageDefaultList = kids.getFnKidsPackageDefaultList();
        fnKidsPackageDefaultList.forEach(y -> {
//            Instant start = Instant.now();
            boolean check = this.checkBeforeGenerate(kids.getId(), y, month, year);
//            Instant end = Instant.now();
//            System.out.println(Duration.between(start, end).toMillis());
//            Instant start1 = Instant.now();
            if (check) {
                this.createKidsPackage(kids, y, schoolConfig, month, year, idUser, generateType);
                number.getAndIncrement();
            }
//            Instant end1 = Instant.now();
//            System.out.println(Duration.between(start1, end1).toMillis());
        });
        return number.get();
    }

    private void createKidsPackage(Kids kids, FnKidsPackageDefault fnKidsPackageDefault, SchoolConfigResponse schoolConfig, int month, int year, Long idUser, String generateType) {
        FnKidsPackage fnKidsPackage = modelMapper.map(fnKidsPackageDefault, FnKidsPackage.class);
        fnKidsPackage.setId(null);
        fnKidsPackage.setYear(year);
        fnKidsPackage.setMonth(month);
        this.setCreateProperties(fnKidsPackage, schoolConfig);
        fnKidsPackage.setKids(kids);
        fnKidsPackage.setFnPackage(fnKidsPackageDefault.getFnPackage());
        if (generateType.equals(FinanceConstant.GENERATE_AUTO)) {
            fnKidsPackage.setIdCreated(idUser);
            fnKidsPackage.setCreatedDate(LocalDateTime.now());
            fnKidsPackage.setIdModified(idUser);
            fnKidsPackage.setLastModifieDate(LocalDateTime.now());
        }
        FnKidsPackage fnKidsPackageSaved = fnKidsPackageRepository.save(fnKidsPackage);
        //tạo thêm khoản đính kèm
        String generate = generateType.equals(FinanceConstant.GENERATE_AUTO) ? FinanceConstant.GENERATE_AUTO : FinanceConstant.GENERATE_PART;
        fnPackageKidsExtendService.saveKidsExtend(Collections.singletonList(fnKidsPackageSaved), generate);
    }

    /**
     * check hết hạn với khoản thu một lần
     * true là chưa hêt hạn
     * flase là đã hết hạn
     */
    private boolean checkBeforeAddFromPackageDefault(FnKidsPackageDefault fnKidsPackageDefault, int month, int year) {
        LocalDate date = LocalDate.of(year, month, 1);
        if (fnKidsPackageDefault.isExpired()) {
            if (fnKidsPackageDefault.getFnPackage().getType().equals(FinanceConstant.TYPE_SINGLE)) {
                return fnKidsPackageDefault.getEndDateExpired().isAfter(date) || fnKidsPackageDefault.getEndDateExpired().isEqual(date);
            }
        }
        return true;
    }

    private void checkBeforeUpdateKidsPackage(FnKidsPackage fnKidsPackage) {
        this.checkApproved(fnKidsPackage);
        this.checkLocked(fnKidsPackage);
    }

    private void checkApproved(FnKidsPackage fnKidsPackage) {
        if (fnKidsPackage.isApproved()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.APPROVED_KIDS_PACKAGE);
        }
    }

    private void checkLocked(FnKidsPackage fnKidsPackage) {
        if (fnKidsPackage.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.LOCKED_KIDS_PACKAGE);
        }
    }

    private void checkMoneyZero(FnKidsPackage fnKidsPackage) {
        if (fnKidsPackage.getUsedNumber() == 0 || fnKidsPackage.getPrice() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ZERO_MONEY);
        }
    }

    private boolean checkMoneyZeroResult(FnKidsPackage fnKidsPackage) {
        //các khoản có giá bằng 0 thì ko cho duyệt
        if (fnKidsPackage.getUsedNumber() == 0 || fnKidsPackage.getPrice() == 0) {
            return true;
        }
        //các khoản đã duyệt thì ko được duyệt thêm
        if (fnKidsPackage.isApproved()) {
            return true;
        }
        return false;
    }

    private void checkPayment(FnKidsPackage fnKidsPackage) {
        if (fnKidsPackage.getPaid() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PAYMENT_KIDS_PACKAGE);
        }
    }

    private String getApprovedNumber(List<FnKidsPackage> fnKidsPackageList) {
        return fnKidsPackageList.stream().filter(FnKidsPackage::isApproved).count() + "/" + fnKidsPackageList.size();
    }

    private String getLockedNumber(List<FnKidsPackage> fnKidsPackageList) {
        return fnKidsPackageList.stream().filter(FnKidsPackage::isLocked).count() + "/" + fnKidsPackageList.size();
    }

    private void saveApproved(FnKidsPackage fnKidsPackage, boolean status, Long idUser, LocalDateTime nowTime) {
        if (!fnKidsPackage.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoản chưa được đăng ký");
        }
        if (status != fnKidsPackage.isApproved()) {
            fnKidsPackage.setApproved(status);
            fnKidsPackage.setTimeApproved(nowTime);
            fnKidsPackage.setIdApproved(idUser);
            fnKidsPackageRepository.save(fnKidsPackage);
        }
    }

    private void saveLocked(Long id, boolean status, Long idUser, LocalDateTime nowTime) {
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        if (status != fnKidsPackage.isLocked()) {
            fnKidsPackage.setLocked(status);
            fnKidsPackage.setTimeLocked(nowTime);
            fnKidsPackage.setIdLocked(idUser);
            fnKidsPackageRepository.save(fnKidsPackage);
        }
    }

    private void checkMultiple(FnKidsPackage fnKidsPackage) {
        this.checkApproved(fnKidsPackage);
        this.checkLocked(fnKidsPackage);
        this.checkPayment(fnKidsPackage);
    }
}
