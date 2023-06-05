package com.example.onekids_project.master.request.school;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author lavanviet
 */
@Data
public class GroupTypeRequest {
    @NotEmpty
    private List<Long> idList;

    @NotBlank
    private String groupType;
}
