package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.GetLinkPlusRequest;
import com.example.onekids_project.mobile.plus.response.KidsReco.DetailClassResponse;
import com.example.onekids_project.mobile.plus.response.KidsReco.ListKidsRecoResponse;
import com.example.onekids_project.mobile.teacher.request.identifykid.IdentityKidRequest;
import com.example.onekids_project.mobile.teacher.response.identitykid.IdentifyKid;
import com.example.onekids_project.mobile.teacher.response.identitykid.InfoIdentityKid;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;
import java.util.List;

public interface KidsRecoPlusService {

    ListKidsRecoResponse searchClass(UserPrincipal principal);

    List<IdentifyKid> getKidsClass(UserPrincipal principal, Long id);

    List<InfoIdentityKid> getKidIdentity(UserPrincipal principal, Long id);

    boolean updateDelInsKidIdentity(UserPrincipal principal, IdentityKidRequest identityKidRequest) throws IOException;
}
