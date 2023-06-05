package com.example.onekids_project.mobile.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * date 2021-06-07 16:39
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class IdAndStatusRequest {
    @NotNull
    private Long id;

    @NotNull
    private Boolean status;
}
