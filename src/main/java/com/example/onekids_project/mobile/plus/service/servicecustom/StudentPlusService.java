package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.student.GroupPlusRequest;
import com.example.onekids_project.mobile.plus.response.student.*;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface StudentPlusService {
    StudentYearPlusResponse searchStudentYear(UserPrincipal principal);

    List<GradePlusResponse> searchGrade(UserPrincipal principal);

    List<ClassPlusResponse> searchClass(UserPrincipal principal, Long idGrade);

    List<KidPlusResponse> searchKid(UserPrincipal principal, Long idClass);

    InfoKidResponse searchDeatailKid(UserPrincipal principal, Long idKid);

    List<GroupPlusResponse> searchGroup(UserPrincipal principal);

    List<FeatureKidPlusResponse> searchKidGroup(UserPrincipal principal, GroupPlusRequest request);

    List<FeatureKidPlusResponse> searchKidWait(UserPrincipal principal, Long idClass);

    List<FeatureKidPlusResponse> searchKidReserve(UserPrincipal principal, Long idClass);

    List<FeatureKidPlusResponse> searchKidOff(UserPrincipal principal, Long idClass);
}
