package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.response.kidstransfer.KidsTransferPlusResponse;

import java.util.List;

/**
 * @author lavanviet
 */
public interface KidsTransferPlusService {
    List<KidsTransferPlusResponse> searchDataArrive(Long idKid);
    List<KidsTransferPlusResponse> searchDataLeave(Long idKid);
}
