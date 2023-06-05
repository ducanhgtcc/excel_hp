package com.example.onekids_project.request.cashinternal;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreateCashInternalRequest {

    @NotNull
    private Double money;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long idPeopleTypeInternal;

    @NotNull
    private Long idPeopleTypeOther;

    private boolean payment;

    private String content;

}
