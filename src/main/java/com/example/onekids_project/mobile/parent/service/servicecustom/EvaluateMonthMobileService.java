package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateMonthMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.ListEvaluateMonthMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

public interface EvaluateMonthMobileService {
    /**
     * find evaluate week of year
     *
     * @param principal
     * @param id
     * @return
     */
    EvaluateMonthMobileResponse findEvaluateMonth(UserPrincipal principal, Long id, Pageable pageable);

    /**
     * create parent reply
     *
     * @param principal
     * @param id
     * @param content
     * @return
     */
    ListEvaluateMonthMobileResponse createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException;

    /**
     * update
     *
     * @param principal
     * @param id
     * @param content
     * @return
     */
    ListEvaluateMonthMobileResponse updateParentReply(UserPrincipal principal, Long id, String content);

    /**
     * thu hồi
     *
     * @param principal
     * @param id
     * @return
     */
    ListEvaluateMonthMobileResponse revokeParentReply(UserPrincipal principal, Long id);

    /**
     * @param principal
     * @param id
     * @return
     */
    int parentView(UserPrincipal principal, Long id);
}
