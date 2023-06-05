package com.example.onekids_project.request.brand;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FindAgentBrandRequest extends PageNumberWebRequest {

    private String name;

    @Override
    public String toString() {
        return "FindAgentBrand{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}