package com.example.onekids_project.request.parentdiary;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Getter
@Setter
public class SearchMessageParentRequest extends PageNumberWebRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private Boolean confirmStatus;

    private Long idGrade;

    private Long idClass;

    private String name;
    @Override
    public String toString() {
        return "SearchMessageParentRequest{" +
                "date=" + date +
                ", confirmStatus=" + confirmStatus +
                ", idGrade=" + idGrade +
                ", idClass=" + idClass +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
