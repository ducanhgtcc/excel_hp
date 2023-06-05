package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentCashRequest extends IdResponse {

    private boolean payment;

    @Override
    public String toString() {
        return "UpdatePaymentCashRequest{" +
                "payment=" + payment +
                "} " + super.toString();
    }
}
