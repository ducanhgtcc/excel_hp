package com.example.onekids_project.service.serviceimpl.finance.extend;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.entity.finance.feesextend.FnMoneyKidsExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageDefaultExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageKidsExtend;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.finance.extend.MoneyUpdateExtendRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.response.finance.extend.MoneyExtendResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendUpdateResponse;
import com.example.onekids_project.service.servicecustom.finance.extend.FnPackageKidsExtendService;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.PackageExtendUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * date 2021-10-07 16:34
 *
 * @author lavanviet
 */
@Service
public class FnPackageKidsExtendServiceImpl implements FnPackageKidsExtendService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;
    @Autowired
    private FnKidsPackageDefaultRepository fnKidsPackageDefaultRepository;
    @Autowired
    private FnPackageKidsExtendRepository fnPackageKidsExtendRepository;
    @Autowired
    private FnMoneyKidsExtendRepository fnMoneyKidsExtendRepository;

    @Transactional
    @Override
    public void createKidsExtendFromKid(Long idSchool, List<Long> idKidList, LocalDate date) {
        List<Kids> kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(idKidList, idSchool);
        kidsList.forEach(x -> this.saveKidsExtend(FinanceUltils.getKidsPackageFromKidDate(x, date), FinanceConstant.GENERATE_MANUAL));
    }

    @Override
    public void activeKidsExtendFromKid(Long idSchool, List<Long> idKidList, boolean active, LocalDate date) {
        List<Kids> kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(idKidList, idSchool);
        List<FnKidsPackage> fnKidsPackageList = new ArrayList<>();
        kidsList.forEach(x -> {
            List<FnKidsPackage> packageList = FinanceUltils.getKidsPackageFromKidDate(x, date);
            fnKidsPackageList.addAll(packageList);
        });
        this.saveActiveKidsExtend(fnKidsPackageList, active);
    }

    @Transactional
    @Override
    public void deleteKidsExtendFromKid(Long idSchool, List<Long> idKidList, LocalDate date) {
        List<Kids> kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(idKidList, idSchool);
        List<FnKidsPackage> fnKidsPackageList = new ArrayList<>();
        kidsList.forEach(x -> {
            List<FnKidsPackage> kidPackageList = FinanceUltils.getKidsPackageFromKidDate(x, date);
            fnKidsPackageList.addAll(kidPackageList);
        });
        this.saveDeleteKidsExtend(fnKidsPackageList);
    }

    @Override
    public PackageExtendUpdateResponse getKidsExtendById(Long idSchool, Long id) {
        FnPackageKidsExtend fnPackageKidsExtend = fnPackageKidsExtendRepository.findByIdAndIdSchool(id, idSchool).orElseThrow();
        PackageExtendUpdateResponse response = modelMapper.map(fnPackageKidsExtend, PackageExtendUpdateResponse.class);
        List<MoneyExtendResponse> moneyExtendResponseList = new ArrayList<>();
        fnPackageKidsExtend.getFnMoneyKidsExtendList().forEach(x -> {
            MoneyExtendResponse model = modelMapper.map(x, MoneyExtendResponse.class);
            model.setDataList(PackageExtendUtils.convertStringToList(x.getRangeALl()));
            model.setAbsentYesList(PackageExtendUtils.convertStringToList(x.getRangeAbsentYes()));
            model.setAbsentNoList(PackageExtendUtils.convertStringToList(x.getRangeAbsentNo()));
            model.setNoAttendanceList(PackageExtendUtils.convertStringToList(x.getRangeNoAttendance()));
            moneyExtendResponseList.add(model);
        });
        response.setMoneyList(moneyExtendResponseList);
        return response;
    }

    @Transactional
    @Override
    public void updateKidsExtend(Long idSchool, PackageExtendUpdateRequest request) {
        PackageExtendUtils.checkUpdateMoneyList(request);
        FnPackageKidsExtend fnPackageKidsExtend = fnPackageKidsExtendRepository.findByIdAndIdSchool(request.getId(), idSchool).orElseThrow();
        PackageExtendUtils.checkUpdateKidsExtend(fnPackageKidsExtend);
        List<FnMoneyKidsExtend> fnMoneyExtendList = fnPackageKidsExtend.getFnMoneyKidsExtendList();
        modelMapper.map(request, fnPackageKidsExtend);
        FnPackageKidsExtend fnPackageKidsExtendSaved = fnPackageKidsExtendRepository.save(fnPackageKidsExtend);
        List<MoneyUpdateExtendRequest> newMoneyList = request.getMoneyList().stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        List<MoneyUpdateExtendRequest> oldMoneyList = request.getMoneyList().stream().filter(x -> fnMoneyExtendList.stream().anyMatch(y -> y.getId().equals(x.getId()))).collect(Collectors.toList());
        List<FnMoneyKidsExtend> deleteMoneyList = fnMoneyExtendList.stream().filter(x -> request.getMoneyList().stream().noneMatch(y -> x.getId().equals(y.getId()))).collect(Collectors.toList());
        deleteMoneyList.forEach(x -> fnMoneyKidsExtendRepository.deleteById(x.getId()));
        newMoneyList.forEach(x -> {
            FnMoneyKidsExtend fnMoneyExtend = modelMapper.map(x, FnMoneyKidsExtend.class);
            fnMoneyExtend.setRangeALl(PackageExtendUtils.convertListToString(x.getDataList()));
            fnMoneyExtend.setRangeAbsentYes(PackageExtendUtils.convertListToString(x.getAbsentYesList()));
            fnMoneyExtend.setRangeAbsentNo(PackageExtendUtils.convertListToString(x.getAbsentNoList()));
            fnMoneyExtend.setRangeNoAttendance(PackageExtendUtils.convertListToString(x.getNoAttendanceList()));
            fnMoneyExtend.setFnPackageKidsExtend(fnPackageKidsExtendSaved);
            fnMoneyKidsExtendRepository.save(fnMoneyExtend);
        });
        oldMoneyList.forEach(x -> {
            FnMoneyKidsExtend fnMoneyExtend = fnMoneyKidsExtendRepository.findById(x.getId()).orElseThrow();
            modelMapper.map(x, fnMoneyExtend);
            fnMoneyExtend.setRangeALl(PackageExtendUtils.convertListToString(x.getDataList()));
            fnMoneyExtend.setRangeAbsentYes(PackageExtendUtils.convertListToString(x.getAbsentYesList()));
            fnMoneyExtend.setRangeAbsentNo(PackageExtendUtils.convertListToString(x.getAbsentNoList()));
            fnMoneyExtend.setRangeNoAttendance(PackageExtendUtils.convertListToString(x.getNoAttendanceList()));
            fnMoneyKidsExtendRepository.save(fnMoneyExtend);
        });
    }

    @Transactional
    @Override
    public void createKidsExtendFromPackage(Long idSchool, List<Long> idKidsPackageList) {
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndDelActiveTrue(idKidsPackageList, idSchool);
        this.saveKidsExtend(fnKidsPackageList, FinanceConstant.GENERATE_MANUAL);
    }

    @Override
    public void activeKidsExtendFromPackage(Long idSchool, List<Long> idKidsPackageList, boolean active) {
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndDelActiveTrue(idKidsPackageList, idSchool);
        this.saveActiveKidsExtend(fnKidsPackageList, active);
    }

    @Transactional
    @Override
    public void deleteKidsExtendFromPackage(Long idSchool, List<Long> idKidsPackageList) {
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndDelActiveTrue(idKidsPackageList, idSchool);
        this.saveDeleteKidsExtend(fnKidsPackageList);
    }

    @Override
    public void saveKidsExtend(List<FnKidsPackage> fnKidsPackageList, String generateType) {
        List<FnPackageKidsExtend> fnPackageKidsExtendList = new ArrayList<>();
        List<FnMoneyKidsExtend> fnMoneyKidsExtendList = new ArrayList<>();
        fnKidsPackageList = fnKidsPackageList.stream().filter(x -> x.isDelActive() && !x.isApproved() && !x.isLocked() && x.getPaid() == 0 && x.getFnPackageKidsExtend() == null).collect(Collectors.toList());
        fnKidsPackageList.forEach(x -> {
            FnPackageKidsExtend fnPackageKidsExtend = null;
            List<FnMoneyKidsExtend> moneyList = new ArrayList<>();
            Optional<FnKidsPackageDefault> fnKidsPackageDefaultOptional = fnKidsPackageDefaultRepository.findByKidsIdAndFnPackageId(x.getKids().getId(), x.getFnPackage().getId());
            //nếu tồn tại mặc định thì: trong mặc định có thì sinh, ko có thì ko sinh(để khi ko muốn sinh nữa thì xóa mặc định đi là xong)
            //nếu ko tồn tại mặc định thì lấy trong trường(để sinh với nhưng khoản được thêm bằng tay vào lấy từ khoản của trường)
            if (fnKidsPackageDefaultOptional.isPresent()) {
                FnPackageDefaultExtend fnPackageDefaultExtend = fnKidsPackageDefaultOptional.get().getFnPackageDefaultExtend();
                if (fnPackageDefaultExtend != null) {
                    fnPackageKidsExtend = modelMapper.map(fnPackageDefaultExtend, FnPackageKidsExtend.class);
                    moneyList = listMapper.mapList(fnPackageDefaultExtend.getFnMoneyDefaultExtendList(), FnMoneyKidsExtend.class);
                }
            } else {
                if (x.getFnPackage().isDelActive() && x.getFnPackage().getFnPackageExtend() != null) {
                    FnPackageExtend fnPackageExtend = x.getFnPackage().getFnPackageExtend();
                    fnPackageKidsExtend = modelMapper.map(fnPackageExtend, FnPackageKidsExtend.class);
                    moneyList = listMapper.mapList(fnPackageExtend.getFnMoneyExtendList(), FnMoneyKidsExtend.class);
                }
            }
            if (fnPackageKidsExtend != null) {
                //nếu tạo tự động(tạo kèm khi khởi tạo khoản hoặc tạo tự động ban đêm) thì chỉ sinh những khoản đính kèm có active=true
                //nếu tạo bằng tay thì mặc định sinh hết
                boolean checkCreate = FinanceConstant.GENERATE_MANUAL.equals(generateType) ? AppConstant.APP_TRUE : fnPackageKidsExtend.isActive();
                if (checkCreate) {
                    fnPackageKidsExtend.setId(null);
                    fnPackageKidsExtend.setFnKidsPackage(x);
                    for (FnMoneyKidsExtend y : moneyList) {
                        y.setId(null);
                        y.setFnPackageKidsExtend(fnPackageKidsExtend);
                    }
                    //nếu tạo tự động ban đêm thì set thêm các thuộc tính liên quan
                    if (FinanceConstant.GENERATE_AUTO.equals(generateType)) {
                        fnPackageKidsExtend.setIdCreated(SystemConstant.ID_SYSTEM_ADMIN);
                        fnPackageKidsExtend.setCreatedDate(LocalDateTime.now());
                        fnPackageKidsExtend.setIdModified(SystemConstant.ID_SYSTEM_ADMIN);
                        fnPackageKidsExtend.setLastModifieDate(LocalDateTime.now());
                        moneyList.forEach(a -> {
                            a.setIdCreated(SystemConstant.ID_SYSTEM_ADMIN);
                            a.setCreatedDate(LocalDateTime.now());
                            a.setIdModified(SystemConstant.ID_SYSTEM_ADMIN);
                            a.setLastModifieDate(LocalDateTime.now());
                        });
                    }
                    fnPackageKidsExtendList.add(fnPackageKidsExtend);
                    fnMoneyKidsExtendList.addAll(moneyList);
                }
            }
        });
        fnPackageKidsExtendRepository.saveAll(fnPackageKidsExtendList);
        fnMoneyKidsExtendRepository.saveAll(fnMoneyKidsExtendList);
    }

    private void saveActiveKidsExtend(List<FnKidsPackage> fnKidsPackageList, boolean active) {
        List<FnPackageKidsExtend> fnPackageKidsExtendList = PackageExtendUtils.getKidsExtendFromKidsPackage(fnKidsPackageList);
        fnPackageKidsExtendList.forEach(a -> a.setActive(active));
        fnPackageKidsExtendRepository.saveAll(fnPackageKidsExtendList);
    }

    private void saveDeleteKidsExtend(List<FnKidsPackage> fnKidsPackageList) {
        List<FnPackageKidsExtend> fnPackageKidsExtendList = PackageExtendUtils.getKidsExtendFromKidsPackage(fnKidsPackageList);
        fnPackageKidsExtendRepository.deleteAll(fnPackageKidsExtendList);
    }
}
