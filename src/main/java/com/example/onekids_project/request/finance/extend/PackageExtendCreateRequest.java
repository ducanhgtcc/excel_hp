package com.example.onekids_project.request.finance.extend;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * date 2021-10-01 15:50
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class PackageExtendCreateRequest {
    @NotNull
    private Long idPackage;

    @NotBlank
    private String name;

    private String note;

    private boolean active;

    @NotEmpty
    @Valid
    private List<MoneyCreateExtendRequest> moneyList;
}
