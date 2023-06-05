package com.example.onekids_project.response.birthdaymanagement;

import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListTeacherBirthDayResponse extends TotalResponse {
    List<TeacherBirthdayResponse> responseList;
}
