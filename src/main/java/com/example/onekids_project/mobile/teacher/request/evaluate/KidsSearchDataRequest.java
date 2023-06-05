package com.example.onekids_project.mobile.teacher.request.evaluate;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class KidsSearchDataRequest extends PageNumberRequest {
    @NotNull
    private Long idKid;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
