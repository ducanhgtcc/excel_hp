package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.finance.*;
import com.example.onekids_project.response.finance.*;
import com.example.onekids_project.response.finance.fnpackage.PackageRootRequest;
import com.example.onekids_project.response.finance.fnpackage.PackageRootResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.FnKidsPackageDefaultService;
import com.example.onekids_project.service.servicecustom.finance.FnPackageService;
import com.example.onekids_project.util.SchoolUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FnPackageServiceImpl implements FnPackageService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private FnPackageRepository fnPackageRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnKidsPackageDefaultService fnKidsPackageDefaultService;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;
    @Autowired
    private FnPackageExtendRepository fnPackageExtendRepository;


    @Override
    public List<PackageBriefResponse> searchPackage(UserPrincipal principal, PackageSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<FnPackage> fnPackageList = fnPackageRepository.searchPackage(principal.getIdSchoolLogin(), request);
        return listMapper.mapList(fnPackageList, PackageBriefResponse.class);
    }

    @Override
    public PackageResponse getById(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnPackage fnPackage = fnPackageRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        return modelMapper.map(fnPackage, PackageResponse.class);
    }

    @Override
    public boolean createPackage(UserPrincipal principal, PackageCreateRequest request) {
        CommonValidate.checkDataPlus(principal);
        this.checkBeforeCreatePackage(request);
        FnPackage fnPackage = modelMapper.map(request, FnPackage.class);
        fnPackage.setIdSchool(principal.getIdSchoolLogin());
        fnPackage.setUsingStatus(AppConstant.APP_TRUE);
        FnPackage fnPackageSaved = fnPackageRepository.save(fnPackage);
        fnPackageSaved.setSortNumber(fnPackageSaved.getId());
        fnPackageRepository.save(fnPackageSaved);
        return true;
    }

    @Override
    public boolean updatePackage(UserPrincipal principal, PackageUpdateRequest request) {
        CommonValidate.checkDataPlus(principal);
        this.checkBeforeUpdatePackage(request);
        FnPackage fnPackage = fnPackageRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), principal.getIdSchoolLogin()).orElseThrow();
        this.checkBeforeUpdatePackageFixed(fnPackage, request);
        modelMapper.map(request, fnPackage);
        fnPackageRepository.save(fnPackage);
        return true;
    }

    @Override
    public void createPackageRoot(Long idSchool) {
        this.createPackageRootRow("Chuyển tiền thanh toán thiếu sang tháng sau", FinanceConstant.CATEGORY_OUT, idSchool, 1);
        this.createPackageRootRow("Chuyển tiền thanh toán thừa sang tháng sau", FinanceConstant.CATEGORY_IN, idSchool, 2);
        this.createPackageRootRow("Truy thu tháng trước", FinanceConstant.CATEGORY_IN, idSchool, 3);
        this.createPackageRootRow("Hoàn trả tiền thừa tháng trước", FinanceConstant.CATEGORY_OUT, idSchool, 4);
    }

    @Override
    public List<PackageRootResponse> searchRootPackage() {
        List<FnPackage> fnPackageList = fnPackageRepository.findByIdSchoolAndRootStatusTrueAndDelActiveTrue(SchoolUtils.getIdSchool());
        return listMapper.mapList(fnPackageList, PackageRootResponse.class);
    }

    @Override
    public void updateRootPackage(PackageRootRequest request) {
        FnPackage fnPackage = fnPackageRepository.findById(request.getId()).orElseThrow();
        modelMapper.map(request, fnPackage);
        fnPackageRepository.save(fnPackage);
    }

    @Override
    public List<KidsPackageForPackageResponse> detailPackage(UserPrincipal principal, Long idPackage, LocalDate date) {
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.searchKidsPackageFlowPackage(idPackage, date);
        List<KidsPackageForPackageResponse> responseList = new ArrayList<>();
        for (FnKidsPackage x : fnKidsPackageList) {
            KidsPackageForPackageResponse model = modelMapper.map(x, KidsPackageForPackageResponse.class);
            model.setKidName(x.getKids().getFullName());
            model.setClassName(x.getKids().getMaClass().getClassName());
            model.setIdClass(x.getKids().getMaClass().getId());
            responseList.add(model);
        }
        return responseList;
    }

    @Transactional
    @Override
    public boolean deletePackageById(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnPackage fnPackage = fnPackageRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        this.checkBeforeDeletePackage(fnPackage);
        fnPackage.setDelActive(AppConstant.APP_FALSE);
        fnPackageRepository.save(fnPackage);
        if (fnPackage.getFnPackageExtend() != null) {
            fnPackageExtendRepository.deleteById(fnPackage.getFnPackageExtend().getId());
        }
        return true;
    }

    @Transactional
    @Override
    public boolean changeSortPackage(UserPrincipal principal, ChangeSortRequest request) {
        FnPackage fnPackage1 = fnPackageRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId1(), principal.getIdSchoolLogin()).orElseThrow();
        FnPackage fnPackage2 = fnPackageRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId2(), principal.getIdSchoolLogin()).orElseThrow();
        long sortNumber1 = fnPackage1.getSortNumber();
        long sortNumber2 = fnPackage2.getSortNumber();
        fnPackage1.setSortNumber(sortNumber2);
        fnPackage2.setSortNumber(sortNumber1);
        fnPackageRepository.save(fnPackage1);
        fnPackageRepository.save(fnPackage2);
        return true;
    }

    private void checkBeforeDeletePackage(FnPackage fnPackage) {
        long count = fnPackage.getMaClassSet().stream().filter(BaseEntity::isDelActive).count();
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Có " + count + " lớp đang áp dụng khoản thu này");
        }
    }

    @Override
    public List<MaClassPackageResponse> searchClassPackage(UserPrincipal principal, String className) {
        CommonValidate.checkDataPlus(principal);
        List<MaClass> maClassList = maClassRepository.searchClassWithName(principal.getIdSchoolLogin(), className);
        List<MaClassPackageResponse> responseList = new ArrayList<>();
        maClassList.forEach(x -> {
            MaClassPackageResponse model = modelMapper.map(x, MaClassPackageResponse.class);
            int count = (int) x.getFnPackageSet().stream().filter(BaseEntity::isDelActive).count();
            model.setNumber(count);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<PackageInClassResponse> getPackageInClass(UserPrincipal principal, Long idClass) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<FnPackage> fnPackageList = fnPackageRepository.findByIdSchoolAndRootStatusFalseAndDelActiveTrueOrderByCategoryAscSortNumberAsc(idSchool);
        List<Long> packageOfClassList = fnPackageRepository.findByIdSchoolAndMaClassSetIdAndDelActiveTrue(idSchool, idClass).stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<PackageInClassResponse> responseList = listMapper.mapList(fnPackageList, PackageInClassResponse.class);
        responseList.forEach(y -> packageOfClassList.forEach(z -> {
            if (y.getId().equals(z)) {
                y.setUsed(AppConstant.APP_TRUE);
            }
        }));
        return responseList;
    }

    @Transactional
    @Override
    public boolean addPackageForClass(UserPrincipal principal, AddPackageClassRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = request.getId();
        List<Long> idPackageList = request.getIdPackageList();

        List<Long> idPackageOfClassOldList = fnPackageRepository.findByIdSchoolAndMaClassSetIdAndDelActiveTrue(idSchool, idClass).stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<Long> idAddList = (List<Long>) CollectionUtils.subtract(idPackageList, idPackageOfClassOldList);
        List<Long> idDeleteList = (List<Long>) CollectionUtils.subtract(idPackageOfClassOldList, idPackageList);
        idAddList.forEach(x -> {
            fnPackageRepository.insertPackageClass(idClass, x);
            fnKidsPackageDefaultService.addPackageForKids(idClass, x);
        });
        idDeleteList.forEach(x -> {
            fnPackageRepository.deletePackageClass(idClass, x);
            fnKidsPackageDefaultService.deletePackageForKids(idClass, x);
        });
        return true;
    }

    private void checkBeforeCreatePackage(PackageCreateRequest request) {
        //check áp dụng điểm danh
        if (request.isAttendance()) {
            if (StringUtils.isBlank(request.getAttendanceType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kiểu điểm danh không được để trống");
            }
            if (!FinanceConstant.ATTENDANCE_PICKUP_LATE.equals(request.getAttendanceType()) && StringUtils.isBlank(request.getAttendanceDetail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi tiết loại điểm danh không được để trống");
            }
        }
        //check khoản thu một lần
        if (request.getType().equals(FinanceConstant.TYPE_SINGLE)) {
            if (request.isExpired()) {
                if (request.getEndDateExpired() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa chọn hạn thu");
                }
            }
            //khoản thu nhiều lần
        } else if (request.getType().equals(FinanceConstant.TYPE_MULTIPLE)) {
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
            if (request.isExpired()) {
                if (request.getFebNumberExpired() == 0 || request.getSmallNumberExpired() == 0 || request.getLargeNumberExpired() == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa chọn ngày hết hạn của các tháng");
                }
            }
        }
    }

    private void checkBeforeUpdatePackage(PackageUpdateRequest request) {
        //check áp dụng điểm danh
        if (request.isAttendance()) {
            if (StringUtils.isBlank(request.getAttendanceType())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kiểu điểm danh không được để trống");
            }
            if (!FinanceConstant.ATTENDANCE_PICKUP_LATE.equals(request.getAttendanceType()) && StringUtils.isBlank(request.getAttendanceDetail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi tiết loại điểm danh không được để trống");
            }
        }
        //check khoản thu một lần
        if (request.getType().equals(FinanceConstant.TYPE_SINGLE)) {
            if (request.isExpired()) {
                if (request.getEndDateExpired() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa chọn hạn thu");
                }
            }
            //khoản thu nhiều lần
        } else if (request.getType().equals(FinanceConstant.TYPE_MULTIPLE)) {
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
            if (request.isExpired()) {
                if (request.getFebNumberExpired() == 0 || request.getSmallNumberExpired() == 0 || request.getLargeNumberExpired() == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa chọn ngày hết hạn của các tháng");
                }
            }
        }
    }

    private void checkBeforeUpdatePackageFixed(FnPackage fnPackage, PackageUpdateRequest request) {
        if (!fnPackage.getName().equals(request.getName()) || !fnPackage.getCategory().equals(request.getCategory()) ||
                !fnPackage.getUnit().equals(request.getUnit()) || !fnPackage.getType().equals(request.getType()) ||
                fnPackage.isAttendance() != (request.isAttendance()) || !fnPackage.getAttendanceType().equals(request.getAttendanceType()) ||
                !fnPackage.getAttendanceDetail().equals(request.getAttendanceDetail()) ||
                !fnPackage.getAttendancePaid().equals(request.getAttendancePaid())) {
            long count = fnPackage.getFnKidsPackageList().stream().filter(BaseEntity::isDelActive).count();
            if (count > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khoản đã được áp dụng cho " + count + " học sinh");
            }
        }
    }

    private void createPackageRootRow(String name, String category, Long idSchool, int rootNumber) {
        FnPackage fnPackage = new FnPackage();
        fnPackage.setName(name);
        fnPackage.setCategory(category);
        fnPackage.setDescription(FinanceConstant.PACKAGE_DESCRIPTION);
        fnPackage.setUnit(FinanceConstant.PACKAGE_UNIT);
        fnPackage.setNumber(1);
        fnPackage.setPrice(0);
        fnPackage.setDiscount(false);
        fnPackage.setActive(true);
        fnPackage.setAttendance(false);
        fnPackage.setType(FinanceConstant.TYPE_SINGLE);
        fnPackage.setExpired(false);
        fnPackage.setUsingStatus(true);
        fnPackage.setRootStatus(true);
        fnPackage.setIdSchool(idSchool);
        fnPackage.setRootNumber(rootNumber);
        fnPackageRepository.save(fnPackage);
    }
}
