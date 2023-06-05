package com.example.onekids_project.master.service;

import com.example.onekids_project.master.response.ReceiversResponse;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface ReceiversService {
    List<ReceiversResponse> findAllReceivers(Long idUser);

    boolean deleteById(Long id);

    boolean deleteByMultiId(List<Long> idList);

    boolean revokeReceiversNotify(Long idReceivers);

    boolean revokeMultiReceiversNotify(List<Long> idList);

    boolean revokeMultiReceiversNotifyShow(List<Long> idList);

    boolean approvedReceiversNotify(Long idReceivers) throws FirebaseMessagingException;
}
