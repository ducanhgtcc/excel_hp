package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentResponse extends IdResponse {
    private String kidCode;

    private String fullName;

    private String nickName;

    private LocalDate dateStart;

    private LocalDate birthDay;

    private String gender;

    private String phone;

    private String phoneRepresentation;

    private boolean isActivated;

    private boolean smsReceive;

    private String username;

    private String password;

    private String login;

    private MaClassOtherResponse maClass;
}
