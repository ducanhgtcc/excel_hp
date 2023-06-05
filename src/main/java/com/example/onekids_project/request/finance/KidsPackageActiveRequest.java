package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class KidsPackageActiveRequest extends IdRequest {
    private String fullName;

    @Valid
    private List<KidsPackageObject> fnKidsPackageList;

    @Override
    public String toString() {
        return "KidsPackageActiveRequest{" +
                "fullName='" + fullName + '\'' +
                ", fnKidsPackageList=" + fnKidsPackageList +
                "} " + super.toString();
    }
}
