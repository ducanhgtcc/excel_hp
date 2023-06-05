package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class KidsPackageDefaultRequest extends IdRequest {
    private String fullName;

    @Valid
    private List<KidsPackageObject> fnKidsPackageDefaultList;

    @Override
    public String toString() {
        return "KidsPackageRequest{" +
                "fullName='" + fullName + '\'' +
                ", fnKidsPackageDefaultList=" + fnKidsPackageDefaultList +
                "} " + super.toString();
    }
}
