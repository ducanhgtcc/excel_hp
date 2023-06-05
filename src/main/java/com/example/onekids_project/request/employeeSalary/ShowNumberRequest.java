package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * date 2021-02-20 11:15 SA
 *
 * @author ADMIN
 */
@Data
public class ShowNumberRequest extends IdRequest {

    @NotNull
    private int userNumber;

}
