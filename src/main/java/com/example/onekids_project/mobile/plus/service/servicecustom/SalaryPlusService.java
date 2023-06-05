package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.response.salary.EmployeeSalaryPlusResponse;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-06-08 14:37
 *
 * @author lavanviet
 */
public interface SalaryPlusService {
    List<EmployeeSalaryPlusResponse> getSalaryEmployee(UserPrincipal principal, LocalDate date);

    void setOrderShow(UserPrincipal principal, List<StatusRequest> request) throws ExecutionException, FirebaseMessagingException, InterruptedException;
}
