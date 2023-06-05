package com.example.onekids_project.response.mauser;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KidsInforMauserResponse extends IdResponse {
    private String fullName;

    private LocalDate birthDay;

    private MaClassOtherResponse maClass;

    private String schoolName;


}
