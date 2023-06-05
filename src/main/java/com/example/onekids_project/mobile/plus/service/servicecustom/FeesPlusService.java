package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.response.fees.FeesClassResponse;
import com.example.onekids_project.mobile.plus.response.fees.KidsFeesResponse;
import com.example.onekids_project.mobile.request.IdAndDateRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-06-07 09:14
 *
 * @author lavanviet
 */
public interface FeesPlusService {
    List<FeesClassResponse> getFeesClass(UserPrincipal principal, LocalDate date);

    List<KidsFeesResponse> getKidsInClass(UserPrincipal principal, IdAndDateRequest request);

    void sendSms(UserPrincipal principal, List<Long> idList, LocalDate date) throws ExecutionException, InterruptedException;

    void sendApp(UserPrincipal principal, List<Long> idList, LocalDate date) throws ExecutionException, InterruptedException, FirebaseMessagingException;

    boolean setOrderShow(UserPrincipal principal, List<StatusRequest> request) throws ExecutionException, FirebaseMessagingException, InterruptedException;
}
