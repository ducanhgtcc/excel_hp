package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddPackageClassRequest extends IdRequest {
    private List<Long> idPackageList;

    @Override
    public String toString() {
        return "AddPackageClassRequest{" +
                "idPackageList=" + idPackageList +
                "} " + super.toString();
    }
}
