package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-06 16:12
 * 
 * @author lavanviet
 */
@Getter
@Setter
public class PackageCustom3 extends IdResponse {
    private String category;

    private String name;

    private String type;

    private String unit;

    private boolean attendance;
}
