package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.request.messageparent.MessageParentMobileRequest;
import com.example.onekids_project.mobile.parent.response.message.ListMessageParentMobileResponse;
import com.example.onekids_project.mobile.parent.response.message.MessageParentDetailMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;

public interface MessageParentMobileService {
    /**
     * tìm kiếm danh sách lời nhắn của phụ huynh
     * @param principal
     * @param pageable
     * @return
     */
    ListMessageParentMobileResponse findMessageParentMobile(UserPrincipal principal, Pageable pageable, LocalDateTime localDateTime);

    /**
     * thu hồi lời nhắn
     * @param id
     * @return
     */
    boolean messageParentRevoke(Long id);

    /**
     * tìm kiếm chi tiết cho một lời nhắn
     * @param id
     * @return
     */
    MessageParentDetailMobileResponse findMessParentDetailMobile(UserPrincipal principal, Long id);

    boolean createMessageParent(UserPrincipal principal, MessageParentMobileRequest messageParentMobileRequest) throws FirebaseMessagingException, IOException;
}
