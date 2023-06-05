package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.school.NotifySchool;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.notifyschool.ListNotifySchoolParentResponse;
import com.example.onekids_project.mobile.parent.response.notifyschool.NotifySchoolParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.NotifySchoolParentMobileService;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.repository.NotifySchoolRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-10-22 10:14 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class NotifySchoolParentMobileServiceImpl implements NotifySchoolParentMobileService {

    @Autowired
    private NotifySchoolRepository notifySchoolRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListNotifySchoolParentResponse searchNotifySchool(Long idSchool, PageNumberRequest request) {
        ListNotifySchoolParentResponse response = new ListNotifySchoolParentResponse();
        List<NotifySchool> notifySchoolList = notifySchoolRepository.searchNotifySchoolParentMobile(idSchool, request);
        List<NotifySchoolParentResponse> dataList = new ArrayList<>();
        notifySchoolList.forEach(x->{
            NotifySchoolParentResponse model = modelMapper.map(x, NotifySchoolParentResponse.class);
            if (CollectionUtils.isNotEmpty(x.getNotifySchoolAttachFileList())){
                List<AttachFileMobileResponse> fileList = listMapper.mapList(x.getNotifySchoolAttachFileList(), AttachFileMobileResponse.class);
                model.setFileList(fileList);
            }
            dataList.add(model);
        });
        boolean lastPage = notifySchoolList.size() < MobileConstant.MAX_PAGE_ITEM;
        response.setLastPage(lastPage);
        response.setDataList(dataList);
        return response;
    }
}
