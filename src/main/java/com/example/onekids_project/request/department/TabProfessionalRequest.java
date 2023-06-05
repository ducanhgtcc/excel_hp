package com.example.onekids_project.request.department;

import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.request.birthdaymanagement.UpdateReiceiversRequest;
import com.example.onekids_project.response.parentdiary.MessageParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabProfessionalRequest extends IdRequest {
    private Boolean isMaster;
    private List<Long> listIdSubject;
//    private List<Long> idSubjectInClassList;
//    private Boolean checkIsClass;
}
