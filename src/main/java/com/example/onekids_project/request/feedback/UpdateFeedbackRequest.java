package com.example.onekids_project.request.feedback;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateFeedbackRequest extends IdRequest {

    private String schoolReply;

    private boolean schoolReplyDel;

    private String dataType;

    private boolean schoolUnread;

    private boolean schoolConfirmStatus;

    private Long idSchoolConfirm;

    private Long idSchoolReply;

    private LocalDateTime confirmDate;

    private LocalDateTime schoolTimeReply;

    @Override
    public String toString() {
        return "UpdateFeedbackRequest{" +
                "schoolReply='" + schoolReply + '\'' +
                ", schoolReplyDel=" + schoolReplyDel +
                ", dataType='" + dataType + '\'' +
                ", schoolUnread=" + schoolUnread +
                ", schoolConfirmStatus=" + schoolConfirmStatus +
                ", idSchoolConfirm=" + idSchoolConfirm +
                ", idSchoolReply=" + idSchoolReply +
                ", confirmDate=" + confirmDate +
                ", schoolTimeReply=" + schoolTimeReply +
                "} " + super.toString();
    }
}
