package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.request.identifykid.IdentityKidRequest;
import com.example.onekids_project.mobile.teacher.response.identitykid.IdentifyKid;
import com.example.onekids_project.mobile.teacher.response.identitykid.InfoIdentityKid;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;
import java.util.List;

public interface IdentityKidTeacherService {

    List<IdentifyKid> getKidsClass(UserPrincipal principal);

    List<InfoIdentityKid> getKidIdentity(UserPrincipal principal, Long idKid);

    boolean updateDelInsKidIdentity(UserPrincipal principal, IdentityKidRequest identityKidRequest) throws IOException;

}
