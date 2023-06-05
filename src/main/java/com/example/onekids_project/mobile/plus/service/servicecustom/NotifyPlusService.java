package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.notify.NotifyPlusRequest;
import com.example.onekids_project.mobile.plus.response.notify.NotifyPlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;

public interface NotifyPlusService {
    NotifyPlusResponse createNotifyGroup(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException;

    NotifyPlusResponse createNotifyStudent(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException;

    NotifyPlusResponse createNotifyClass(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException;

    NotifyPlusResponse createNotifyGrade(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException;

    NotifyPlusResponse createNotifyEmployee(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException;

    NotifyPlusResponse createNotifyDeparment(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException;
}
