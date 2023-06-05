package com.example.onekids_project.service.serviceimpl.finance.extend;

import com.example.onekids_project.entity.finance.fees.FnPackage;
import com.example.onekids_project.entity.finance.feesextend.FnMoneyExtend;
import com.example.onekids_project.entity.finance.feesextend.FnPackageExtend;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.FnMoneyExtendRepository;
import com.example.onekids_project.repository.FnPackageExtendRepository;
import com.example.onekids_project.repository.FnPackageRepository;
import com.example.onekids_project.request.common.ActiveRequest;
import com.example.onekids_project.request.finance.extend.MoneyUpdateExtendRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendCreateRequest;
import com.example.onekids_project.request.finance.extend.PackageExtendUpdateRequest;
import com.example.onekids_project.response.finance.PackageBriefResponse;
import com.example.onekids_project.response.finance.extend.MoneyExtendResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendResponse;
import com.example.onekids_project.response.finance.extend.PackageExtendUpdateResponse;
import com.example.onekids_project.service.servicecustom.finance.extend.FnPackageExtendService;
import com.example.onekids_project.util.PackageExtendUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-10-01 16:13
 *
 * @author lavanviet
 */
@Service
public class FnPackageExtendServiceImpl implements FnPackageExtendService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private FnPackageRepository fnPackageRepository;
    @Autowired
    private FnPackageExtendRepository fnPackageExtendRepository;
    @Autowired
    private FnMoneyExtendRepository fnMoneyExtendRepository;


    @Override
    public List<PackageBriefResponse> getPackageAdd(Long idSchool) {
        List<FnPackage> fnPackageList = fnPackageRepository.findByIdSchoolAndRootStatusFalseAndFnPackageExtendIsNullAndDelActiveTrue(idSchool);
        return listMapper.mapList(fnPackageList, PackageBriefResponse.class);
    }

    @Override
    public List<PackageExtendResponse> getPackageExtend(Long idSchool, String name) {
        List<FnPackageExtend> fnPackageExtendList = fnPackageExtendRepository.searchPackageExtend(idSchool, name);
        List<PackageExtendResponse> responseList = new ArrayList<>();
        fnPackageExtendList.forEach(x -> {
            PackageExtendResponse model = modelMapper.map(x, PackageExtendResponse.class);
            model.setNumber(x.getFnMoneyExtendList().size());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public PackageExtendUpdateResponse getPackageExtendById(Long idSchool, Long id) {
        FnPackageExtend fnPackageExtend = fnPackageExtendRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow();
        PackageExtendUpdateResponse response = modelMapper.map(fnPackageExtend, PackageExtendUpdateResponse.class);
        List<MoneyExtendResponse> moneyExtendResponseList = new ArrayList<>();
        fnPackageExtend.getFnMoneyExtendList().forEach(x -> {
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
    public void createPackageExtend(Long idSchool, PackageExtendCreateRequest request) {
        PackageExtendUtils.checkCreateMoneyList(request);
        FnPackage fnPackage = fnPackageRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdPackage(), idSchool).orElseThrow();
        if (fnPackage.getFnPackageExtend() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đã tồn tại khoản đính kèm");
        }
        FnPackageExtend fnPackageExtend = modelMapper.map(request, FnPackageExtend.class);
        fnPackageExtend.setFnPackage(fnPackage);
        fnPackageExtend.setIdSchool(idSchool);
        FnPackageExtend fnPackageExtendSaved = fnPackageExtendRepository.save(fnPackageExtend);
        request.getMoneyList().forEach(x -> {
            FnMoneyExtend fnMoneyExtend = modelMapper.map(x, FnMoneyExtend.class);
            fnMoneyExtend.setRangeALl(PackageExtendUtils.convertListToString(x.getDataList()));
            fnMoneyExtend.setRangeAbsentYes(PackageExtendUtils.convertListToString(x.getAbsentYesList()));
            fnMoneyExtend.setRangeAbsentNo(PackageExtendUtils.convertListToString(x.getAbsentNoList()));
            fnMoneyExtend.setRangeNoAttendance(PackageExtendUtils.convertListToString(x.getNoAttendanceList()));
            fnMoneyExtend.setFnPackageExtend(fnPackageExtendSaved);
            fnMoneyExtendRepository.save(fnMoneyExtend);
        });
    }

    @Transactional
    @Override
    public void updatePackageExtend(Long idSchool, PackageExtendUpdateRequest request) {
        PackageExtendUtils.checkUpdateMoneyList(request);
        FnPackageExtend fnPackageExtend = fnPackageExtendRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), idSchool).orElseThrow();
        List<FnMoneyExtend> fnMoneyExtendList = fnPackageExtend.getFnMoneyExtendList();
        modelMapper.map(request, fnPackageExtend);
        FnPackageExtend fnPackageExtendSaved = fnPackageExtendRepository.save(fnPackageExtend);
        List<MoneyUpdateExtendRequest> newMoneyList = request.getMoneyList().stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        List<MoneyUpdateExtendRequest> oldMoneyList = request.getMoneyList().stream().filter(x -> fnMoneyExtendList.stream().anyMatch(y -> y.getId().equals(x.getId()))).collect(Collectors.toList());
        List<FnMoneyExtend> deleteMoneyList = fnMoneyExtendList.stream().filter(x -> request.getMoneyList().stream().noneMatch(y -> x.getId().equals(y.getId()))).collect(Collectors.toList());
        deleteMoneyList.forEach(x -> fnMoneyExtendRepository.deleteById(x.getId()));
        newMoneyList.forEach(x -> {
            FnMoneyExtend fnMoneyExtend = modelMapper.map(x, FnMoneyExtend.class);
            fnMoneyExtend.setRangeALl(PackageExtendUtils.convertListToString(x.getDataList()));
            fnMoneyExtend.setRangeAbsentYes(PackageExtendUtils.convertListToString(x.getAbsentYesList()));
            fnMoneyExtend.setRangeAbsentNo(PackageExtendUtils.convertListToString(x.getAbsentNoList()));
            fnMoneyExtend.setRangeNoAttendance(PackageExtendUtils.convertListToString(x.getNoAttendanceList()));
            fnMoneyExtend.setFnPackageExtend(fnPackageExtendSaved);
            fnMoneyExtendRepository.save(fnMoneyExtend);
        });
        oldMoneyList.forEach(x -> {
            FnMoneyExtend fnMoneyExtend = fnMoneyExtendRepository.findById(x.getId()).orElseThrow();
            modelMapper.map(x, fnMoneyExtend);
            fnMoneyExtend.setRangeALl(PackageExtendUtils.convertListToString(x.getDataList()));
            fnMoneyExtend.setRangeAbsentYes(PackageExtendUtils.convertListToString(x.getAbsentYesList()));
            fnMoneyExtend.setRangeAbsentNo(PackageExtendUtils.convertListToString(x.getAbsentNoList()));
            fnMoneyExtend.setRangeNoAttendance(PackageExtendUtils.convertListToString(x.getNoAttendanceList()));
            fnMoneyExtendRepository.save(fnMoneyExtend);
        });
    }

    @Transactional
    @Override
    public void deletePackageExtendById(Long idSchool, Long id) {
        fnPackageExtendRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool).orElseThrow();
        fnPackageExtendRepository.deleteById(id);
    }

    @Override
    public void activePackageExtendById(Long idSchool, ActiveRequest request) {
        FnPackageExtend fnPackageExtend = fnPackageExtendRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), idSchool).orElseThrow();
        fnPackageExtend.setActive(request.getActive());
        fnPackageExtendRepository.save(fnPackageExtend);
    }
}
