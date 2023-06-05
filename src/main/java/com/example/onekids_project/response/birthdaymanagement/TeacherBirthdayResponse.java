package com.example.onekids_project.response.birthdaymanagement;

import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ReceiversResponse;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TeacherBirthdayResponse extends IdResponse {

    private String phone;

    private String fullName;

    private LocalDate birthday;

    private String address;

    private String gender;

    private List<AppSendResponse> appSendList;

    private List<ReceiversResponse> receiversList;

    private int number;

    private MaClassOtherResponse maClassOtherResponse;

    private List<ExEmployeeClass> exEmployeeClassList;

}
