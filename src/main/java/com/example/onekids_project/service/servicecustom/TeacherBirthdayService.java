package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.UpdateReiceiversRequest;
import com.example.onekids_project.response.birthdaymanagement.KidBirthdayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListTeacherBirthDayResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.Optional;

public interface TeacherBirthdayService {

    Optional<EmployeeDTO> findByIdEmployee(UserPrincipal principal, Long idSchoolLogin, Long id);

    KidBirthdayResponse updateApprove(Long idSchoolLogin, UserPrincipal principal, UpdateReiceiversRequest updateReiceiversEditRequest);

    ListTeacherBirthDayResponse searchTeacherBirthdayNewa(UserPrincipal principal, SearchTeacherBirthDayRequest request);
}
