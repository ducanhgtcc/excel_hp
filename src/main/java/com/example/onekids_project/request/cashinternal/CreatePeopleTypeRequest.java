package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class CreatePeopleTypeRequest {

    @NotBlank
    private String name;

    @NotBlank
    @StringInList(values = {FinanceConstant.INTERNAL, FinanceConstant.EXTERNAL})
    private String type;

    private String phone;

    private String birthday;

    private String gender;

    private String email;

    private String indentify;

    private String address;

    private String description;
}
