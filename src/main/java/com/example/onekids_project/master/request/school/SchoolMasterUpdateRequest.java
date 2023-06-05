package com.example.onekids_project.master.request.school;

import com.example.onekids_project.master.response.MaUserOtherRequest;
import com.example.onekids_project.master.response.MaUserOtherResponse;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.school.SchoolUniqueResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class SchoolMasterUpdateRequest extends IdRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    private String phone;

    private String email;

    private LocalDate birthDay;

    private String gender;

    private String note;

    @Valid
    private MaUserOtherRequest maUser;
}
