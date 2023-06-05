package com.example.onekids_project.model.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-06 15:47
 *
 * @author lavanviet
 */
@Getter
@Setter
public class NameActiveModel extends IdResponse {
    private String name;

    private boolean active;
}
