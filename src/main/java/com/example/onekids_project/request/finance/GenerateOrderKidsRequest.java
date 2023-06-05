package com.example.onekids_project.request.finance;

import com.example.onekids_project.request.base.IdObjectRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-02-23 15:41
 *
 * @author lavanviet
 */
@Getter
@Setter
public class GenerateOrderKidsRequest {
    @NotNull
    private LocalDate date;

    @NotEmpty
    @Valid
    private List<IdObjectRequest> idKidList;
}
