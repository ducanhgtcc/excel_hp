package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentGroupOutResponse extends IdResponse {
    private String kidCode;

    private String fullName;

    private LocalDate dateStart;

    private LocalDate birthDay;

    private String gender;

    private MaClassOtherResponse maClass;

    private LocalDate outDate;
}
