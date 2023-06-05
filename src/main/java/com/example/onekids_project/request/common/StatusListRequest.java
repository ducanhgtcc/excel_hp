package com.example.onekids_project.request.common;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * date 2021-02-24 13:41
 *
 * @author lavanviet
 */
@Data
public class StatusListRequest {
    @NotNull
    private Boolean status;

    @NotEmpty
    private List<Long> idList;
}
