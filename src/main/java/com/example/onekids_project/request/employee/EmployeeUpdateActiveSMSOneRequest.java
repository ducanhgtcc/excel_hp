package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

@Data
public class EmployeeUpdateActiveSMSOneRequest extends IdRequest {

    private boolean smsReceive;
}
