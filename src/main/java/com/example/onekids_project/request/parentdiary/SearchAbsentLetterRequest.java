package com.example.onekids_project.request.parentdiary;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class SearchAbsentLetterRequest extends PageNumberWebRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Boolean confirmStatus;

    private Long idGrade;

    private Long idClass;

    private String dateSick;

    private String name;

    private String statusfirst;

    @Override
    public String toString() {
        return "SearchAbsentLetterRequest{" +
                "date=" + date +
                ", confirmStatus=" + confirmStatus +
                ", idGrade=" + idGrade +
                ", idClass=" + idClass +
                ", dateSick='" + dateSick + '\'' +
                ", name='" + name + '\'' +
                ", statusfirst='" + statusfirst + '\'' +
                "} " + super.toString();
    }
}
