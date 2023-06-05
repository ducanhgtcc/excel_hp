package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.MedicineRequest;
import com.example.onekids_project.request.parentdiary.SearchMedicineRequest;
import com.example.onekids_project.response.parentdiary.ListMedicineResponse;
import com.example.onekids_project.response.parentdiary.MedicineNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface MedicineSerVice {

    boolean updateRead(Long id, List<MedicineRequest> medicineResponse);

    boolean updateConfirmMany(Long id, List<MedicineRequest> medicineResponse, UserPrincipal principal) throws FirebaseMessagingException;

    ListMedicineResponse searchMedicineAbc(UserPrincipal principal, Long idSchool, SearchMedicineRequest request);

    boolean revokePlus(UserPrincipal principal, StatusRequest request);

    boolean updateMedicine(Long idSchoolLogin, UserPrincipal principal, ContentRequest request) throws FirebaseMessagingException;

    boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    boolean revokeTeacher(UserPrincipal principal, StatusRequest request);

    MedicineNewResponse findByIdMedicineNew(UserPrincipal principal, Long id);
}
