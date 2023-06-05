package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-10-15 14:56
 *
 * @author lavanviet
 */
@Getter
@Setter
public class MenuSupportResponse extends IdResponse {
    private String name;

    private String link;

    private boolean plusStatus;

    private boolean teacherStatus;

    private boolean parentStatus;
}
