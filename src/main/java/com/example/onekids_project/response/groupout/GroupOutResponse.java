package com.example.onekids_project.response.groupout;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * date 2021-07-12 2:26 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class GroupOutResponse extends IdResponse {
    @NotNull
    private String name;

    private long number;

    private String note;

    private boolean defaultStatus;
}
