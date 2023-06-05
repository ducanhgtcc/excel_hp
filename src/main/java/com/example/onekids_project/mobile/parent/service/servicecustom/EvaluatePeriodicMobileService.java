package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.evaluate.EvaluatePeriodicMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.ListEvaluatePeriodicMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

public interface EvaluatePeriodicMobileService {
    /**
     * find evaluate week of year
     *
     * @param principal
     * @param id
     * @return
     */
    EvaluatePeriodicMobileResponse findEvaluatePeriodic(UserPrincipal principal, Long id, Pageable pageable);

    /**
     * create parent reply
     *
     * @param principal
     * @param id
     * @param content
     * @return
     */
    ListEvaluatePeriodicMobileResponse createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException;

    /**
     * update
     *
     * @param principal
     * @param id
     * @param content
     * @return
     */
    ListEvaluatePeriodicMobileResponse updateParentReply(UserPrincipal principal, Long id, String content);

    /**
     * thu hồi
     *
     * @param principal
     * @param id
     * @return
     */
    ListEvaluatePeriodicMobileResponse revokeParentReply(UserPrincipal principal, Long id);

    /**
     * @param principal
     * @param id
     * @return
     */
    int parentView(UserPrincipal principal, Long id);
}
