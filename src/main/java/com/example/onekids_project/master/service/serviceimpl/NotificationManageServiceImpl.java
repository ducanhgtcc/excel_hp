package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.NotificationManage;
import com.example.onekids_project.entity.school.NotificationManageDate;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.notify.NotificationManageRequest;
import com.example.onekids_project.master.request.notify.NotificationStatusRequest;
import com.example.onekids_project.master.request.notify.NotifyManageDateRequest;
import com.example.onekids_project.master.request.notify.SearchNotificationRequest;
import com.example.onekids_project.master.response.notify.ListNotifyManageResponse;
import com.example.onekids_project.master.response.notify.NotifyManageDateResponse;
import com.example.onekids_project.master.response.notify.NotifyManageResponse;
import com.example.onekids_project.master.service.NotificationManageService;
import com.example.onekids_project.repository.NotificationManageDateRepository;
import com.example.onekids_project.repository.NotificationManageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-08-02 9:49 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class NotificationManageServiceImpl implements NotificationManageService {

    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private NotificationManageRepository notificationManageRepository;
    @Autowired
    private NotificationManageDateRepository notificationManageDateRepository;

    @Override
    public ListNotifyManageResponse findNotifyManage(Long idSchool, SearchNotificationRequest request) {
        ListNotifyManageResponse responseList = new ListNotifyManageResponse();
        List<NotifyManageResponse> dataList  = new ArrayList<>();
        List<NotificationManage> notificationManageList =  notificationManageRepository.findNotificationManage(idSchool, request);
        long total =  notificationManageRepository.countNotificationManage(idSchool, request);
        notificationManageList.forEach(x->{
            NotifyManageResponse notifyManageResponse = modelMapper.map(x, NotifyManageResponse.class);
            List<NotifyManageDateResponse> notifyManageDateResponseList = listMapper.mapList(x.getNotificationManageDateList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList()), NotifyManageDateResponse.class);
            notifyManageResponse.setNotifyManageDateResponseList(notifyManageDateResponseList);
            dataList.add(notifyManageResponse);
        });
        responseList.setDataList(dataList);
        responseList.setTotal(total);
        return responseList;
    }

    @Override
    public NotifyManageDateResponse findDateNotification(Long id) {
        NotificationManageDate notificationManageDate =  notificationManageDateRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        NotifyManageDateResponse response = modelMapper.map(notificationManageDate, NotifyManageDateResponse.class);
        LocalTime time = LocalTime.of(notificationManageDate.getHour(), notificationManageDate.getMinute());
        response.setTime(time);
        return response;
    }

    @Override
    public boolean updateDateNotification(NotifyManageDateRequest request) {
        NotificationManageDate notificationManageDate = notificationManageDateRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        modelMapper.map(request, notificationManageDate);
        notificationManageDate.setMinute(request.getTime().getMinute());
        notificationManageDate.setHour(request.getTime().getHour());
        notificationManageDateRepository.save(notificationManageDate);
        return true;
    }

    @Override
    public boolean deleteDateNotification(Long id) {
        NotificationManageDate notificationManageDate = notificationManageDateRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        notificationManageDate.setDelActive(AppConstant.APP_FALSE);
        notificationManageDateRepository.save(notificationManageDate);
        return true;
    }

    @Override
    @Transactional
    public boolean createDateNotification(Long idSchool, NotifyManageDateRequest request) {
        NotificationManage notificationManage = notificationManageRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdNotification(), idSchool).orElseThrow();
        NotificationManageDate notificationManageDate = modelMapper.map(request, NotificationManageDate.class);
        notificationManageDate.setMinute(request.getTime().getMinute());
        notificationManageDate.setHour(request.getTime().getHour());
        notificationManageDate.setNotificationManage(notificationManage);
        notificationManageDateRepository.save(notificationManageDate);
        return true;
    }

    @Override
    public boolean getStatusNotifyManage(Long idSchool, NotificationStatusRequest request) {
        NotificationManage notificationManage = notificationManageRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), idSchool).orElseThrow();
        notificationManage.setStatus(request.isStatus());
        notificationManageRepository.save(notificationManage);
        return true;
    }

    @Override
    public boolean getUpdateNotifyManage(Long idSchool, NotificationManageRequest request) {
        NotificationManage notificationManage = notificationManageRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), idSchool).orElseThrow();
        modelMapper.map(request, notificationManage);
        notificationManageRepository.save(notificationManage);
        return true;
    }
}
