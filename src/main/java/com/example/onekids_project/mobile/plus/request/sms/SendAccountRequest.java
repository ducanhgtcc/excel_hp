package com.example.onekids_project.mobile.plus.request.sms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SendAccountRequest {
    @NotEmpty
    @NotNull
    List<Long> idList;
}
