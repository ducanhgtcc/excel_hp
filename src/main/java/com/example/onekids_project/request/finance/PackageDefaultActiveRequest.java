package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageDefaultActiveRequest extends IdRequest {
    private boolean active;

    @Override
    public String toString() {
        return "PackageDefaultActiveRequest{" +
                "active=" + active +
                "} " + super.toString();
    }
}
