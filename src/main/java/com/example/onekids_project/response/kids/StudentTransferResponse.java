package com.example.onekids_project.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudentTransferResponse extends IdResponse {
    private String kidCode;

    private String fullName;

    private LocalDate birthDay;

    private String gender;

    private String phone;

    private MaClassOtherResponse maClass;

    private int count;
}
