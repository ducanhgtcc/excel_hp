package com.example.onekids_project.master.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.response.school.SchoolOtherResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class StudentAdminResponse extends IdResponse {
    private String fullName;

    private LocalDate birthDay;

    private String gender;

    private String phone;

    private String phoneRepresentation;

    private boolean isActivated;

    private boolean smsSend;

    private boolean smsReceive;

    private String username;

    private String password;

    private String login;

    private String verifyCodeSchool;

    private String verifyCodeAdmin;

    private MaClassOtherResponse maClass;

    private String schoolName;

    //tài khoản phụ huynh đã được thêm số vào username
    private boolean invalidAccount;

    private LocalDateTime createdDate;
}
