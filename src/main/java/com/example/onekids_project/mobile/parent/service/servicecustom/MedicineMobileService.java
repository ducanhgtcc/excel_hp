package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.medicine.MedicineMobileRequest;
import com.example.onekids_project.mobile.parent.response.medicine.ListMedicineMobileResponse;
import com.example.onekids_project.mobile.parent.response.medicine.MedicineDetailMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MedicineMobileService {

    ListMedicineMobileResponse findMedicineMoblie(UserPrincipal principal, Pageable pageable, LocalDateTime dateTime);

    boolean medicineRevoke(Long id);

    MedicineDetailMobileResponse findMedicineDetailMobile(UserPrincipal principal, Long id);

    boolean createMedicineMob(UserPrincipal principal, MedicineMobileRequest medicineMobileRequest) throws FirebaseMessagingException;
}
