package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class TransferEmployeeDepartmentRequest {
    @NotNull
    private Long id;//idDepartment

    private List<Long> idEmployeeInDepartmentList;

    @Override
    public String toString() {
        return "TransferEmployeeDepartmentRequest{" +
                "id=" + id +
                ", idEmployeeInDepartmentList=" + idEmployeeInDepartmentList +
                '}';
    }
}
