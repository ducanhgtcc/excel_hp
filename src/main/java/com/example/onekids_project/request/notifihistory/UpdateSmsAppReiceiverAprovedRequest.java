package com.example.onekids_project.request.notifihistory;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSmsAppReiceiverAprovedRequest extends IdRequest {

    private boolean isApproved;

    @Override
    public String toString() {
        return "UpdateSmsAppReiceiverAprovedRequest{" +
                "isApproved=" + isApproved +
                "} " + super.toString();
    }
}
