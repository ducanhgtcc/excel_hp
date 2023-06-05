package com.example.onekids_project.request.birthdaymanagement;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class SearchTeacherBirthDayRequest extends PageNumberWebRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate week;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate month;

    private String name;

    @Override
    public String toString() {
        return "SearchTeacherBirthDayRequest{" +
                "date=" + date +
                ", week=" + week +
                ", month=" + month +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}