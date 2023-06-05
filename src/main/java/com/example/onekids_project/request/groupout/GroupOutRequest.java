package com.example.onekids_project.request.groupout;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-07-12 2:28 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@ToString
public class GroupOutRequest {
    @NotBlank
    private String name;

    private String note;
}
