package com.example.onekids_project.mobile.plus.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePlusRevokeRequest extends IdRequest {

    private String keyType;

    @Override
    public String toString() {
        return "UpdatePlusRevokeRequest{" +
                "keyType='" + keyType + '\'' +
                "} " + super.toString();
    }
}
