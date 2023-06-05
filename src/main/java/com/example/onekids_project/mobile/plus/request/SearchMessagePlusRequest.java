package com.example.onekids_project.mobile.plus.request;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class SearchMessagePlusRequest extends PageNumberRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Boolean confirmStatus;

    private Long idClass;

    private String kidName;


    @Override
    public String toString() {
        return "SearchMessagePlusRequest{" +
                "date=" + date +
                ", confirmStatus=" + confirmStatus +
                ", idClass=" + idClass +
                ", kidName='" + kidName + '\'' +
                "} " + super.toString();
    }
}

