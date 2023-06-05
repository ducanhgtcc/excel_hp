package com.example.onekids_project.mobile.plus.response.fees;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-07 08:58
 *
 * @author lavanviet
 */
@Getter
@Setter
public class FeesClassResponse extends IdResponse {
    private String className;

    private int studentNumber;

    private int noOrderNumber;

    private int noPackageNumber;

    private int successYesNumber;

    private int successNoNumber;


}
