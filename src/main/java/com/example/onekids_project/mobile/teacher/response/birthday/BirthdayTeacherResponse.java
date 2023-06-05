package com.example.onekids_project.mobile.teacher.response.birthday;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class BirthdayTeacherResponse extends IdResponse {

    private String nameKid;

    private String birthDay;

    private String avatar;

    boolean status;

}

