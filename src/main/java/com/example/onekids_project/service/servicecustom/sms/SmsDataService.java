package com.example.onekids_project.service.servicecustom.sms;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-04-03 14:24
 *
 * @author lavanviet
 */
public interface SmsDataService {

    /**
     * @param kidList
     * @param idSchool
     * @param request: ko cần set title vì trong hàm đã lấy rồi
     * @throws ExecutionException
     * @throws InterruptedException
     */
    void sendSmsKid(List<Kids> kidList, Long idSchool, SmsNotifyDataRequest request) throws ExecutionException, InterruptedException;

    /**
     *
     * @param infoList
     * @param idSchool
     * @param request
     * @param appType app_type của người gửi
     * @throws ExecutionException
     * @throws InterruptedException
     */
    void sendSmsEmployee(List<InfoEmployeeSchool> infoList, Long idSchool, SmsNotifyDataRequest request, String appType) throws ExecutionException, InterruptedException;
}
