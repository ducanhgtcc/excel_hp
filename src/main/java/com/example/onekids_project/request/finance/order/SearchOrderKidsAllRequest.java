package com.example.onekids_project.request.finance.order;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * date 2021-03-03 15:13
 *
 * @author lavanviet
 */
@Data
public class SearchOrderKidsAllRequest {
    @NotNull
    private Long idKid;

    private Integer year;
}
