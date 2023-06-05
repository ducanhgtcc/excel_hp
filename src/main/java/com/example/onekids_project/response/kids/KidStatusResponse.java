package com.example.onekids_project.response.kids;

import com.example.onekids_project.enums.StudentStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidStatusResponse {
    private StudentStatusEnum key;

    private String value;
}
