package com.example.onekids_project.mobile.parent.response.kids;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KidsParentResponse {
    private String fullName;

    private String gender;

    private LocalDate birthDay;

    private String className;

    private String avatar;
}
