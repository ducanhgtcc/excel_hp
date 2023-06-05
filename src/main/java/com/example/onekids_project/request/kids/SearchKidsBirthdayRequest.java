package com.example.onekids_project.request.kids;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Getter
@Setter
public class SearchKidsBirthdayRequest extends BaseRequest {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private LocalDate week;

    private LocalDate month;

    private String name;
}
