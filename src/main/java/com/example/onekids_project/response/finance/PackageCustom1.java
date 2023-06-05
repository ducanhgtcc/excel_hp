package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-26 12:04
 *
 * @author lavanviet
 */
@Getter
@Setter
public class PackageCustom1 extends IdResponse {

    private String name;

    private String category;

    private String type;

    private String unit;

    private boolean attendance;
}