package com.example.onekids_project.service.serviceimpl.system;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.user.Api;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.api.ApiUpdateRequest;
import com.example.onekids_project.master.response.api.ApiMasterResponse;
import com.example.onekids_project.repository.ApiRepository;
import com.example.onekids_project.request.finance.ChangeSortRequest;
import com.example.onekids_project.response.user.ApiResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.system.ApiService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ApiRepository apiRepository;

    @Override
    public List<ApiResponse> searchApi(UserPrincipal principal, String type) {
        CommonValidate.checkDataSupperPlus(principal);
        if (AppTypeConstant.TEACHER.equals(type)) {
            type = AppTypeConstant.SCHOOL;
        }
        List<Api> apiList = apiRepository.findByTypeAndDelActiveTrueOrderByRanks(type);
        return listMapper.mapList(apiList, ApiResponse.class);
    }

    @Override
    public List<ApiMasterResponse> searchApiMaster(UserPrincipal principal, String type) {
        CommonValidate.checkDataAdmin(principal);
        List<Api> apiList = apiRepository.findByTypeAndDelActiveTrueOrderByRanks(type);
        return listMapper.mapList(apiList, ApiMasterResponse.class);
    }

    @Override
    public boolean updateApi(UserPrincipal principal, ApiUpdateRequest request) {
        Api api = apiRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        modelMapper.map(request, api);
        apiRepository.save(api);
        return true;
    }

    @Override
    public boolean updateApiRanks(UserPrincipal principal, ChangeSortRequest request) {
        Api api1 = apiRepository.findByIdAndDelActiveTrue(request.getId1()).orElseThrow();
        Api api2 = apiRepository.findByIdAndDelActiveTrue(request.getId2()).orElseThrow();
        int ranks1 = api1.getRanks();
        int ranks2 = api2.getRanks();
        api1.setRanks(ranks2);
        api2.setRanks(ranks1);
        apiRepository.save(api1);
        apiRepository.save(api2);
        return true;
    }
}
