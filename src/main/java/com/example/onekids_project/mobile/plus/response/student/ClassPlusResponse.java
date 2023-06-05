package com.example.onekids_project.mobile.plus.response.student;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClassPlusResponse extends IdResponse {
    String nameClass;

    int numberKid;

    List<String> teacherList;

}
