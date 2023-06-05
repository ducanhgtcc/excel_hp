package com.example.onekids_project.request.mauser;

import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class AccountRequest extends IdRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    @Size(min = 6)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String passwordShow;

    private boolean activated;

    private boolean trialStatus;

    private LocalDate fromDate;

    private LocalDate toDate;

    private boolean unlimitTime;

    private boolean demoStatus;

    private LocalDate fromDemoDate;

    private LocalDate toDemoDate;
}
