package com.example.onekids_project.master.request.admin;

import com.example.onekids_project.master.response.MaUserOtherRequest;
import com.example.onekids_project.master.response.MaUserOtherResponse;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class MaAdminUpdateRequest extends IdRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String address;

    @NotBlank
    private String gender;

    @NotBlank
    private String phone;

    @NotNull
    private LocalDate startDate;

    @NotBlank
    private String adminStatus;

    private String note;

    @Valid
    private MaUserOtherRequest maUser;

}
