package com.example.onekids_project.request.cashinternal;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class UpdatePeopleTypeRequest extends IdRequest {

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

    @Override
    public String toString() {
        return "UpdatePeopleTypeRequest{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", indentify='" + indentify + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
