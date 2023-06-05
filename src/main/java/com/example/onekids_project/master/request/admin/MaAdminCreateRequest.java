package com.example.onekids_project.master.request.admin;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class MaAdminCreateRequest {

    @NotBlank
    @Size(min = 6)
    private String username;

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

    private String note;

}
