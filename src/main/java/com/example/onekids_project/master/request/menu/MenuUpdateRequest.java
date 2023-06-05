package com.example.onekids_project.master.request.menu;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * date 2021-10-16 08:50
 *
 * @author lavanviet
 */
@Getter
@Setter
@ToString
public class MenuUpdateRequest extends IdRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String link;

    private boolean plusStatus;

    private boolean teacherStatus;

    private boolean parentStatus;
}
