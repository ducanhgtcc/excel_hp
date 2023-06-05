package com.example.onekids_project.request.kids;

import com.example.onekids_project.request.base.PageNumberWebRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class SearchKidsGroupOutRequest extends PageNumberWebRequest {

    @NotNull
    private Long idGroupOut;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> dateInList;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate yearOut;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private List<LocalDate> birthdayList;

    private String fullName;

}
