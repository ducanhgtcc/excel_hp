package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.DetailSmsRequest;

import java.util.List;

public interface SmsReiceriversRepositoryCustom {

    List<SmsReceivers> findByIdSmsSendNew(Long idSchool, Long idSend, DetailSmsRequest request);

}
