package com.example.onekids_project.response.finance.extend;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.finance.PackageOtherObject;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-10-05 08:49
 *
 * @author lavanviet
 */
@Getter
@Setter
public class PackageExtendResponse extends IdResponse {
    private String name;

    private String note;

    private boolean active;

    private int number;

    private PackageOtherObject fnPackage;
}
