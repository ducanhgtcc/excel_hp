package com.example.onekids_project.mobile.teacher.request;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class BirthdayTeacherRequest{

    @NotBlank
    private String sendContent;

    private String urlPicture;

    @NotNull
    private List<Long> idKidList;
}
