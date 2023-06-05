package com.example.onekids_project.master.request.menu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-10-15 14:51
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class MenuSupportRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String link;

    private boolean plusStatus;

    private boolean teacherStatus;

    private boolean parentStatus;

}
