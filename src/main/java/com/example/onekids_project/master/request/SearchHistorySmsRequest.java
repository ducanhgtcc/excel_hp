package com.example.onekids_project.master.request;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class SearchHistorySmsRequest {

    @NotNull
    private Long idSchool;

    private String appType;

    private String typeSend;

    private String fromTimeSend;

    private String toTimeSend;
}