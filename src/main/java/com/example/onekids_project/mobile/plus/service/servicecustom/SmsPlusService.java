package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.sms.SendAccountRequest;
import com.example.onekids_project.mobile.plus.request.sms.SendSmsFailRequest;
import com.example.onekids_project.mobile.plus.request.sms.SendSmsRequest;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.concurrent.ExecutionException;

public interface SmsPlusService {
    boolean sendAccountStudentGrade(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException;

    boolean sendAccountEmployeeDeparment(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException;

    boolean sendAccountStudentClass(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException;

    boolean sendAccountStudentMulti(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException;

    boolean sendAccountStudentGroup(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException;

    boolean sendAccountStudent(UserPrincipal principal, IdRequest request) throws ExecutionException, InterruptedException;

    boolean sendSmsStudentGrade(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException;

    boolean sendSmsStudentClass(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException;

    boolean sendSmsStudentGroup(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException;

    boolean sendSmsStudentMulti(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException;

    boolean sendSmsEmployeeDeparment(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException;

    boolean sendAccountEmployeeMulti(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException;

    boolean sendSmsEmployeeMulti(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException;

    boolean sendSmsFail(UserPrincipal principal, SendSmsFailRequest request);
}
