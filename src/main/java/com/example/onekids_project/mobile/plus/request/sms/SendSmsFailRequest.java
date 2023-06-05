package com.example.onekids_project.mobile.plus.request.sms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SendSmsFailRequest {

    @NotEmpty
    @NotNull
    List<Long> idList; // id người nhận

    @NotNull
    Long id; // id thông báo sms
}
