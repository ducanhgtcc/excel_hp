package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.entity.school.InternalNotificationPlus;
import com.example.onekids_project.master.request.notify.SearchInternalNotificationPlus;
import com.example.onekids_project.master.response.notify.InternalNotificationPlusResponse;
import com.example.onekids_project.master.response.notify.ListInternalNotificationPlusResponse;
import com.example.onekids_project.master.service.InternalNotificationPlusService;
import com.example.onekids_project.repository.InternalNotificationPlusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-08-12 2:22 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class InternalNotificationPlusServiceImpl implements InternalNotificationPlusService {

    @Autowired
    private InternalNotificationPlusRepository internalNotificationPlusRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListInternalNotificationPlusResponse findInternalNotificationPlus(SearchInternalNotificationPlus request) {
        ListInternalNotificationPlusResponse responseList = new ListInternalNotificationPlusResponse();
        List<InternalNotificationPlus> internalNotificationPlusList = internalNotificationPlusRepository.findInternalPlus(request);
        long total =  internalNotificationPlusRepository.countInternalPlus(request);
        List<InternalNotificationPlusResponse> dataList = new ArrayList<>();
        internalNotificationPlusList.forEach(x->{
            InternalNotificationPlusResponse internalNotificationPlusResponse = modelMapper.map(x, InternalNotificationPlusResponse.class);
            internalNotificationPlusResponse.setFullName(x.getInfoEmployeeSchool().getFullName());
            dataList.add(internalNotificationPlusResponse);
        });
        responseList.setDataList(dataList);
        responseList.setTotal(total);
        return responseList;
    }

    @Override
    public InternalNotificationPlusResponse findByIdNotificationPlus(Long id) {
        InternalNotificationPlus internalNotificationPlus = internalNotificationPlusRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        InternalNotificationPlusResponse response = modelMapper.map(internalNotificationPlus, InternalNotificationPlusResponse.class);
        response.setFullName(internalNotificationPlus.getInfoEmployeeSchool().getFullName());
        return response;
    }
}
