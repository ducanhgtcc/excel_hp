package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.kids.KidsExtraInforRequest;
import com.example.onekids_project.mobile.parent.response.kids.KidsExtraInforResponse;
import com.example.onekids_project.mobile.parent.response.kids.KidsParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface KidsParentService {
    /**
     * find kids
     * @param principal
     * @return
     */
    KidsParentResponse findKidById(UserPrincipal principal);
    /**
     * get extra infor kids
     * @param principal
     * @return
     */
    KidsExtraInforResponse findKidsExtraInfo(UserPrincipal principal);

    /**
     * update extra infor
     * @param principal
     * @param kidsExtraInforRequest
     * @return
     */
    KidsExtraInforResponse updateKidsExtraInfo(UserPrincipal principal, KidsExtraInforRequest kidsExtraInforRequest);

}
