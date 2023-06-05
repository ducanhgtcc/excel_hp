package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApproveCashRequest extends IdResponse {

    private boolean isApproved;

    @Override
    public String toString() {
        return "UpdateApproveCashRequest{" +
                "isApproved=" + isApproved +
                "} " + super.toString();
    }
}
