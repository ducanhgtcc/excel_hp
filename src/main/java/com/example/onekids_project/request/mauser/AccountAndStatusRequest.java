package com.example.onekids_project.request.mauser;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountAndStatusRequest extends IdRequest {
    private boolean status;

    @Override
    public String toString() {
        return "AccountAndStatusRequest{" +
                "status=" + status +
                "} " + super.toString();
    }
}
