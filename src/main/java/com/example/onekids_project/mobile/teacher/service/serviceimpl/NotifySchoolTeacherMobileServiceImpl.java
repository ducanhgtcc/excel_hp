package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.school.NotifySchool;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.teacher.response.notifyschool.ListNotifySchoolTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.notifyschool.NotifySchoolTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.NotifySchoolTeacherMobileService;
import com.example.onekids_project.repository.NotifySchoolRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-10-22 10:53 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class NotifySchoolTeacherMobileServiceImpl implements NotifySchoolTeacherMobileService {

    @Autowired
    private NotifySchoolRepository notifySchoolRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ListNotifySchoolTeacherResponse searchNotifySchoolTeacher(Long idSchool, PageNumberRequest request) {
        ListNotifySchoolTeacherResponse response = new ListNotifySchoolTeacherResponse();
        List<NotifySchool> notifySchoolList = notifySchoolRepository.searchNotifySchoolTeacherMobile(idSchool, request);
        List<NotifySchoolTeacherResponse> dataList = new ArrayList<>();
        notifySchoolList.forEach(x->{
            NotifySchoolTeacherResponse model = modelMapper.map(x, NotifySchoolTeacherResponse.class);
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
