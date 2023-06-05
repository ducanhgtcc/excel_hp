package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.kidstransfer.KidsTransferTeacherResponse;

import java.util.List;

/**
 * @author lavanviet
 */
public interface KidsTransferTeacherService {
    List<KidsTransferTeacherResponse> searchDataArrive(Long idKid);
    List<KidsTransferTeacherResponse> searchDataLeave(Long idKid);
}
