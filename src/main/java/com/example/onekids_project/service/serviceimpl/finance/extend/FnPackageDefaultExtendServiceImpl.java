package com.example.onekids_project.service.serviceimpl.finance.extend;

import com.example.onekids_project.entity.finance.fees.FnKidsPackageDefault;
import com.example.onekids_project.entity.finance.feesextend.FnMoneyDefaultExtend;
import com.example.onekids_project.entity.finance.feesextend.FnMoneyExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageDefaultExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.FnKidsPackageDefaultRepository;
import com.example.onekids_project.repository.FnMoneyDefaultExtendRepository;
import com.example.onekids_project.repository.FnPackageDefaultExtendRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.request.finance.extend.MoneyUpdateExtendRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.response.finance.extend.MoneyExtendResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendUpdateResponse;
import com.example.onekids_project.service.servicecustom.finance.extend.FnPackageDefaultExtendService;
import com.example.onekids_project.util.PackageExtendUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-10-06 11:13
 *
 * @author lavanviet
 */
@Service
public class FnPackageDefaultExtendServiceImpl implements FnPackageDefaultExtendService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private FnPackageDefaultExtendRepository fnPackageDefaultExtendRepository;
    @Autowired
    private FnMoneyDefaultExtendRepository fnMoneyDefaultExtendRepository;
    @Autowired
    private FnKidsPackageDefaultRepository fnKidsPackageDefaultRepository;

    @Transactional
    @Override
    public void createDefaultExtendFromKid(Long idSchool, List<Long> idKidList) {
        List<Kids> kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(idKidList, idSchool);
        kidsList.forEach(x -> this.saveDefaultExtend(x.getFnKidsPackageDefaultList()));
    }

    @Override
    public void activeDefaultExtendFromKid(Long idSchool, List<Long> idKidList, boolean active) {
        List<Kids> kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(idKidList, idSchool);
        List<FnPackageDefaultExtend> fnPackageDefaultExtendList = PackageExtendUtils.getDefaultExtendFromKids(kidsList);
        this.saveActiveDefaultExtend(fnPackageDefaultExtendList, active);
    }

    @Override
    public void deleteDefaultExtendFromKid(Long idSchool, List<Long> idKidList) {
        List<Kids> kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(idKidList, idSchool);
        List<FnPackageDefaultExtend> fnPackageDefaultExtendList = PackageExtendUtils.getDefaultExtendFromKids(kidsList);
        fnPackageDefaultExtendRepository.deleteAll(fnPackageDefaultExtendList);
    }

    @Override
    public PackageExtendUpdateResponse getDefaultExtendById(Long idSchool, Long id) {
        FnPackageDefaultExtend fnPackageDefaultExtend = fnPackageDefaultExtendRepository.findByIdAndIdSchool(id, idSchool).orElseThrow();
        PackageExtendUpdateResponse response = modelMapper.map(fnPackageDefaultExtend, PackageExtendUpdateResponse.class);
        List<MoneyExtendResponse> moneyExtendResponseList = new ArrayList<>();
        fnPackageDefaultExtend.getFnMoneyDefaultExtendList().forEach(x -> {
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
    public void updateDefaultExtend(Long idSchool, PackageExtendUpdateRequest request) {
        PackageExtendUtils.checkUpdateMoneyList(request);
        FnPackageDefaultExtend fnPackageDefaultExtend = fnPackageDefaultExtendRepository.findByIdAndIdSchool(request.getId(), idSchool).orElseThrow();
        List<FnMoneyDefaultExtend> fnMoneyExtendList = fnPackageDefaultExtend.getFnMoneyDefaultExtendList();
        modelMapper.map(request, fnPackageDefaultExtend);
        FnPackageDefaultExtend fnPackageDefaultExtendSaved = fnPackageDefaultExtendRepository.save(fnPackageDefaultExtend);
        List<MoneyUpdateExtendRequest> newMoneyList = request.getMoneyList().stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        List<MoneyUpdateExtendRequest> oldMoneyList = request.getMoneyList().stream().filter(x -> fnMoneyExtendList.stream().anyMatch(y -> y.getId().equals(x.getId()))).collect(Collectors.toList());
        List<FnMoneyDefaultExtend> deleteMoneyList = fnMoneyExtendList.stream().filter(x -> request.getMoneyList().stream().noneMatch(y -> x.getId().equals(y.getId()))).collect(Collectors.toList());
        deleteMoneyList.forEach(x -> fnMoneyDefaultExtendRepository.deleteById(x.getId()));
        newMoneyList.forEach(x -> {
            FnMoneyDefaultExtend fnMoneyExtend = modelMapper.map(x, FnMoneyDefaultExtend.class);
            fnMoneyExtend.setRangeALl(PackageExtendUtils.convertListToString(x.getDataList()));
            fnMoneyExtend.setRangeAbsentYes(PackageExtendUtils.convertListToString(x.getAbsentYesList()));
            fnMoneyExtend.setRangeAbsentNo(PackageExtendUtils.convertListToString(x.getAbsentNoList()));
            fnMoneyExtend.setRangeNoAttendance(PackageExtendUtils.convertListToString(x.getNoAttendanceList()));
            fnMoneyExtend.setFnPackageDefaultExtend(fnPackageDefaultExtendSaved);
            fnMoneyDefaultExtendRepository.save(fnMoneyExtend);
        });
        oldMoneyList.forEach(x -> {
            FnMoneyDefaultExtend fnMoneyExtend = fnMoneyDefaultExtendRepository.findById(x.getId()).orElseThrow();
            modelMapper.map(x, fnMoneyExtend);
            fnMoneyExtend.setRangeALl(PackageExtendUtils.convertListToString(x.getDataList()));
            fnMoneyExtend.setRangeAbsentYes(PackageExtendUtils.convertListToString(x.getAbsentYesList()));
            fnMoneyExtend.setRangeAbsentNo(PackageExtendUtils.convertListToString(x.getAbsentNoList()));
            fnMoneyExtend.setRangeNoAttendance(PackageExtendUtils.convertListToString(x.getNoAttendanceList()));
            fnMoneyDefaultExtendRepository.save(fnMoneyExtend);
        });
    }

    @Transactional
    @Override
    public void createDefaultExtendFromPackage(Long idSchool, List<Long> idDefaultPackageList) {
        List<FnKidsPackageDefault> fnKidsPackageDefaultList = fnKidsPackageDefaultRepository.findByIdInAndFnPackageIdSchool(idDefaultPackageList, idSchool);
        this.saveDefaultExtend(fnKidsPackageDefaultList);
    }

    @Override
    public void activeDefaultExtendFromPackage(Long idSchool, List<Long> idDefaultPackageList, boolean active) {
        List<FnKidsPackageDefault> fnKidsPackageDefaultList = fnKidsPackageDefaultRepository.findByIdInAndFnPackageIdSchool(idDefaultPackageList, idSchool);
        List<FnPackageDefaultExtend> fnPackageDefaultExtendList = PackageExtendUtils.getDefaultExtendFromDefaultPackage(fnKidsPackageDefaultList);
        this.saveActiveDefaultExtend(fnPackageDefaultExtendList, active);
    }

    @Override
    public void deleteDefaultExtendFromPackage(Long idSchool, List<Long> idDefaultPackageList) {
        List<FnKidsPackageDefault> fnKidsPackageDefaultList = fnKidsPackageDefaultRepository.findByIdInAndFnPackageIdSchool(idDefaultPackageList, idSchool);
        List<FnPackageDefaultExtend> fnPackageDefaultExtendList = PackageExtendUtils.getDefaultExtendFromDefaultPackage(fnKidsPackageDefaultList);
        fnPackageDefaultExtendRepository.deleteAll(fnPackageDefaultExtendList);
    }

    private void saveDefaultExtend(List<FnKidsPackageDefault> fnKidsPackageDefaultList) {
        fnKidsPackageDefaultList = fnKidsPackageDefaultList.stream().filter(a -> a.getFnPackage().isDelActive() && a.getFnPackage().getFnPackageExtend() != null && a.getFnPackageDefaultExtend() == null).collect(Collectors.toList());
        fnKidsPackageDefaultList.forEach(y -> {
            FnPackageExtend fnPackageExtend = y.getFnPackage().getFnPackageExtend();
            FnPackageDefaultExtend fnPackageDefaultExtend = modelMapper.map(fnPackageExtend, FnPackageDefaultExtend.class);
            fnPackageDefaultExtend.setId(null);
            fnPackageDefaultExtend.setFnKidsPackageDefault(y);
            FnPackageDefaultExtend fnPackageDefaultExtendSaved = fnPackageDefaultExtendRepository.save(fnPackageDefaultExtend);
            List<FnMoneyExtend> fnMoneyExtendList = y.getFnPackage().getFnPackageExtend().getFnMoneyExtendList();
            fnMoneyExtendList.forEach(z -> {
                FnMoneyDefaultExtend fnMoneyDefaultExtend = modelMapper.map(z, FnMoneyDefaultExtend.class);
                fnMoneyDefaultExtend.setId(null);
                fnMoneyDefaultExtend.setFnPackageDefaultExtend(fnPackageDefaultExtendSaved);
                fnMoneyDefaultExtendRepository.save(fnMoneyDefaultExtend);
            });
        });
    }

    private void saveActiveDefaultExtend(List<FnPackageDefaultExtend> fnPackageDefaultExtendList, boolean active) {
        fnPackageDefaultExtendList.forEach(a -> a.setActive(active));
        fnPackageDefaultExtendRepository.saveAll(fnPackageDefaultExtendList);
    }

}
