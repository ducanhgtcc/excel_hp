package com.example.onekids_project.request.AppSend;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateKidsBirthdayRequest extends IdRequest {

    private boolean isApproved;

    private boolean sendDel;

    @Override
    public String toString() {
        return "UpdateKidsBirthdayRequest{" +
                "isApproved=" + isApproved +
                ", sendDel=" + sendDel +
                "} " + super.toString();
    }
}
