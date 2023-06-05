package com.example.onekids_project.master.response.school;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.master.response.MaUserOtherResponse;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.school.SchoolUniqueResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
public class SchoolMasterResponse extends IdResponse {
    private String fullName;

    private String phone;

    private String email;

    private LocalDate birthDay;

    private String gender;

    private String note;

    private SchoolUniqueResponse school;

    private MaUserOtherResponse maUser;
}
