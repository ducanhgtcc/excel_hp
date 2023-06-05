package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KidsPackageObject extends IdRequest {
    private boolean active;

    @Override
    public String toString() {
        return "KidsPackageRequest{" +
                "active=" + active +
                "} " + super.toString();
    }
}
