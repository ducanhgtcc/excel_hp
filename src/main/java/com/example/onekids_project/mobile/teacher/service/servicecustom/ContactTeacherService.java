package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.response.phonebook.KidTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.phonebook.ListContactTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.phonebook.ParentTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface ContactTeacherService {
    KidTeacherResponse findKidPhoneBook(UserPrincipal principal, Long idKid);

    List<ParentTeacherResponse> findParentPhoneBook(UserPrincipal principal);

    ListContactTeacherResponse findTeacherPhoneBook(UserPrincipal principal, PageNumberRequest pageNumberRequest);
}
