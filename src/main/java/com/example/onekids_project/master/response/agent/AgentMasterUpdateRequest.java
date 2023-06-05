package com.example.onekids_project.master.response.agent;

import com.example.onekids_project.master.response.MaUserOtherRequest;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class AgentMasterUpdateRequest extends IdRequest {
    @NotBlank
    private String fullName;

    private LocalDate birthDay;

    @NotBlank
    private String phone;

    private String email;

    @NotBlank
    private String gender;

    private String note;

    @Valid
    private MaUserOtherRequest maUser;
}
