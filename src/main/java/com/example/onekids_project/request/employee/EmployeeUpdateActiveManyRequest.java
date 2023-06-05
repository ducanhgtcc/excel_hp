package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EmployeeUpdateActiveManyRequest {
    @NotNull
    private List<Long> idList;

    private boolean status;
}
