package com.example.onekids_project.request.notifihistory;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSmsAppRevokeRequest extends IdRequest {

    private boolean sendDel;

    @Override
    public String toString() {
        return "UpdateSmsAppRevokeRequest{" +
                "sendDel=" + sendDel +
                "} " + super.toString();
    }
}
