package com.example.onekids_project.request.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class CreateParentInforRequest {
    private String phone;

    private String fullName;
}
