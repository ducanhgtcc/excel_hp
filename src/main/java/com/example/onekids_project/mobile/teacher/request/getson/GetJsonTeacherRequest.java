package com.example.onekids_project.mobile.teacher.request.getson;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class GetJsonTeacherRequest  {

    private String dateInput;

}
