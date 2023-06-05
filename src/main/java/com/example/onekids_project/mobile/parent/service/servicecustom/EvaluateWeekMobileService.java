package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.evaluate.EvaluateWeekMobileResponse;
import com.example.onekids_project.mobile.parent.response.evaluate.ListEvaluateWeekMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EvaluateWeekMobileService {
    /**
     * find evaluate week of year
     * @param principal
     * @param id
     * @return
     */
    EvaluateWeekMobileResponse findEvaluateWeek(UserPrincipal principal, Long id, Pageable pageable);

    /**
     * create parent reply
     * @param principal
     * @param id
     * @param content
     * @return
     */
    ListEvaluateWeekMobileResponse createParentReply(UserPrincipal principal, Long id, String content) throws FirebaseMessagingException;

    /**
     * update
     * @param principal
     * @param id
     * @param content
     * @return
     */
    ListEvaluateWeekMobileResponse updateParentReply(UserPrincipal principal, Long id, String content);

    /**
     * thu hồi
     * @param principal
     * @param id
     * @return
     */
    ListEvaluateWeekMobileResponse revokeParentReply(UserPrincipal principal, Long id);

    /**
     *
     * @param principal
     * @param id
     * @return
     */
    int parentView(UserPrincipal principal, Long id);
}
