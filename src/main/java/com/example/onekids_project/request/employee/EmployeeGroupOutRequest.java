package com.example.onekids_project.request.employee;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-07-14 4:58 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@ToString
public class EmployeeGroupOutRequest {

    @NotNull
    private Long idGroupOut;

    @NotEmpty
    private List<Long> idList;

    @NotNull
    private LocalDate dateOut;
}
