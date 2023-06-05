package com.example.onekids_project.request.finance.extend;

import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.response.base.IdResponse;
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
public class PackageExtendUpdateRequest extends IdRequest {
    @NotBlank
    private String name;

    private String note;

    private boolean active;

    @NotEmpty
    @Valid
    private List<MoneyUpdateExtendRequest> moneyList;
}
