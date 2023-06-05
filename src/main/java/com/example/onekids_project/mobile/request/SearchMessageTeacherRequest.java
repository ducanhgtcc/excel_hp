package com.example.onekids_project.mobile.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SearchMessageTeacherRequest extends PageNumberRequest {

    private String date;

    private Boolean confirmStatus;

    private String keyWord;
}
