package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.commononekids.ChangePhoneSMSRequest;
import com.example.onekids_project.response.commononekids.LocalStorageResponse;
import com.example.onekids_project.response.commononekids.PlusInSchoolResponse;
import com.example.onekids_project.response.commononekids.SchoolConfigCommonResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface CommonOnekidsService {

    boolean changePhoneSmsUser(ChangePhoneSMSRequest request);

    List<PlusInSchoolResponse> findInforEmployeeInEmployee(UserPrincipal principal);

    ResponseEntity updateChangeSchool(UserPrincipal principal, Long idShool);

    SchoolConfigCommonResponse getSchoolConfigCommon(UserPrincipal userPrincipal);

    String getAvatarUser(UserPrincipal principal);

    Set<String> getApiUser(UserPrincipal principal);

    String getSchoolName(UserPrincipal principal);


}
