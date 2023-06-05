package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateActiveCashRequest extends IdResponse {

    private boolean canceled;

    @Override
    public String toString() {
        return "UpdateActiveCashRequest{" +
                "canceled=" + canceled +
                "} " + super.toString();
    }
}
