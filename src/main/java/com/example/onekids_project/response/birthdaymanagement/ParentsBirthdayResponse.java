package com.example.onekids_project.response.birthdaymanagement;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ParentsBirthdayResponse extends IdResponse {

    private String fullName;

    private String phone;

    private String gender;

    private LocalDate birthday;

    private String nameKid;

}
