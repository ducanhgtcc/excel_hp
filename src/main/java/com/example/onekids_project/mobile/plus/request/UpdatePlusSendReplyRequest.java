package com.example.onekids_project.mobile.plus.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdatePlusSendReplyRequest extends IdRequest {

    @NotBlank
    private String schoolReply;

    @Override
    public String toString() {
        return "UpdatePlusSendReplyRequest{" +
                "schoolReply='" + schoolReply + '\'' +
                "} " + super.toString();
    }
}
