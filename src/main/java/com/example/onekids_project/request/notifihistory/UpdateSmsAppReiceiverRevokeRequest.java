package com.example.onekids_project.request.notifihistory;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSmsAppReiceiverRevokeRequest extends IdRequest {

    private boolean sendDel;

    @Override
    public String toString() {
        return "UpdateSmsAppReiceiverRevokeRequest{" +
                "sendDel=" + sendDel +
                "} " + super.toString();
    }
}
