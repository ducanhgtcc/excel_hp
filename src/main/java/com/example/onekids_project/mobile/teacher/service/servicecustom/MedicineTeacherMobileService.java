package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.UpdateTeacherReplyRequest;
import com.example.onekids_project.mobile.teacher.request.medicine.SearchMedicineTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.medicine.UpdateTeacherReplyMedcRequest;
import com.example.onekids_project.mobile.teacher.request.medicine.UpdateTeacherReplyMedicineRequest;
import com.example.onekids_project.mobile.teacher.response.medicine.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

public interface MedicineTeacherMobileService {

    ListMedicineTeacherResponse searchMedicine(UserPrincipal principal, SearchMedicineTeacherRequest searchMedicineTeacherRequest);

    MedicineTeacherDetailResponse findDetailMedicineTeacher(UserPrincipal principal, Long id);

    MedicineTeacherRevokeResponse medicineTeacherRevoke(UserPrincipal principal, Long id);

    MedicineTeacherSendReplyResponse sendTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyMedcRequest updateTeacherReplyMedcRequest) throws FirebaseMessagingException;

    MedicineTeacherConfirmResponse medicineTeacherConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    MedicineTeacherSendReplyResponse updateTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyMedcRequest updateTeacherReplyMedcRequest) throws FirebaseMessagingException;
}
