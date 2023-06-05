package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.kidstransfer.KidsTransferParentCreateRequest;
import com.example.onekids_project.mobile.parent.request.kidstransfer.KidsTransferParentUpdateRequest;
import com.example.onekids_project.mobile.parent.response.kidstransfer.KidsTransferParentResponse;
import com.example.onekids_project.request.kids.transfer.KidsTransferUpdateRequest;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;
import java.util.List;

/**
 * @author lavanviet
 */
public interface KidsTransferParentService {
    List<KidsTransferParentResponse> kidsTransferSearch();

    void kidsTransferUpdateService(UserPrincipal principal, KidsTransferParentUpdateRequest request) throws IOException;
    void kidsTransferCreateService(UserPrincipal principal, KidsTransferParentCreateRequest request) throws IOException;
}
